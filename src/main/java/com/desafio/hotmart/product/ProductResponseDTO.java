package com.desafio.hotmart.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(String producer, String code, BigDecimal price, int maximumNumberOfInstallments) {

    public static List<ProductResponseDTO> convert(List<Product> products) {
        return products
                .stream()
                .map(product -> new ProductResponseDTO(product.getUserEmail(),
                                product.getCode(), product.getPriceFromActiveOffer(),
                                product.getMaximumNumberOfInstallmentsFromActiveOffer()))
                .toList();
    }
}
