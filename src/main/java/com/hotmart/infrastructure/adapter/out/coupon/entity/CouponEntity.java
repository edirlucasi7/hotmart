package com.hotmart.infrastructure.adapter.out.coupon.entity;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.coupon.validator.Status;
import com.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
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

    public CouponEntity(Coupon coupon, ProductEntity productEntity) {
        this.code = coupon.getCode();
        this.discountValue = coupon.getDiscountValue();
        this.productEntity = productEntity;
        this.expirationAt = coupon.getExpirationAt();
        this.status = coupon.getStatus();
    }

    public CouponEntity(Coupon coupon) {
        this.id = coupon.getId();
        this.code = coupon.getCode();
        this.discountValue = coupon.getDiscountValue();
        this.productEntity = new ProductEntity(coupon.getProduct());
        this.expirationAt = coupon.getExpirationAt();
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

    public void invalidateActiveCoupon() {
        this.status = Status.INACTIVE;
    }

    public Coupon toCoupon() {
        return new Coupon(id, code, discountValue, expirationAt, productEntity.toProduct());
    }
}