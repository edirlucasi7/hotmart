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

    public Purchase toPurchaseWithDiscount(User user, Product product, BigDecimal discountAmount) {
        PurchaseType purchaseType = PurchaseType.getByName(type);
        return Purchase.newPurchase(user,
                purchaseType,
                product.calculatePriceWithDiscount(discountAmount),
                purchaseType.isRecurring(),
                setNumberOfInstallmentsFor(purchaseType), product);
    }

    public Payout toPayout(Purchase purchase) {
        BigDecimal amountForPayout = purchase.calculateAmountForPayout();
        return new Payout(purchase, amountForPayout, purchase.getFeeProduct());
    }

    // TODO AINDA PODE SER DUPLICADO EM ALGUM MOMENTO?! ACHO QUE SIM
    public String generatePixCode() {
        return Tsid.fast().toString();
    }

    private int setNumberOfInstallmentsFor(PurchaseType purchaseType) {
        if (!purchaseType.isRecurring()) return 1;
        return numberOfInstallments;
    }
}