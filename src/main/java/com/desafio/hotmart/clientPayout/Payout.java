package com.desafio.hotmart.clientPayout;

import com.desafio.hotmart.purchase.Purchase;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

// TODO talvez usar schedule para pagar os infoprodutores, não sei
@Entity
public class Payout {

    private final static BigDecimal MONTHLY_DISCOUNT_ON_INSTALLMENTS = new BigDecimal("0.5");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    private Purchase purchase;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal platformFee;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PayoutStatus status;

    @Deprecated
    public Payout() { }

    public Payout(Purchase purchase) {
        this.purchase = purchase;
        this.amount = calculateAmountForPayout();
        this.platformFee = purchase.getFeeProduct();
        this.status = PayoutStatus.PENDING;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public User getProducer() {
        return purchase.getProductOwner();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PayoutStatus getStatus() {
        return status;
    }

    private BigDecimal calculateAmountForPayout() {
        BigDecimal feeRate = this.purchase.getFeeProduct().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal feeAmount = this.purchase.getPrice().multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountAfterFee = this.purchase.getPrice().subtract(feeAmount);

        if (this.purchase.isCreditCardInterestBorneByProductOwner()) {
            updateStatusForCreditCardPurchase();
            BigDecimal installmentInterest = calculateInterestOnInstallments();
            return amountAfterFee.subtract(installmentInterest).setScale(2, RoundingMode.HALF_UP);
        }

        return amountAfterFee.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInterestOnInstallments() {
        // TODO essa regra não está normalizada no banco, deveria? acho que sim
        BigDecimal amountAfterFeePerMonth = MONTHLY_DISCOUNT_ON_INSTALLMENTS
                .multiply(BigDecimal.valueOf(this.purchase.getNumberOfInstallments()))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return this.purchase.getPrice().multiply(amountAfterFeePerMonth);
    }

    private void updateStatusForCreditCardPurchase() {
        this.status = PayoutStatus.CONFIRMED;
    }
}