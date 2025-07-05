package com.desafio.hotmart.infrastructure.adapter.in.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.repository.ProductEntityRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductRequestValidator implements Validator {

    private final ProductEntityRepository productEntityRepository;

    public ProductRequestValidator(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return clazz.isAssignableFrom(ProductRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ProductRequest productRequest = (ProductRequest) target;
        boolean existsByCode = productEntityRepository.existsByCode(productRequest.code());
        if (existsByCode) errors.rejectValue("code", "code.already.exists", "Product code already exists");
    }
}
