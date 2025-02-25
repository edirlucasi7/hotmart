package com.desafio.hotmart.product;

import com.desafio.hotmart.purchase.PurchaseRepository;
import com.desafio.hotmart.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@RequestScope
@Component
public class ProductValidator {

    private final PurchaseRepository purchaseRepository;
    private final List<String> errors = new ArrayList<>();

    public ProductValidator(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public boolean isValid(Product product, User client) {
        validatesIfTheClientAlreadyHasTheProduct(client.getId(), product.getCode());

        return errors.isEmpty();
    }

    private void validatesIfTheClientAlreadyHasTheProduct(Long userId, String productCode) {
        boolean customerAlreadyHasTheProduct = purchaseRepository.hasValidPurchaseProcessedBy(userId, productCode);
        if (customerAlreadyHasTheProduct) {
            errors.add("The client already has the valid product with code: %s".formatted(productCode));
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}
