package com.hotmart.application.core.service.purchase;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.payout.Payout;
import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.purchase.NewPurchaseContract;
import com.hotmart.application.core.domain.purchase.Purchase;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.application.core.domain.user.User;
import com.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.hotmart.infrastructure.adapter.in.purchase.PurchaseServicePort;
import com.hotmart.infrastructure.adapter.in.user.UserServicePort;
import com.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;
import jakarta.transaction.Transactional;

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

    @Transactional
    @Override
    public void save(NewPurchaseContract newPurchaseContract, Coupon coupon, boolean isSmartPayment) throws IllegalArgumentException {
        Optional<Product> possibleProduct = productServicePort.findByIdAndActiveTrue(newPurchaseContract.getProductCode());
        if (possibleProduct.isEmpty()) throw new IllegalArgumentException("Product is not available");

        User client = userServicePort.getBy(newPurchaseContract.getEmail());
        Product product = possibleProduct.get();
        PurchaseType purchaseType = PurchaseType.getByName(newPurchaseContract.getUpperCaseType());

        // TODO como validar isso na request? o user pode ser criado logo acima
        if (ifClientAlreadyHasProduct(client, product))
            throw new IllegalArgumentException("The client already has the valid purchase for the product");

        if (purchaseType.isSmartPaymentWithCreditCard(isSmartPayment)
                && !product.hasValidNumberOfInstallments(newPurchaseContract.getInstallments()))
            throw new IllegalArgumentException("The number of installments of a smart payment must be the maximum "
                    + "allowed by product and have the type credit card.");

        Purchase newPurchase = new Purchase(client, coupon, product, purchaseType);
        Payout newPayout = new Payout(new PurchaseEntity(newPurchase, isSmartPayment).toPurchase(), newPurchaseContract.getInstallments());
        purchaseRepositoryPort.save(newPurchase, newPayout, isSmartPayment);
    }

    private boolean ifClientAlreadyHasProduct(User client, Product product) {
        return purchaseRepositoryPort.hasValidPurchaseAssociatedWith(client.getId(), product.getCode());
    }
}