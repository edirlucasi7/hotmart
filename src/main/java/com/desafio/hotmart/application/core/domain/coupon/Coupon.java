package com.desafio.hotmart.application.core.domain.coupon;

import com.desafio.hotmart.coupon.Status;
import com.desafio.hotmart.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Coupon {

    private Long id;

    private String code;

    private BigDecimal discountValue;

    private Product product;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expirationAt;

    private Status status = Status.ACTIVE;

    public Coupon(String code, BigDecimal discountValue, Product product) {
        this.code = code;
        this.discountValue = discountValue;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public Product getProduct() {
        return product;
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

    public Coupon toCoupon() {
        return new Coupon(code, discountValue, product);
    }
}
