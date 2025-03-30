package com.desafio.hotmart.purchase;

public class PurchaseEventResultBody {

    private final String errorMessage;

    public PurchaseEventResultBody(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
