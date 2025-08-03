package com.desafio.hotmart.infrastructure.adapter.in.purchase;

import com.desafio.hotmart.application.core.domain.purchase.NewPurchaseContract;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(@NotBlank String productCode,
                              @NotBlank String email,
                              @NotBlank String type,
                              @NotNull Integer installments) implements NewPurchaseContract {

    @Override
    public String getProductCode() {
        return productCode;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Integer getInstallments() {
        return installments;
    }
}