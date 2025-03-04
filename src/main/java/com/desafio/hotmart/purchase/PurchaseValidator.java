package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

import static com.desafio.hotmart.purchase.PurchaseType.CREDIT_CARD;

@RequestScope
@Component
public class PurchaseValidator {

    private final PurchaseRepository purchaseRepository;
    private final List<String> errors = new ArrayList<>();

    public PurchaseValidator(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public boolean isValid(Product product, User client, PurchaseRequest request) {
        validatesIfTheClientAlreadyHasTheProduct(client.getId(), product.getCode());
        validatesNumberOfInstallmentsToCreditCard(request.numberOfInstallments(), request.type(), product.getMaximumNumberOfInstallmentsFromActiveOffer());

        return errors.isEmpty();
    }

    private void validatesIfTheClientAlreadyHasTheProduct(Long userId, String productCode) {
        boolean customerAlreadyHasTheProduct = purchaseRepository.hasValidPurchaseProcessedBy(userId, productCode);
        if (customerAlreadyHasTheProduct) {
            errors.add("The client already has the valid product with code: %s".formatted(productCode));
        }
    }

    private void validatesNumberOfInstallmentsToCreditCard(int numberOfInstallments, String type, int maximumNumberOfInstallments) {
        if (CREDIT_CARD.equals(PurchaseType.getByName(type)) && !hasValidNumberOfInstallmentsToCreditCard(numberOfInstallments, maximumNumberOfInstallments)) {
            errors.add("The number of installments cannot be grater than that allowed by the product");
        }
    }

    private boolean hasValidNumberOfInstallmentsToCreditCard(int numberOfInstallments, int maximumNumberOfInstallments) {
        return numberOfInstallments > 0 && (numberOfInstallments <= maximumNumberOfInstallments);
    }

    public List<String> getErrors() {
        return errors;
    }
}
