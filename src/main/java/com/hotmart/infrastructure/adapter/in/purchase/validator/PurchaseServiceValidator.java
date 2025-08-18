package com.hotmart.infrastructure.adapter.in.purchase.validator;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceValidator {

    private final List<PurchaseValidator> validators;

    public PurchaseServiceValidator(List<PurchaseValidator> validators) {
        this.validators = validators;
    }

    public void process(PurchaseValidationResponse purchaseValidationResponse) {
        validators.stream()
                .filter(validator ->
                        validator.shouldValidate(
                                purchaseValidationResponse.purchaseType(),
                                purchaseValidationResponse.isSmartPayment()))
                .forEach(validator ->
                        validator.isValid(
                                purchaseValidationResponse.client(),
                                purchaseValidationResponse.product(),
                                purchaseValidationResponse.installments()));
    }
}