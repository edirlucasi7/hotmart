package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.purchase.PurchaseType;
import com.desafio.hotmart.user.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.desafio.hotmart.purchase.PurchaseType.CREDIT_CARD;

@Order(1)
@Component
public class NumberOfInstallmentsToCreditCard implements PurchaseValidatorRules {

    @Override
    public boolean isValid(Product product, User client, PurchaseRequest request, boolean smartPayment) {
        return CREDIT_CARD.equals(PurchaseType.getByName(request.type()))
                && hasValidNumberOfInstallmentsToCreditCard(request.numberOfInstallments(), product.getMaximumNumberOfInstallmentsFromActiveOffer());
    }

    @Override
    public ResultErrorResponse getErrorResponse() {
        return new ResultErrorResponse("", "The number of installments is grater than that allowed by the product");
    }

    private boolean hasValidNumberOfInstallmentsToCreditCard(int numberOfInstallments, int maximumNumberOfInstallments) {
        return numberOfInstallments > 0 && (numberOfInstallments <= maximumNumberOfInstallments);
    }
}