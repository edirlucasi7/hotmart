package com.hotmart.infrastructure.adapter.out.purchase.repository;

import com.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    @Query(value = """
        SELECT COALESCE(
           (SELECT 'true'
            FROM purchase pu
                JOIN product pr ON pr.id = pu.product_id
            WHERE pu.user_id = :userId
                AND pr.code = :productCode
                AND pu.status NOT IN ('REIMBURSED', 'EXPIRED', 'SUSPENDED')
                AND pu.expiration_at >= :now), 'false') AS hasPurchaseProcesses
    """, nativeQuery = true)
    boolean hasValidPurchaseProcessedBy(Long userId, String productCode, LocalDateTime now);

    default boolean hasValidPurchaseAssociatedWith(Long userId, String productCode) {
        return hasValidPurchaseProcessedBy(userId, productCode, LocalDateTime.now());
    }
}