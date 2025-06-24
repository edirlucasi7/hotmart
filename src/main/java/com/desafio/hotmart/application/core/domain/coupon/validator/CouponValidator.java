package com.desafio.hotmart.application.core.domain.coupon.validator;

public class CouponValidator {

    public static boolean isValid(String coupon) {
        return coupon != null && !coupon.isBlank();
    }
}