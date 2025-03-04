package com.desafio.hotmart;

import com.desafio.hotmart.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

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
    public Offer() { }

    public Offer(int maximumNumberOfInstallments, BigDecimal price, boolean smartPayment, String interestPayer) {
        this.maximumNumberOfInstallments = maximumNumberOfInstallments;
        this.price = price;
        this.smartPayment = smartPayment;
        this.interestPayer = InterestPayer.fromName(interestPayer);
    }

    public Long getId() {
        return id;
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

    public void setPost(Product product) {
        this.product = product;
    }

    public void disable() {
        this.active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return maximumNumberOfInstallments == offer.maximumNumberOfInstallments && active == offer.active && smartPayment == offer.smartPayment && Objects.equals(id, offer.id) && Objects.equals(product, offer.product) && Objects.equals(price, offer.price) && interestPayer == offer.interestPayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, maximumNumberOfInstallments, active, smartPayment, price, interestPayer);
    }
}