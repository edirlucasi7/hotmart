package com.desafio.hotmart.purchase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PixPurchaseRepository extends JpaRepository<PixPurchase, Long> {

    Optional<PixPurchase> findByCodeToPay(String code);
}
