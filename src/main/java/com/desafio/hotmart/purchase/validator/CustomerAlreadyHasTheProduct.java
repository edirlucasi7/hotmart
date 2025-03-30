package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.purchase.PurchaseRepository;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.user.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Order(0)
@Component
public class CustomerAlreadyHasTheProduct implements PurchaseValidatorRules {

    private final PurchaseRepository purchaseRepository;

    public CustomerAlreadyHasTheProduct(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public boolean isValid(Product product, User client, PurchaseRequest request, boolean smartPayment) {
        return !purchaseRepository.hasValidPurchaseAssociatedWith(client.getId(), product.getCode(), LocalDateTime.now());
    }

    @Override
    public ResultErrorResponse getErrorResponse() {
        return new ResultErrorResponse("", "The client already has the valid purchase for the product");
    }
}