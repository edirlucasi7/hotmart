package com.desafio.hotmart.application.core.domain.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.InterestPayer;

import java.math.BigDecimal;

import static com.desafio.hotmart.infrastructure.adapter.out.product.entity.InterestPayer.INFO_PRODUCER;

public class ProductOffer {

    private int maximumNumberOfInstallments;

    private boolean active = true;

    private boolean smartPayment;

    private BigDecimal price;

    private InterestPayer interestPayer;

    public ProductOffer() {}

    public ProductOffer(int maximumNumberOfInstallments, BigDecimal price, boolean smartPayment, InterestPayer interestPayer) {
        this.maximumNumberOfInstallments = maximumNumberOfInstallments;
        this.price = price;
        this.smartPayment = smartPayment;
        this.interestPayer = interestPayer;
    }

    public int getMaximumNumberOfInstallments() {
        return maximumNumberOfInstallments;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSmartPayment() {
        return smartPayment;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public InterestPayer getInterestPayer() {
        return interestPayer;
    }

    public boolean isPaidByProducer() {
        return this.interestPayer == INFO_PRODUCER;
    }
}
