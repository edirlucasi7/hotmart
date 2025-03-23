package com.desafio.hotmart.coupon;

public class CouponValidator {

    public static boolean isValid(String coupon) {
        return coupon != null && !coupon.isBlank();
    }
}