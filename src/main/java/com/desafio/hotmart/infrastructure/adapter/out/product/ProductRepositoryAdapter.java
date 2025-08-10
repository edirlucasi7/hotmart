package com.desafio.hotmart.infrastructure.adapter.out.product;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.product.ProductOffer;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.application.port.product.ProductRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.PageDTO;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductOfferEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.repository.ProductEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductEntityRepository productEntityRepository;

    public ProductRepositoryAdapter(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Override
    public Optional<Product> findByCode(String productCode) {
        return productEntityRepository.findByCode(productCode).map(ProductEntity::toProduct);
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = new ProductEntity(product);
        return productEntityRepository.save(productEntity).toProduct();
    }

    @Override
    public PagePort<Product> findAllByActiveTrue(int page, int size) {
        Page<Product> activeProducts = productEntityRepository.findAllByActiveTrue(PageRequest.of(page, size)).map(ProductEntity::toProduct);
        return new PageDTO<>(activeProducts);
    }

    @Override
    public void addOffer(String productCode, ProductOffer productOffer) {
        ProductEntity productEntity = productEntityRepository.findByCode(productCode).orElseThrow(IllegalArgumentException::new);
        productEntity.addOffer(new ProductOfferEntity(productOffer));
        productEntityRepository.save(productEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productEntityRepository.findById(id).map(ProductEntity::toProduct);
    }

    @Override
    public Optional<Product> findByIdAndActiveTrue(String code) {
        return productEntityRepository.findByCodeAndActiveIsTrue(code).map(ProductEntity::toProduct);
    }
}
