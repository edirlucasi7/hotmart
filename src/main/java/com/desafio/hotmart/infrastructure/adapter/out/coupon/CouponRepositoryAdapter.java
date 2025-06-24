package com.desafio.hotmart.infrastructure.adapter.out.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.repository.CouponEntityRepository;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<CouponEntity> possibleCouponEntity = couponEntityRepository.findByCodeAndProductEntity_IdAndStatus(code, product.getId());
        if (possibleCouponEntity.isEmpty()) return new Coupon(code, discountValue, product);

        return possibleCouponEntity.get().invalidate();
    }

    @Override
    @Transactional
    public Coupon save(Coupon coupon, Product product) {
        ProductEntity productEntity = new ProductEntity(product);
        return couponEntityRepository.save(new CouponEntity(coupon, productEntity)).toCoupon();
    }
}
