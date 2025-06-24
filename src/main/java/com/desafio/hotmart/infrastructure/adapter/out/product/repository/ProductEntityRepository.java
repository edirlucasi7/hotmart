package com.desafio.hotmart.infrastructure.adapter.out.product.repository;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductEntityRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByCode(String code);
    Optional<ProductEntity> findByCode(String code);
    Page<ProductEntity> findAllByActiveTrue(Pageable pageable);
    Optional<ProductEntity> findByCodeAndActiveIsTrue(String code);
}