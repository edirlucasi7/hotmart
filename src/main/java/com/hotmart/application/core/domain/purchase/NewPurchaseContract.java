package com.hotmart.application.core.domain.purchase;

public interface NewPurchaseContract {

    String getProductCode();
    String getEmail();
    String getType();
    Integer getInstallments();
    boolean isSmartPayment();

    default String getUpperCaseType() {
        return getType().toUpperCase();
    }
}
