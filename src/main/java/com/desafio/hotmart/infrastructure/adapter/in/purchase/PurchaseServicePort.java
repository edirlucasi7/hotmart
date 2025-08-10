package com.desafio.hotmart.infrastructure.adapter.in.purchase;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.purchase.NewPurchaseContract;

public interface PurchaseServicePort {

    void save(NewPurchaseContract newPurchaseContract, Coupon coupon, boolean smartPayment);
}
