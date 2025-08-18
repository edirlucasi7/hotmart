package com.hotmart.infrastructure.adapter.in.purchase.validator;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.application.core.domain.user.User;

public interface PurchaseValidator {

    void isValid(User client, Product product, Integer installments);

    default boolean shouldValidate(PurchaseType purchaseType, boolean smartPayment) {
        return true;
    }
}
