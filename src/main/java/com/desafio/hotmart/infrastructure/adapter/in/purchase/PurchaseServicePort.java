package com.desafio.hotmart.infrastructure.adapter.in.purchase;

import com.desafio.hotmart.application.core.domain.purchase.NewPurchaseContract;

import java.math.BigDecimal;

public interface PurchaseServicePort {

    void save(NewPurchaseContract newPurchaseContract, BigDecimal bigDecimal, boolean smartPayment);
}
