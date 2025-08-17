package com.hotmart.infrastructure.adapter.in.product;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.product.ProductOffer;
import com.hotmart.application.port.PagePort;

import java.util.Optional;

public interface ProductServicePort {

    Optional<Product> findByCode(String productCode);

    Product save(Product product);

    PagePort<Product> findAllByActiveTrue(int page, int size);

    void addOffer(String productCode, ProductOffer offer);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdAndActiveTrue(String code);
}
