package com.desafio.hotmart.infrastructure.adapter.out.purchase;

import com.desafio.hotmart.application.core.domain.payout.Payout;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.out.payout.entity.PayoutEntity;
import com.desafio.hotmart.infrastructure.adapter.out.payout.repository.PayoutRepository;
import com.desafio.hotmart.infrastructure.adapter.out.purchase.entity.PurchaseEntity;
import com.desafio.hotmart.infrastructure.adapter.out.purchase.repository.PurchaseRepository;
import org.springframework.stereotype.Component;

@Component
public class PurchaseRepositoryAdapter implements PurchaseRepositoryPort {

    private final PurchaseRepository purchaseRepository;
    private final PayoutRepository payoutRepository;

    public PurchaseRepositoryAdapter(PurchaseRepository purchaseRepository, PayoutRepository payoutRepository) {
        this.purchaseRepository = purchaseRepository;
        this.payoutRepository = payoutRepository;
    }

    @Override
    public void save(Purchase newPurchase, Payout newPayout, boolean smartPayment) {
        PurchaseEntity purchaseEntity = purchaseRepository.save(new PurchaseEntity(newPurchase, smartPayment));
        purchaseRepository.save(purchaseEntity);
        payoutRepository.save(new PayoutEntity(purchaseEntity, newPayout));
    }

    @Override
    public boolean hasValidPurchaseAssociatedWith(Long clientId, String productCode) {
        return purchaseRepository.hasValidPurchaseAssociatedWith(clientId, productCode);
    }
}