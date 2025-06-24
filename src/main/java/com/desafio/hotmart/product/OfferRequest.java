package com.desafio.hotmart.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductOfferEntity;

import java.math.BigDecimal;

public record OfferRequest(int maximumNumberOfInstallments, BigDecimal price, boolean smartPayment, String interestPayer) {

    public ProductOfferEntity toOffer() {
        return new ProductOfferEntity(maximumNumberOfInstallments, price, smartPayment, interestPayer);
    }
}