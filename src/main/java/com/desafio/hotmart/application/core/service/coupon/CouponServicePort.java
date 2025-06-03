package com.desafio.hotmart.application.core.service.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.product.Product;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponServicePort {

    Optional<BigDecimal> tryGetDiscount(String coupon, Product product);

    Coupon invalidate(String code, BigDecimal discountValue, Product product);

    Coupon save(Coupon coupon);
}
