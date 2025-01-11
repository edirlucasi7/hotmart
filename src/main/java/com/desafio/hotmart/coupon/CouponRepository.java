package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
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
    """, nativeQuery = true)
    Optional<Coupon> findCouponByCodeAndProduct(String coupon, Long productId);
}
