package com.desafio.hotmart.infrastructure.adapter.out.coupon.repository;

import com.desafio.hotmart.application.core.domain.coupon.validator.Status;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponEntityRepository extends JpaRepository<CouponEntity, Long> {

    @Query(value = """
        SELECT co.*
        FROM coupon co
            JOIN product po ON po.id = co.product
        WHERE co.code = :couponCode
            AND po.id = :productId
            AND co.created_at <= NOW() AND co.expiration_at >= NOW()
            AND co.status = 'ACTIVE'
    """, nativeQuery = true)
    Optional<CouponEntity> findCouponByCodeAndProduct(String couponCode, Long productId);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponEntity> findByCodeAndProductEntity_IdAndStatus(String code, Long productId, Status status);

    default Optional<CouponEntity> findByCodeAndProductEntity_IdAndActiveStatus(String code, Long productId) {
        return findByCodeAndProductEntity_IdAndStatus(code, productId, Status.ACTIVE);
    }

    Optional<CouponEntity> findByCode(String code);
}