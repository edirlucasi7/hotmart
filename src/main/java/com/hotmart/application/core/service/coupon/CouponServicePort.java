package com.hotmart.application.core.service.coupon;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponServicePort {

    Optional<Coupon> tryGetDiscount(String coupon, String productCode);

    void invalidate(String code, Product product);

    Coupon save(String code, BigDecimal discountValue, LocalDateTime expirationAt, Product product);
}
