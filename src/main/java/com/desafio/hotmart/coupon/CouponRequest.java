package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(String code, String productCode, BigDecimal discountValue, LocalDateTime expirationAt) {

    public Coupon toCoupon(Product product) {
        return new Coupon(code, discountValue, product, expirationAt);
    }
}
