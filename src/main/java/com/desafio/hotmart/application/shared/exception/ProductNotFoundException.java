package com.desafio.hotmart.application.shared.exception;

public class ProductNotFoundException extends Exception {

    private final String productCode;

    public ProductNotFoundException(String productCode) {
        super("Product not found with code: " + productCode);
        this.productCode = productCode;
    }

    public String getProductCode() {
        return productCode;
    }
}
