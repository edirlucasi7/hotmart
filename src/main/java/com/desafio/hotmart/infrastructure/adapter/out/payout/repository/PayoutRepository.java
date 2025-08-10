package com.desafio.hotmart.infrastructure.adapter.out.payout.repository;

import com.desafio.hotmart.infrastructure.adapter.out.payout.entity.PayoutEntity;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PayoutRepository extends JpaRepository<PayoutEntity, Long> {

    Optional<PayoutEntity> findByPurchase(Purchase purchase);

    @Modifying
    @Query(value = """
        UPDATE payout SET status = 'CONFIRMED', updated_at = :now  WHERE purchase_id = :purchaseId
    """, nativeQuery = true)
    void updateStatusForConfirmed(Long purchaseId, LocalDateTime now);

    default void updateStatusForConfirmedAndUpdatedAt(Long purchaseId) {
        updateStatusForConfirmed(purchaseId, LocalDateTime.now());
    }
}
