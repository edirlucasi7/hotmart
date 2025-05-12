package com.desafio.hotmart.product;

import com.desafio.hotmart.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(@NotBlank @Email String email,
                             @NotNull String code,
                             @NotNull @JsonProperty("offer") OfferRequest offerRequest) {

    public Product toProduct(User user) {
        Offer offer = offerRequest.toOffer();
        return new Product(user, code, offer);
    }
}