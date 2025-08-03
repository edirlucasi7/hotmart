package com.desafio.hotmart.infrastructure.adapter.out.product.entity;

import com.desafio.hotmart.application.core.domain.product.ProductOffer;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductOfferEntity {

    @Min(value = 1) @Max(value = 12)
    private int maximumNumberOfInstallments;

    private boolean active = true;

    private boolean smartPayment;

    @NotNull
    @Min(value = 0)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InterestPayer interestPayer;

    @Deprecated
    public ProductOfferEntity() { }

    public ProductOfferEntity(ProductOffer productOffer) {
        this.maximumNumberOfInstallments = productOffer.getMaximumNumberOfInstallments();
        this.price = productOffer.getPrice();
        this.smartPayment = productOffer.isSmartPayment();
        this.active = productOffer.isActive();
        this.interestPayer = productOffer.getInterestPayer();
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public InterestPayer getInterestPayer() {
        return interestPayer;
    }

    public int getMaximumNumberOfInstallments() {
        return maximumNumberOfInstallments;
    }

    public void disable() {
        this.active = false;
    }

    public ProductOffer toProductOffer() {
        return new ProductOffer(this.maximumNumberOfInstallments, this.price, this.smartPayment, interestPayer);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductOfferEntity that = (ProductOfferEntity) o;
        return maximumNumberOfInstallments == that.maximumNumberOfInstallments && active == that.active && smartPayment == that.smartPayment && Objects.equals(price, that.price) && interestPayer == that.interestPayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximumNumberOfInstallments, active, smartPayment, price, interestPayer);
    }
}