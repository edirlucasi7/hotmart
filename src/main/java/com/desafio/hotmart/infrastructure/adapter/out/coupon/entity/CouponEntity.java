package com.desafio.hotmart.infrastructure.adapter.out.coupon.entity;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.coupon.Status;
import com.desafio.hotmart.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private BigDecimal discountValue;

    @ManyToOne
    @JoinColumn
    private Product product;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expirationAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Deprecated
    public CouponEntity() { }

    public CouponEntity(String code, BigDecimal discountValue, Product product, LocalDateTime expirationAt) {
        this.code = code;
        this.discountValue = discountValue;
        this.product = product;
        this.expirationAt = expirationAt;
    }

    public CouponEntity(Coupon activeCoupon) {
        this.code = activeCoupon.getCode();
        this.discountValue = activeCoupon.getDiscountValue();
        this.product = activeCoupon.getProduct();
        this.status = activeCoupon.getStatus();
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }

    public Status getStatus() {
        return status;
    }

    public Coupon invalidate() {
        this.status = Status.INACTIVE;
        return this.toCoupon();
    }

    public Coupon toCoupon() {
        return new Coupon(code, discountValue, product);
    }
}