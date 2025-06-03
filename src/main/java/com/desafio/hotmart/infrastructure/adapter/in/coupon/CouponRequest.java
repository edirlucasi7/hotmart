package com.desafio.hotmart.infrastructure.adapter.in.coupon;

import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.product.Product;
import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(String code, String productCode, BigDecimal discountValue,
                            @Future(message = "A data de expiração deve ser futura") LocalDateTime expirationAt) {

    public CouponEntity toCoupon(Product product) {
        return new CouponEntity(code, discountValue, product, expirationAt);
    }
}
