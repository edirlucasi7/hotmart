package com.desafio.hotmart.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query(value = """
        SELECT COALESCE(
           (SELECT 'true'
            FROM purchase pu
                JOIN product pr ON pr.id = pu.product_id
            WHERE pu.user_id = :userId
                AND pr.code = :productCode
                AND pu.state = 'PROCESSED'
                AND pu.expiration_at >= :now), 'false') AS hasPurchaseProcesses
    """, nativeQuery = true)
    boolean hasValidPurchaseProcessedBy(Long userId, String productCode, LocalDateTime now);

    default boolean hasValidPurchaseAssociatedWith(Long userId, String productCode, LocalDateTime now) {
        return hasValidPurchaseProcessedBy(userId, productCode, LocalDateTime.now());
    }
}