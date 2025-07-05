package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.purchase.PurchaseRepository;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.application.core.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class CustomerAlreadyHasTheProduct implements PurchaseValidatorRules {

    private final PurchaseRepository purchaseRepository;

    public CustomerAlreadyHasTheProduct(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public boolean isValid(ProductEntity productEntity, User client, PurchaseRequest request, boolean smartPayment) {
        return !purchaseRepository.hasValidPurchaseAssociatedWith(client.getId(), productEntity.getCode());
    }

    @Override
    public ResultErrorResponse getErrorResponse() {
        return new ResultErrorResponse("", "The client already has the valid purchase for the product");
    }
}