package com.hotmart.infrastructure.adapter.in.purchase;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.purchase.NewPurchaseContract;
import org.springframework.lang.Nullable;

public interface PurchaseServicePort {

    void save(NewPurchaseContract newPurchaseContract, @Nullable Coupon coupon, boolean smartPayment);
}
