package com.desafio.hotmart.purchase;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsByUserIdAndProductCode(Long userId, String productCode);
}
