package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
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

    public boolean isValid(PurchaseRequest request, Product product, User client) {
        validatesNumberOfInstallments(request.numberOfInstallments(), product.getMaximumNumberOfInstallments());
        validatesIfTheClientAlreadyHasTheProduct(client.getId(), product.getCode());

        return errors.isEmpty();
    }

    private void validatesNumberOfInstallments(int installmentsPassedByClient, int maximumInstallmentsFromProduct) {
        if (installmentsPassedByClient > maximumInstallmentsFromProduct) {
            errors.add("Installments passed by client: %s is greater than allowed: %s".formatted(installmentsPassedByClient, maximumInstallmentsFromProduct));
        }
    }

    private void validatesIfTheClientAlreadyHasTheProduct(Long userId, String productCode) {
        boolean customerAlreadyHasTheProduct = purchaseRepository.existsByUserIdAndProductCode(userId, productCode);
        if (customerAlreadyHasTheProduct) {
            errors.add("The client already has the product with code: %s".formatted(productCode));
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}
