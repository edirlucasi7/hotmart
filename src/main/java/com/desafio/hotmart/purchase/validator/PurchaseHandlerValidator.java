package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PurchaseHandlerValidator {

    private final List<PurchaseValidatorRules> purchaseValidatorRules;

    public PurchaseHandlerValidator(List<PurchaseValidatorRules> purchaseValidatorRules) {
        this.purchaseValidatorRules = purchaseValidatorRules;
    }

    public Optional<ResultErrorResponse> handler(Product product, User client, PurchaseRequest request, boolean smartPayment) {
        return purchaseValidatorRules.stream()
                .filter(rule -> !rule.isValid(product, client, request, smartPayment))
                .map(PurchaseValidatorRules::getErrorResponse)
                .findFirst();
    }
}
