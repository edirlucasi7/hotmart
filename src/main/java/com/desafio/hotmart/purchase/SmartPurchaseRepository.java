package com.desafio.hotmart.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmartPurchaseRepository extends JpaRepository<SmartPurchase, Long> {
}