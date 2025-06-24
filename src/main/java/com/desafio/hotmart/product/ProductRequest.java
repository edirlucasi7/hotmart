package com.desafio.hotmart.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductOfferEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(@NotBlank @Email String email,
                             @NotNull String code,
                             @NotNull @JsonProperty("offer") OfferRequest offerRequest) {

    public ProductEntity toProduct(User user) {
        ProductOfferEntity productOfferEntity = offerRequest.toOffer();
        return new ProductEntity(user, code, productOfferEntity);
    }
}