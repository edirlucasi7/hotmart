package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.application.core.domain.user.User;

public interface PurchaseValidatorRules {

    boolean isValid(ProductEntity productEntity, User client, PurchaseRequest request, boolean smartPayment);
    ResultErrorResponse getErrorResponse();
}
