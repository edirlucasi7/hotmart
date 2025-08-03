package com.desafio.hotmart.application.core.service.purchase;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.desafio.hotmart.infrastructure.adapter.in.purchase.PurchaseServicePort;
import com.desafio.hotmart.infrastructure.adapter.in.user.UserServicePort;
import com.desafio.hotmart.application.core.domain.purchase.NewPurchaseContract;
import com.desafio.hotmart.application.core.domain.purchase.PurchaseType;

import java.math.BigDecimal;
import java.util.Optional;

public class PurchaseService implements PurchaseServicePort {

    private final PurchaseRepositoryPort purchaseRepositoryPort;
    private final ProductServicePort productServicePort;
    private final UserServicePort userServicePort;

    public PurchaseService(PurchaseRepositoryPort purchaseRepositoryPort, ProductServicePort productServicePort, UserServicePort userServicePort) {
        this.purchaseRepositoryPort = purchaseRepositoryPort;
        this.productServicePort = productServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public void save(NewPurchaseContract newPurchaseContract, BigDecimal discount, boolean isSmartPayment) throws IllegalArgumentException {
        Optional<Product> possibleProduct = productServicePort.findByIdAndActiveTrue(newPurchaseContract.getProductCode());
        if (possibleProduct.isEmpty())
            throw new IllegalArgumentException("Product is not available");

        User client = userServicePort.getBy(newPurchaseContract.getEmail());
        Product product = possibleProduct.get();
        PurchaseType purchaseType = PurchaseType.getByName(newPurchaseContract.getUpperCaseType());

        if (ifClientAlreadyHasProduct(client, product))
            throw new IllegalArgumentException("The client already has the valid purchase for the product");

        if (isSmartPaymentValid(isSmartPayment, purchaseType)
                && !hasValidNumberOfInstallments(newPurchaseContract, product))
            throw new IllegalArgumentException("The number of installments of a smart payment must be the maximum " +
                    "allowed by product and have the type credit card.");

        Purchase newPurchase = new Purchase(client, product.calculatePriceWithDiscount(discount), product, purchaseType);
        Payout payout = new Payout(newPurchase, newPurchaseContract.getInstallments());
        purchaseRepositoryPort.save(newPurchase, payout, isSmartPayment);
    }

    private boolean isSmartPaymentValid(boolean isSmartPayment, PurchaseType purchaseType) {
        return isSmartPayment && purchaseType.isCreditCard();
    }

    private boolean hasValidNumberOfInstallments(NewPurchaseContract newPurchaseContract, Product product) {
        return !hasMaximumNumberOfInstallmentsFromActiveOffer(newPurchaseContract, product);
    }

    private boolean hasMaximumNumberOfInstallmentsFromActiveOffer(NewPurchaseContract newPurchaseContract, Product product) {
        return newPurchaseContract.getInstallments() == product.getMaximumNumberOfInstallmentsFromActiveOffer();
    }

    private boolean ifClientAlreadyHasProduct(User client, Product product) {
        return purchaseRepositoryPort.hasValidPurchaseAssociatedWith(client.getId(), product.getCode());
    }
}
