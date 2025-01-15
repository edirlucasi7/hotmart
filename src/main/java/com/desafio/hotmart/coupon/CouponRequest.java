package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;
import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(String code, String productCode, BigDecimal discountValue,
                            @Future(message = "A data de expiração deve ser futura") LocalDateTime expirationAt) {

    public Coupon toCoupon(Product product) {
        return new Coupon(code, discountValue, product, expirationAt);
    }
}
