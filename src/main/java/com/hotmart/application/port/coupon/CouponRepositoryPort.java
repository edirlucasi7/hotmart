package com.hotmart.application.port.coupon;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.product.Product;

import java.util.Optional;

public interface CouponRepositoryPort {

    Optional<Coupon> findCouponByCodeAndProduct(String coupon, Long productId);

    void invalidate(String code, Product product);

    Coupon save(Coupon coupon);
}
