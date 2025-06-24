package com.desafio.hotmart.infrastructure.adapter.out.coupon.entity;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.coupon.validator.Status;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
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
    @JoinColumn(name = "product")
    private ProductEntity productEntity;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expirationAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Deprecated
    public CouponEntity() { }

    public CouponEntity(String code, BigDecimal discountValue, ProductEntity productEntity, LocalDateTime expirationAt) {
        this.code = code;
        this.discountValue = discountValue;
        this.productEntity = productEntity;
        this.expirationAt = expirationAt;
    }

    public CouponEntity(Coupon coupon, ProductEntity productEntity) {
        this.code = coupon.getCode();
        this.discountValue = coupon.getDiscountValue();
        this.productEntity = productEntity;
        this.status = coupon.getStatus();
    }

    public ProductEntity getProduct() {
        return productEntity;
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
        return new Coupon(code, discountValue, productEntity.toProduct());
    }
}