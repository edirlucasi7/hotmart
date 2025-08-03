package com.desafio.hotmart.clientPayout;

import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.core.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

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

    public Payout(Purchase purchase, int numberOfInstallments) {
        this.purchase = purchase;
        this.amount = calculateAmountForPayout(numberOfInstallments);
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

    private BigDecimal calculateAmountForPayout(int installments) {
        BigDecimal feeRate = getFeeRate();
        BigDecimal feeAmount = getFeeAmount(feeRate);
        BigDecimal amountAfterFee = getAmountAfterFeeAmount(feeAmount);

        if (installmentPurchaseWithInterestForTheProducer(installments)) {
            return applyAmountForPurchaseWith(installments, amountAfterFee);
        }

        return amountAfterFee.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean installmentPurchaseWithInterestForTheProducer(int installments) {
        return installments > 0 && this.purchase.isInterestBorneByProductOwner();
    }

    private BigDecimal applyAmountForPurchaseWith(int numberOfInstallments, BigDecimal amountAfterFee) {
        updateStatusToConfirmed();
        BigDecimal installmentInterest = calculateInterestFor(numberOfInstallments);
        return amountAfterFee.subtract(installmentInterest).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getAmountAfterFeeAmount(BigDecimal feeAmount) {
        return this.purchase.getPrice().subtract(feeAmount);
    }

    private BigDecimal getFeeAmount(BigDecimal feeRate) {
        return this.purchase.getPrice().multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getFeeRate() {
        return this.purchase.getFeeProduct().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInterestFor(int numberOfInstallments) {
        BigDecimal amountAfterFeePerMonth = MONTHLY_DISCOUNT_ON_INSTALLMENTS
                .multiply(BigDecimal.valueOf(numberOfInstallments))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return this.purchase.getPrice().multiply(amountAfterFeePerMonth);
    }

    private void updateStatusToConfirmed() {
        this.status = PayoutStatus.CONFIRMED;
    }
}