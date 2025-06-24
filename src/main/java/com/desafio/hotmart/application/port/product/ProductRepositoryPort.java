package com.desafio.hotmart.application.port.product;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.product.OfferRequestDTO;

import java.util.Optional;

public interface ProductRepositoryPort {
    Optional<Product> findByCode(String productCode);

    Product save(Product product);

    PagePort<Product> findAllByActiveTrue(int page, int size);

    void addOffer(String productCode, OfferRequestDTO request) throws ProductNotFoundException;
}
