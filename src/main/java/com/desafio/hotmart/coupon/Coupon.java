package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Coupon {

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

    @Deprecated
    public Coupon() { }

    public Coupon(String code, BigDecimal discountValue, Product product, LocalDateTime expirationAt) {
        this.code = code;
        this.discountValue = discountValue;
        this.product = product;
        this.expirationAt = expirationAt;
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
}