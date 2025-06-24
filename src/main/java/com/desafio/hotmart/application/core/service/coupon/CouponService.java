package com.desafio.hotmart.application.core.service.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class CouponService implements CouponServicePort {

    private final CouponRepositoryPort couponRepositoryPort;

    public CouponService(CouponRepositoryPort couponRepositoryPort) {
        this.couponRepositoryPort = couponRepositoryPort;
    }

    @Override
    public Optional<BigDecimal> tryGetDiscount(String coupon, ProductEntity productEntity) {
        return couponRepositoryPort.findCouponByCodeAndProduct(coupon, productEntity.getId()).map(CouponEntity::getDiscountValue);
    }

    @Override
    public Coupon invalidate(String code, BigDecimal discountValue, Product product) {
        return couponRepositoryPort.invalidate(code, discountValue, product);
    }

    @Override
    public Coupon save(Coupon coupon, Product product) {
        return couponRepositoryPort.save(coupon, product);
    }
}
