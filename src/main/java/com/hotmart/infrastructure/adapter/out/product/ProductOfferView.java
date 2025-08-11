package com.hotmart.infrastructure.adapter.out.product;

import com.hotmart.application.core.domain.product.ProductOffer;

import java.math.BigDecimal;

public record ProductOfferView(int maximumNumberOfInstallments, boolean hasSmartPayment, BigDecimal price, String interestPayer) {

    public static ProductOfferView from(ProductOffer productOffer) {
        return new ProductOfferView(productOffer.getMaximumNumberOfInstallments(),
                productOffer.isSmartPayment(),
                productOffer.getPrice(),
                productOffer.getInterestPayer().name());
    }
}
