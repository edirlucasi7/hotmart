package com.desafio.hotmart.product;

import com.desafio.hotmart.user.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(@NotBlank String email,
                             @NotNull BigDecimal price,
                             @NotNull String code,
                             @Min(value = 1) @Max(value = 12) int maximumNumberOfInstallments,
                             @NotNull boolean interestPaidByCustomer) {

    public Product toProduct(User user) {
        return new Product(user, price, code, maximumNumberOfInstallments, interestPaidByCustomer);
    }
}