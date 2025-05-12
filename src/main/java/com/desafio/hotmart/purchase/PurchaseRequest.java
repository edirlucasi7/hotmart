package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PurchaseRequest(@NotBlank String productCode,
                              @NotBlank String email,
                              @NotBlank String type,
                              @NotNull Integer installments) {

    public Purchase toPurchaseWithCouponDiscount(User user, Product product, BigDecimal discountAmount) {
        return new Purchase(user, product.calculatePriceWithDiscount(discountAmount), product);
    }

    public Payout toPayout(Purchase purchase) {
        return new Payout(purchase, installments);
    }
}