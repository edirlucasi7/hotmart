package com.desafio.hotmart.coupon;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {


    @Query(value = """
        SELECT co.*
        FROM coupon co
            JOIN product po ON po.id = co.product_id
        WHERE co.code = :coupon
            AND po.id = :productId
            AND co.created_at <= NOW() AND co.expiration_at >= NOW()
            AND co.status = 'ACTIVE'
    """, nativeQuery = true)
    Optional<Coupon> findCouponByCodeAndProduct(String coupon, Long productId);

    // TODO rever como vai ser a expiração e se esse lock faz sentido
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Coupon> findByCodeAndProduct_IdAndStatus(String code,  Long productId, Status status);

    default Optional<Coupon> findCouponByCodeAndActiveStatus(String code, Long productId) {
        return findByCodeAndProduct_IdAndStatus(code, productId, Status.ACTIVE);
    }
}