package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

import static com.desafio.hotmart.purchase.PurchaseType.CREDIT_CARD;

// TODO da pra retornar somente um erro por vez, usar chain of responsibility com ordem parece bom
@RequestScope
@Component
public class PurchaseValidator {

    private final PurchaseRepository purchaseRepository;
    private final List<String> errors = new ArrayList<>();

    public PurchaseValidator(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public boolean isValid(Product product, User client, PurchaseRequest request, boolean smartPayment) {
        validatesIfTheClientAlreadyHasTheProduct(client.getId(), product.getCode());
        validatesNumberOfInstallmentsToCreditCard(request.numberOfInstallments(), request.type(), product.getMaximumNumberOfInstallmentsFromActiveOffer());
        validatesIfTheNumberOfInstallmentsIsEqualToTheMaximumAllowed(product, smartPayment, request.numberOfInstallments());

        return errors.isEmpty();
    }

    private void validatesIfTheClientAlreadyHasTheProduct(Long userId, String productCode) {
        boolean customerAlreadyHasTheProduct = purchaseRepository.hasValidPurchaseProcessedBy(userId, productCode);
        if (customerAlreadyHasTheProduct) {
            errors.add("The client already has the valid product with code: %s".formatted(productCode));
        }
    }

    private void validatesIfTheNumberOfInstallmentsIsEqualToTheMaximumAllowed(Product product, boolean smartPayment, int numberOfInstallments) {
        if (smartPayment && (numberOfInstallments != product.getMaximumNumberOfInstallmentsFromActiveOffer())) {
            errors.add("The number of installments of a smart payment must be the maximum allowed by product: %s".formatted(product.getCode()));
        }
    }

    private void validatesNumberOfInstallmentsToCreditCard(int numberOfInstallments, String type, int maximumNumberOfInstallments) {
        if (CREDIT_CARD.equals(PurchaseType.getByName(type)) && !hasValidNumberOfInstallmentsToCreditCard(numberOfInstallments, maximumNumberOfInstallments)) {
            errors.add("The number of installments is grater than that allowed by the product");
        }
    }

    private boolean hasValidNumberOfInstallmentsToCreditCard(int numberOfInstallments, int maximumNumberOfInstallments) {
        return numberOfInstallments > 0 && (numberOfInstallments <= maximumNumberOfInstallments);
    }

    public List<String> getErrors() {
        return errors;
    }
}
