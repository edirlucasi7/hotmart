package com.desafio.hotmart.infrastructure.adapter.out.payout.entity;

import com.desafio.hotmart.application.core.domain.payout.Payout;
import com.desafio.hotmart.application.core.domain.payout.PayoutStatus;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payout")
public class PayoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    private PurchaseEntity purchase;

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
    public PayoutEntity() { }

    public PayoutEntity(PurchaseEntity purchaseEntity, Payout payout) {
        this.purchase = purchaseEntity;
        this.amount = payout.getAmount();
        this.platformFee = payout.getPlatformFee();
        this.status = payout.getStatus();
    }
}