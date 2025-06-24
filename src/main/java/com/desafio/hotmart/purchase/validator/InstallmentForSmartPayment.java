package com.desafio.hotmart.purchase.validator;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.purchase.PurchaseRequest;
import com.desafio.hotmart.purchase.PurchaseType;
import com.desafio.hotmart.user.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
public class InstallmentForSmartPayment implements PurchaseValidatorRules {

    @Override
    public boolean isValid(ProductEntity productEntity, User client, PurchaseRequest request, boolean smartPayment) {
        if (!smartPayment) return true;

        PurchaseType purchaseType = PurchaseType.getByName(request.type());
        return request.installments() == productEntity.getMaximumNumberOfInstallmentsFromActiveOffer()
                && purchaseType.isCreditCard();
    }

    @Override
    public ResultErrorResponse getErrorResponse() {
        return new ResultErrorResponse("",
                "The number of installments of a smart payment " +
                        "must be the maximum allowed by product and have the type credit card.");
    }
}
