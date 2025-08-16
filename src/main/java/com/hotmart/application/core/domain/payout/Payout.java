package com.hotmart.application.core.domain.payout;

import com.hotmart.application.core.domain.purchase.Purchase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class Payout {

    private final static BigDecimal MONTHLY_DISCOUNT_ON_INSTALLMENTS = BigDecimal.valueOf(0.5);

    private Long id;

    private Purchase purchase;

    private BigDecimal amount;

    private BigDecimal platformFee;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private PayoutStatus status;

    public Payout() { }

    public Payout(Purchase purchase, int numberOfInstallments) {
        this.purchase = purchase;
        this.amount = calculateAmountForPayout(numberOfInstallments);
        this.platformFee = purchase.getFeeProduct();
        this.status = PayoutStatus.PENDING;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private BigDecimal calculateAmountForPayout(int installments) {
        BigDecimal feeRate = getFeeRate();
        BigDecimal feeAmount = getFeeAmount(feeRate);
        BigDecimal amountAfterFee = getAmountAfterFeeAmount(feeAmount);

        if (isInstallmentWithProducerInterest(installments)) {
            return applyInstallmentInterestDiscount(installments, amountAfterFee);
        }

        return amountAfterFee.setScale(2, RoundingMode.HALF_UP);
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

    private boolean isInstallmentWithProducerInterest(int installments) {
        return installments > 0 && this.purchase.isInterestBorneByProductOwner();
    }

    private BigDecimal applyInstallmentInterestDiscount(int numberOfInstallments, BigDecimal amountAfterFee) {
        updateStatusToConfirmed(); // TODO preciso desse status?
        BigDecimal installmentInterest = calculateInstallmentInterestAmount(numberOfInstallments);
        return amountAfterFee.subtract(installmentInterest).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInstallmentInterestAmount(int numberOfInstallments) {
        BigDecimal amountAfterFeePerMonth = MONTHLY_DISCOUNT_ON_INSTALLMENTS
                .multiply(BigDecimal.valueOf(numberOfInstallments))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return this.purchase.getPrice().multiply(amountAfterFeePerMonth);
    }

    private void updateStatusToConfirmed() {
        this.status = PayoutStatus.CONFIRMED;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public BigDecimal getPlatformFee() {
        return platformFee;
    }

    public PayoutStatus getStatus() {
        return status;
    }
}
