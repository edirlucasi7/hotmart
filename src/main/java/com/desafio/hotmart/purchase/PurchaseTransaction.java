package com.desafio.hotmart.purchase;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class PurchaseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private SmartPurchase smartPurchase;

    @NotBlank
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private State state;

    @NotNull
    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Deprecated
    public PurchaseTransaction() { }

    public PurchaseTransaction(SmartPurchase smartPurchase, String transactionId, State state, LocalDateTime updatedAt) {
        this.smartPurchase = smartPurchase;
        this.transactionId = transactionId;
        this.state = state;
        this.updatedAt = updatedAt;
    }

    public SmartPurchase getSmartPurchase() {
        return smartPurchase;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public State getState() {
        return state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
