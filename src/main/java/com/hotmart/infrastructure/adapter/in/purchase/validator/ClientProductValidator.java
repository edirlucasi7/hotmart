package com.hotmart.infrastructure.adapter.in.purchase.validator;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.domain.user.User;
import com.hotmart.application.port.purchase.PurchaseRepositoryPort;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(0)
@Component
public class ClientProductValidator implements PurchaseValidator {

    private final PurchaseRepositoryPort purchaseRepositoryPort;

    public ClientProductValidator(PurchaseRepositoryPort purchaseRepositoryPort) {
        this.purchaseRepositoryPort = purchaseRepositoryPort;
    }

    @Override
    public void isValid(User client, Product product, Integer installments) {
        if (ifClientAlreadyHasProduct(client, product)) {
            throw new IllegalStateException("The client already has the valid purchase for the product");
        }
    }

    private boolean ifClientAlreadyHasProduct(User client, Product product) {
        return purchaseRepositoryPort.hasValidPurchaseAssociatedWith(client.getId(), product.getCode());
    }
}
