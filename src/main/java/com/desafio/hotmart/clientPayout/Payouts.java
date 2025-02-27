package com.desafio.hotmart.clientPayout;

import com.desafio.hotmart.purchase.Purchase;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payouts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Purchase purchase;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal platformFee;

    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    private PayoutStatus status;

    @Deprecated
    public Payouts() { }

    public Payouts(Purchase purchase, User user, BigDecimal amount, BigDecimal platformFree) {
        this.purchase = purchase;
        this.user = user;
        this.amount = amount;
        this.platformFee = platformFree;
        this.status = PayoutStatus.PENDING; // AQUI CRIA PENDING, DEPOIS QUE A FILA PROCESSA, MUDA STATUS
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public User getUser() {
        return user;
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

    public PayoutStatus getStatus() {
        return status;
    }
}