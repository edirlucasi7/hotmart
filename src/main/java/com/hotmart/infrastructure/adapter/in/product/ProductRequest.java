package com.hotmart.infrastructure.adapter.in.product;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.product.ProductOffer;
import com.hotmart.application.core.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(@NotBlank @Email String email,
                             @NotNull String code,
                             @NotNull @JsonProperty("offer") OfferRequest offerRequest) {

    public Product toProduct(User user) {
        ProductOffer productOfferEntity = offerRequest.toOffer();
        return new Product(user, code, productOfferEntity);
    }
}