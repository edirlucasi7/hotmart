package com.desafio.hotmart.infrastructure.adapter.out.coupon.repository;

import com.desafio.hotmart.coupon.Status;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponEntityRepository extends JpaRepository<CouponEntity, Long> {

    @Query(value = """
        SELECT co.*
        FROM couponEntity co
            JOIN product po ON po.id = co.product_id
        WHERE co.code = :couponEntity
            AND po.id = :productId
            AND co.created_at <= NOW() AND co.expiration_at >= NOW()
            AND co.status = 'ACTIVE'
    """, nativeQuery = true)
    Optional<CouponEntity> findCouponByCodeAndProduct(String couponEntity, Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CouponEntity> findByCodeAndProduct_IdAndStatus(String code, Long productId, Status status);

    default Optional<CouponEntity> findCouponByCodeAndProductWithActiveStatus(String code, Long productId) {
        return findByCodeAndProduct_IdAndStatus(code, productId, Status.ACTIVE);
    }

    Optional<CouponEntity> findByCode(String code);
}