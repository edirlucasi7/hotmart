package com.desafio.hotmart.application.port.purchase;

import com.desafio.hotmart.application.core.domain.payout.Payout;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;

public interface PurchaseRepositoryPort {
    void save(Purchase newPurchase, Payout newPayout, boolean smartPayment);
    boolean hasValidPurchaseAssociatedWith(Long clientId, String productCode);
}
