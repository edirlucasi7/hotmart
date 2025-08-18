package com.hotmart.infrastructure.adapter.in.purchase.validator;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.application.core.domain.user.User;

public record PurchaseValidationResponse(PurchaseType purchaseType, User client, Product product,
                                         Integer installments, boolean isSmartPayment) {
}
