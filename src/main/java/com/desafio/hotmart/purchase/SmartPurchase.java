package com.desafio.hotmart.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.desafio.hotmart.purchase.State.WAIT;

@Entity
public class SmartPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Purchase purchase;

    private int installmentNumber;

    @Min(value = 0)
    private BigDecimal amount;

    @NotNull
    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private State state = WAIT;

    @Min(value = 0)
    private int retryAttempt = 0;

    @Deprecated
    public SmartPurchase() { }

    private SmartPurchase(Purchase purchase, int installmentNumber, BigDecimal amount) {
        this.purchase = purchase;
        this.installmentNumber = installmentNumber;
        this.amount = amount;
    }

    public static List<SmartPurchase> createSmartPurchase(Purchase purchase, int maximumNumberOfInstallments, BigDecimal installmentPrice) {
        return IntStream.range(1, maximumNumberOfInstallments + 1)
                .mapToObj(installment -> new SmartPurchase(purchase, installment, installmentPrice))
                .toList();
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public State getState() {
        return state;
    }

    public int getRetryAttempt() {
        return retryAttempt;
    }
}