package com.desafio.hotmart.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query(value = """
        SELECT COALESCE(
           (SELECT 'true'
            FROM purchase pu
                JOIN product pr ON pr.id = pu.product_id
            WHERE pu.user_id = :userId
                AND pr.code = :productCode
                AND pu.state = 'PROCESSED'
                AND pu.expiration_at >= NOW()), 'false') AS hasPurchaseProcesses
    """, nativeQuery = true)
    boolean hasValidPurchaseProcessedBy(Long userId, String productCode);
}