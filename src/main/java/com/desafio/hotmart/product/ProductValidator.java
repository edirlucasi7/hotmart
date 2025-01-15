package com.desafio.hotmart.product;

import com.desafio.hotmart.purchase.PurchaseRepository;
import com.desafio.hotmart.purchase.PurchaseRequest;
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
        validateConfirmationTime(request);
        validatesNumberOfInstallments(request.numberOfInstallments(), product.getMaximumNumberOfInstallments());
        validatesIfTheClientAlreadyHasTheProduct(client.getId(), product.getCode());

        return errors.isEmpty();
    }

    private void validateConfirmationTime(PurchaseRequest request) {
        if (!"PIX".equals(request.type()) && request.confirmationTime() != 0) {
            errors.add("Purchase via %s: cannot have confirmationTime different from 0".formatted(request.type()));
            return;
        }

        if ("PIX".equals(request.type()) && request.confirmationTime() <= 0) {
            errors.add("Confirmation time must be grater than zero");
        }
    }

    private void validatesNumberOfInstallments(int installmentsPassedByClient, int maximumInstallmentsFromProduct) {
        if (installmentsPassedByClient > maximumInstallmentsFromProduct) {
            errors.add("Installments passed by client: %s is greater than allowed: %s".formatted(installmentsPassedByClient, maximumInstallmentsFromProduct));
        }
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
