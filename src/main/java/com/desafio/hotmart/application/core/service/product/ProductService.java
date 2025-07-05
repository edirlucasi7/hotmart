package com.desafio.hotmart.application.core.service.product;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.product.ProductOffer;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.application.port.product.ProductRepositoryPort;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.infrastructure.adapter.in.product.ProductServicePort;

import java.util.Optional;

public class ProductService implements ProductServicePort {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Optional<Product> findByCode(String productCode) {
        return productRepositoryPort.findByCode(productCode);
    }

    @Override
    public Product save(Product product) {
        return productRepositoryPort.save(product);
    }

    @Override
    public PagePort<Product> findAllByActiveTrue(int page, int size) {
        return productRepositoryPort.findAllByActiveTrue(page, size);
    }

    @Override
    public void addOffer(String productCode, ProductOffer request) throws ProductNotFoundException {
        productRepositoryPort.addOffer(productCode, request);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepositoryPort.findById(id);
    }
}
