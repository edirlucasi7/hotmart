package com.desafio.hotmart.application.core.domain.purchase;

public interface NewPurchaseContract {

    String getProductCode();
    String getEmail();
    String getType();
    Integer getInstallments();

    default String getUpperCaseType() {
        return getType().toUpperCase();
    }
}
