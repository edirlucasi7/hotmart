package com.desafio.hotmart.infrastructure.adapter.out.product;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.application.port.product.ProductRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.PageDTO;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductOfferEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.repository.ProductEntityRepository;
import com.desafio.hotmart.product.OfferRequestDTO;
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
    public void addOffer(String productCode, OfferRequestDTO request) throws ProductNotFoundException {
        ProductEntity productEntity = productEntityRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(productCode));

        ProductOfferEntity productOfferEntity = request.toOffer();
        productEntity.addOffer(productOfferEntity);

        productEntityRepository.save(productEntity);
    }
}
