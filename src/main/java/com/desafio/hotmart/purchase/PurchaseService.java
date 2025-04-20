package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.Payout;
import com.desafio.hotmart.clientPayout.PayoutRepository;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PayoutRepository payoutRepository;
    private final SmartPurchaseRepository smartPurchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, PayoutRepository payoutRepository, SmartPurchaseRepository smartPurchaseRepository) {
        this.purchaseRepository = purchaseRepository;
        this.payoutRepository = payoutRepository;
        this.smartPurchaseRepository = smartPurchaseRepository;
    }

    public Purchase save(PurchaseRequest request, User client, Product product, BigDecimal discount, boolean smartPayment) {
        Purchase newPurchase = request.toPurchaseWithCouponDiscount(client, product, discount, smartPayment);
        Payout newPayout = request.toPayout(newPurchase);

        purchaseRepository.save(newPurchase);
        payoutRepository.save(newPayout);

        if (smartPayment) generateSmartPayment(newPurchase);

        return newPurchase;
    }

    private void generateSmartPayment(Purchase purchase) {
        if (purchase.hasValidInstallment()) throw new IllegalStateException();

        List<SmartPurchase> smartPurchases = purchase.createSmartPurchase();
        smartPurchaseRepository.saveAll(smartPurchases);
    }
}
