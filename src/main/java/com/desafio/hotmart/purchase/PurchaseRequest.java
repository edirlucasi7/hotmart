package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(@NotBlank String productCode,
                              @NotBlank String email,
                              @NotBlank String type,
                              @NotNull @Min(value = 1) int numberOfInstallments) {

    public Purchase toPurchase(User user, Product product) {
        PurchaseType purchaseType = PurchaseType.getByName(type);
        return Purchase.newPurchase(user, purchaseType, product.getPrice(), purchaseType.isRecurring(), numberOfInstallments, product);
    }
}
