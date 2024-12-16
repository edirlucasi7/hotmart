package com.desafio.hotmart.product;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductRequestValidator implements Validator {

    private final ProductRepository productRepository;

    public ProductRequestValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProductRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductRequest productRequest = (ProductRequest) target;
        boolean existsByCode = productRepository.existsByCode(productRequest.code());
        if (existsByCode) errors.rejectValue("code", "Product code already exists");
    }
}
