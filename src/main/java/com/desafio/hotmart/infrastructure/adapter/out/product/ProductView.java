package com.desafio.hotmart.infrastructure.adapter.out.product;

import com.desafio.hotmart.application.core.domain.product.Product;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record ProductView(String userName, String code, BigDecimal fee, Set<ProductOfferView> offers) {

    public static ProductView from(Product product) {
        return new ProductView(product.getUsername(),
                product.getCode(),
                product.getFee(),
                product.getOffers().stream().map(ProductOfferView::from).collect(Collectors.toSet()));
    }
}
