package com.desafio.hotmart.product;

import java.math.BigDecimal;

public record OfferRequest(int maximumNumberOfInstallments, BigDecimal price, boolean smartPayment, String interestPayer) {

    public Offer toOffer() {
        return new Offer(maximumNumberOfInstallments, price, smartPayment, interestPayer);
    }
}