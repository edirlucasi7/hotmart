package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PurchaseRequest(@NotBlank String productCode,
                              @NotBlank String email,
                              @NotBlank String type,
                              int numberOfInstallments,
                              int confirmationTime) {

    public Purchase toPurchase(User user, Product product) {
        PurchaseType purchaseType = PurchaseType.getByName(type);
        return Purchase.newPurchase(user,
                purchaseType,
                product.getPrice(),
                purchaseType.isRecurring(),
                setNumberOfInstallmentsFor(purchaseType), product);
    }

    public Purchase toPurchaseWithDiscount(User user, Product product, BigDecimal discountAmount) {
        PurchaseType purchaseType = PurchaseType.getByName(type);
        return Purchase.newPurchase(user,
                purchaseType,
                product.calculatePriceWithDiscount(discountAmount),
                purchaseType.isRecurring(),
                setNumberOfInstallmentsFor(purchaseType), product);
    }

    private int setNumberOfInstallmentsFor(PurchaseType purchaseType) {
        if (!purchaseType.isRecurring()) return 1;
        return numberOfInstallments;
    }
}