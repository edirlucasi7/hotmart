package com.hotmart.infrastructure.adapter.out.coupon;

import com.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;

import java.math.BigDecimal;

public record CouponView(String code, BigDecimal discountValue, ProductEntity productEntity) {

    public static CouponView of(String code, BigDecimal discountValue, ProductEntity productEntity) {
        return new CouponView(code, discountValue, productEntity);
    }
}
