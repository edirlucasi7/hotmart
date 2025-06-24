package com.desafio.hotmart.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(String producer, String code, BigDecimal price, int maximumNumberOfInstallments) {

    public static List<ProductResponseDTO> convert(List<ProductEntity> productEntities) {
        return productEntities
                .stream()
                .map(product -> new ProductResponseDTO(product.getUserEmail(),
                                product.getCode(), product.getPriceFromActiveOffer(),
                                product.getMaximumNumberOfInstallmentsFromActiveOffer()))
                .toList();
    }
}
