package com.desafio.hotmart.coupon;

import com.desafio.hotmart.purchase.Purchase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    @JoinColumn
    private Purchase purchase;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expirationAt;

    @Deprecated
    public Coupon() { }

    public Coupon(String code, Purchase purchase, LocalDateTime expirationAt) {
        this.code = code;
        this.purchase = purchase;
        this.expirationAt = expirationAt;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public Long getId() {
        return id;
    }
}