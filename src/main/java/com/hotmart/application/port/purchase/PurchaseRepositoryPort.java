package com.hotmart.application.port.purchase;

import com.hotmart.application.core.domain.payout.Payout;
import com.hotmart.application.core.domain.purchase.Purchase;

public interface PurchaseRepositoryPort {
    void save(Purchase newPurchase, Payout newPayout, boolean smartPayment);
    boolean hasValidPurchaseAssociatedWith(Long clientId, String productCode);
}
