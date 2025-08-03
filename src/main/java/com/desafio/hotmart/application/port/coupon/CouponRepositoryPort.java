package com.desafio.hotmart.application.port.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponRepositoryPort {

    Optional<Coupon> findCouponByCodeAndProduct(String coupon, Long productId);

    Coupon invalidate(String code, BigDecimal discountValue, Product product);

    Coupon save(Coupon coupon, Product product);
}
