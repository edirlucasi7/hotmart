package com.hotmart.infrastructure.adapter.out.coupon;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.port.coupon.CouponRepositoryPort;
import com.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.hotmart.infrastructure.adapter.out.coupon.repository.CouponEntityRepository;
import com.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class CouponRepositoryAdapter implements CouponRepositoryPort {

    private final CouponEntityRepository couponEntityRepository;

    public CouponRepositoryAdapter(CouponEntityRepository couponEntityRepository) {
        this.couponEntityRepository = couponEntityRepository;
    }

    @Override
    public Optional<Coupon> findCouponByCodeAndProduct(String coupon, Long productId) {
        return couponEntityRepository.findCouponByCodeAndProduct(coupon, productId).map(CouponEntity::toCoupon);
    }

    @Override
    public void invalidate(String code, Product product) {
        Optional<CouponEntity> possibleCouponEntity = couponEntityRepository.findByCodeAndProductEntity_IdAndActiveStatus(code, product.getId());
        if (possibleCouponEntity.isEmpty()) return;

        possibleCouponEntity.get().invalidateActiveCoupon();
    }

    @Override
    @Transactional
    public Coupon save(Coupon coupon) {
        ProductEntity productEntity = new ProductEntity(coupon.getProduct());
        return couponEntityRepository.save(new CouponEntity(coupon, productEntity)).toCoupon();
    }
}
