package com.desafio.hotmart.purchase.errors;

import java.util.List;

public class ProductEventResultBody {

    private final List<String> errors;

    public ProductEventResultBody(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}