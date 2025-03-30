package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.user.User;

public interface PurchaseValidatorRules {

    boolean isValid(Product product, User client, PurchaseRequest request, boolean smartPayment);
    ResultErrorResponse getErrorResponse();
}
