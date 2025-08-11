package com.hotmart.infrastructure.adapter.in.purchase;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.purchase.NewPurchaseContract;

public interface PurchaseServicePort {

    void save(NewPurchaseContract newPurchaseContract, Coupon coupon, boolean smartPayment);
}
