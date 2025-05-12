package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.clientPayout.PayoutRepository;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
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

    public void save(PurchaseRequest request, User client, Product product, BigDecimal discount, boolean smartPayment) {
        Purchase newPurchase = request.toPurchaseWithCouponDiscount(client, product, discount);
        Payout newPayout = request.toPayout(newPurchase);

        Purchase purchase = purchaseRepository.save(newPurchase);
        payoutRepository.save(newPayout);

        if (smartPayment) purchase.updatedStatusToSmart();
    }
}
