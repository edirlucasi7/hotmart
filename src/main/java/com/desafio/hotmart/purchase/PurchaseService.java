package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.clientPayout.PayoutRepository;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.application.core.domain.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PayoutRepository payoutRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, PayoutRepository payoutRepository) {
        this.purchaseRepository = purchaseRepository;
        this.payoutRepository = payoutRepository;
    }

    public void save(PurchaseRequest request, User client, ProductEntity productEntity, BigDecimal discount, boolean smartPayment) {
        Purchase newPurchase = request.toPurchaseWithCouponDiscount(client, productEntity, discount);
        Payout newPayout = request.toPayout(newPurchase);

        Purchase purchase = purchaseRepository.save(newPurchase);
        payoutRepository.save(newPayout);

        if (smartPayment) purchase.updatedStatusToSmart();
    }
}
