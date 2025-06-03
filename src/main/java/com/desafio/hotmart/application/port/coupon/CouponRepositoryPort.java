package com.desafio.hotmart.application.port.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.product.Product;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponRepositoryPort {

    Optional<CouponEntity> findCouponByCodeAndProduct(String coupon, Long productId);

    Coupon invalidate(String code, BigDecimal discountValue, Product product);

    Coupon save(Coupon coupon);
}
