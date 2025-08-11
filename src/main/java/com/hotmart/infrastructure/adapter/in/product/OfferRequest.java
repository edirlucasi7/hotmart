package com.hotmart.infrastructure.adapter.in.product;

import com.hotmart.application.core.domain.product.ProductOffer;
import com.hotmart.infrastructure.adapter.out.product.entity.InterestPayer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OfferRequest(@Min(value = 1) @Max(value = 12) int maximumNumberOfInstallments,
                           @Min(value = 0) BigDecimal price,
                           boolean smartPayment,
                           @NotBlank String interestPayer) {

    public ProductOffer toOffer() {
        return new ProductOffer(maximumNumberOfInstallments, price, smartPayment, InterestPayer.fromName(interestPayer));
    }
}