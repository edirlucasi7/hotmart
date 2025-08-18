package com.hotmart.infrastructure.adapter.in.purchase.validator;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.application.core.domain.user.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class SmartPaymentCreditCardInstallmentValidator implements PurchaseValidator {

    @Override
    public void isValid(User client, Product product, Integer installments) {
        if (!product.hasValidNumberOfInstallments(installments)) {;
            throw new IllegalArgumentException("Invalid number of installments for smart payment with credit card. "
                    + "Must not exceed the maximum allowed by the product.");
        }
    }

    @Override
    public boolean shouldValidate(PurchaseType purchaseType, boolean isSmartPayment) {
        return purchaseType.isSmartPaymentWithCreditCard(isSmartPayment);
    }
}
