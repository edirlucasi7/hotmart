package com.hotmart.application.core.service.purchase;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.payout.Payout;
import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.purchase.NewPurchaseContract;
import com.hotmart.application.core.domain.purchase.Purchase;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.application.core.domain.user.User;
import com.hotmart.infrastructure.adapter.in.purchase.validator.PurchaseServiceValidator;
import com.hotmart.infrastructure.adapter.in.purchase.validator.PurchaseValidationResponse;
import com.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.hotmart.infrastructure.adapter.in.purchase.PurchaseServicePort;
import com.hotmart.infrastructure.adapter.in.user.UserServicePort;
import com.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;
import jakarta.transaction.Transactional;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class PurchaseCreateService implements PurchaseServicePort {

    private final PurchaseRepositoryPort purchaseRepositoryPort;
    private final ProductServicePort productServicePort;
    private final UserServicePort userServicePort;
    private final PurchaseServiceValidator purchaseServiceValidator;

    public PurchaseCreateService(PurchaseRepositoryPort purchaseRepositoryPort, ProductServicePort productServicePort, UserServicePort userServicePort, PurchaseServiceValidator purchaseServiceValidator) {
        this.purchaseRepositoryPort = purchaseRepositoryPort;
        this.productServicePort = productServicePort;
        this.userServicePort = userServicePort;
        this.purchaseServiceValidator = purchaseServiceValidator;
    }

    @Transactional
    @Override
    public void save(NewPurchaseContract newPurchaseContract, @Nullable Coupon coupon) {
        Optional<Product> possibleProduct = productServicePort.findByIdAndActiveTrue(newPurchaseContract.getProductCode());
        if (possibleProduct.isEmpty()) throw new IllegalArgumentException("Product is not available");

        User client = userServicePort.getByOrCreate(newPurchaseContract.getEmail());
        Product product = possibleProduct.get();

        validateBy(newPurchaseContract, client, product);
        createWith(newPurchaseContract, coupon, client, product);
    }

    private void validateBy(NewPurchaseContract newPurchaseContract, User client, Product product) {
        purchaseServiceValidator.process(
                new PurchaseValidationResponse(
                        PurchaseType.getByName(newPurchaseContract.getUpperCaseType()),
                        client,
                        product,
                        newPurchaseContract.getInstallments(),
                        newPurchaseContract.isSmartPayment()));
    }

    private void createWith(NewPurchaseContract newPurchaseContract, Coupon coupon, User client, Product product) {
        PurchaseType purchaseType = PurchaseType.getByName(newPurchaseContract.getUpperCaseType());

        Purchase newPurchase = new Purchase(client, coupon, product, purchaseType);
        Payout newPayout = new Payout(new PurchaseEntity(newPurchase, newPurchaseContract.isSmartPayment()).toPurchase(), newPurchaseContract.getInstallments());

        purchaseRepositoryPort.save(newPurchase, newPayout, newPurchaseContract.isSmartPayment());
    }
}