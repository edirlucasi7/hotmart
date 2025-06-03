package com.desafio.hotmart.infrastructure.adapter.in.coupon;

import com.desafio.hotmart.product.Product;

import java.math.BigDecimal;

public record CouponView(String code, BigDecimal discountValue, Product product) {

    public static CouponView of(String code, BigDecimal discountValue, Product product) {
        return new CouponView(code, discountValue, product);
    }
}
