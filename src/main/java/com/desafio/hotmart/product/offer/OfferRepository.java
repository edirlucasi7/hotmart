package com.desafio.hotmart.product.offer;

import com.desafio.hotmart.product.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}
