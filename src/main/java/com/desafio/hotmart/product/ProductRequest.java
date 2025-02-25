package com.desafio.hotmart.product;

import com.desafio.hotmart.Offer;
import com.desafio.hotmart.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(@NotBlank String email,
                             @NotNull String code,
                             @NotNull int confirmationTimeToPix,
                             @NotNull @JsonProperty("offer") OfferRequest offerRequest) {

    public Product toProduct(User user) {
        Offer offer = offerRequest.toOffer();
        return new Product(user, code, confirmationTimeToPix, offer);
    }
}