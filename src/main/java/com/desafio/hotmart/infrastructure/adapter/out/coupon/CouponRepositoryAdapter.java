package com.desafio.hotmart.infrastructure.adapter.out.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.repository.CouponEntityRepository;
import com.desafio.hotmart.product.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CouponRepositoryAdapter implements CouponRepositoryPort {

    private final CouponEntityRepository couponEntityRepository;

    public CouponRepositoryAdapter(CouponEntityRepository couponEntityRepository) {
        this.couponEntityRepository = couponEntityRepository;
    }

    @Override
    public Optional<CouponEntity> findCouponByCodeAndProduct(String coupon, Long productId) {
        return couponEntityRepository.findCouponByCodeAndProduct(coupon, productId);
    }

    @Override
    public Coupon invalidate(String code, BigDecimal discountValue, Product product) {
        Optional<CouponEntity> possibleCouponEntity = couponEntityRepository.findCouponByCodeAndProductWithActiveStatus(code, product.getId());
        if (possibleCouponEntity.isEmpty()) return new Coupon(code, discountValue, product);

        return possibleCouponEntity.get().invalidate();
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponEntityRepository.save(new CouponEntity(coupon)).toCoupon();
    }
}
