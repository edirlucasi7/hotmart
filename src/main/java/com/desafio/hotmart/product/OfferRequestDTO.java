package com.desafio.hotmart.product;

import com.desafio.hotmart.Offer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OfferRequestDTO(@Min(value = 1) @Max(value = 12) int maximumNumberOfInstallments,
                              @Min(value = 0) BigDecimal price,
                              boolean smartPayment,
                              @NotBlank String interestPayer) {

    public Offer toOffer() {
        return new Offer(maximumNumberOfInstallments, price, smartPayment, interestPayer);
    }
}
