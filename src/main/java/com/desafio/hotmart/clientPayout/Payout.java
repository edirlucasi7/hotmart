package com.desafio.hotmart.clientPayout;

import com.desafio.hotmart.purchase.Purchase;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// TODO talevez usar schedule para pagar os clientes, nao sei
@Entity
public class Payout {

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

    public Payout(Purchase purchase, BigDecimal amount, BigDecimal platformFree) {
        this.purchase = purchase;
        this.amount = amount;
        this.platformFee = platformFree;
        this.status = PayoutStatus.PENDING;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public User getUser() {
        return purchase.getUser();
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
}