package com.desafio.hotmart.application.core.service.purchase;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.payout.Payout;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.purchase.NewPurchaseContract;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.core.domain.purchase.PurchaseType;
import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.desafio.hotmart.infrastructure.adapter.in.purchase.PurchaseServicePort;
import com.desafio.hotmart.infrastructure.adapter.in.user.UserServicePort;
import com.desafio.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;

import java.util.Optional;

public class PurchaseCreateService implements PurchaseServicePort {

    private final PurchaseRepositoryPort purchaseRepositoryPort;
    private final ProductServicePort productServicePort;
    private final UserServicePort userServicePort;

    public PurchaseCreateService(PurchaseRepositoryPort purchaseRepositoryPort, ProductServicePort productServicePort, UserServicePort userServicePort) {
        this.purchaseRepositoryPort = purchaseRepositoryPort;
        this.productServicePort = productServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    public void save(NewPurchaseContract newPurchaseContract, Coupon coupon, boolean isSmartPayment) throws IllegalArgumentException {
        Optional<Product> possibleProduct = productServicePort.findByIdAndActiveTrue(newPurchaseContract.getProductCode());
        if (possibleProduct.isEmpty()) throw new IllegalArgumentException("Product is not available");

        User client = userServicePort.getBy(newPurchaseContract.getEmail());
        Product product = possibleProduct.get();
        PurchaseType purchaseType = PurchaseType.getByName(newPurchaseContract.getUpperCaseType());

        // TODO precisa ter validação na request, só estoura erro se for erro de programação
        if (ifClientAlreadyHasProduct(client, product)) throw new IllegalArgumentException("The client already has the valid purchase for the product");

        // TODO precisa ter validação na request, só estoura erro se for erro de programação
        if (isSmartPaymentValid(isSmartPayment, purchaseType)
                && !hasValidNumberOfInstallments(newPurchaseContract, product))
            throw new IllegalArgumentException("The number of installments of a smart payment must be the maximum " + "allowed by product and have the type credit card.");

        Purchase newPurchase = new Purchase(client, coupon, product, purchaseType);
        Payout newPayout = new Payout(new PurchaseEntity(newPurchase, isSmartPayment).toPurchase(), newPurchaseContract.getInstallments());
        purchaseRepositoryPort.save(newPurchase, newPayout, isSmartPayment);
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