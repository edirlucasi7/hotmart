package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import com.github.f4b6a3.tsid.Tsid;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PurchaseRequest(@NotBlank String productCode,
                              @NotBlank String email,
                              @NotBlank String type,
                              int numberOfInstallments) {

    public Purchase toPurchaseWithCouponDiscount(User user, Product product, BigDecimal discountAmount, boolean smartPayment) {
        PurchaseType purchaseType = PurchaseType.getByName(type);
        return Purchase.newPurchase(user,
                purchaseType,
                product.calculatePriceWithDiscount(discountAmount),
                purchaseType.isRecurring(),
                setNumberOfInstallmentsFor(purchaseType),
                product,
                smartPayment);
    }

    public Payout toPayout(Purchase purchase) {
        return new Payout(purchase);
    }

    public String generatePixCode() {
        return Tsid.fast().toString();
    }

    private int setNumberOfInstallmentsFor(PurchaseType purchaseType) {
        if (!purchaseType.isRecurring() || numberOfInstallments == 0) return 1;
        return numberOfInstallments;
    }
}