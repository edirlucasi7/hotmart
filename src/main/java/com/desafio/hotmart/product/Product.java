package com.desafio.hotmart.product;

import com.desafio.hotmart.Offer;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    private static final BigDecimal STANDARD_INTEREST_IN_PERCENTAGE = new BigDecimal("20.0");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn
    private User user;

    @NotBlank
    private String code;
    
    private boolean active = true;

    @NotNull
    private BigDecimal fee;

    private int confirmationTime;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offer> offers = new ArrayList<>();

    @Deprecated
    public Product() { }

    public Product(User user, String code, int confirmationTime, Offer offer) {
        Assert.notNull(user, "User must not be null!");
        Assert.notNull(offer, "Offer must not be null!");
        Assert.notNull(code, "Code must not be null!");
        this.user = user;
        this.code = code;
        this.confirmationTime = confirmationTime;
        this.fee = STANDARD_INTEREST_IN_PERCENTAGE;
        this.addOffer(offer);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public int getConfirmationTime() {
        return confirmationTime;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public void addOffer(Offer offer) {
        this.offers.forEach(Offer::disable);
        this.offers.add(offer);
        offer.setPost(this);
        this.activate();
    }

    public void removeOffer(Offer offer) {
        this.offers.remove(offer);
        offer.setPost(null);
        if (!this.existsOfferActive()) this.disable();
    }

    public BigDecimal getPriceFromActiveOffer() {
        return this.offers.stream().filter(Offer::isActive).map(Offer::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getMaximumNumberOfInstallmentsFromActiveOffer() {
        if (!this.active) throw new IllegalStateException();
        return this.offers.stream().filter(Offer::isActive).findFirst().map(Offer::getMaximumNumberOfInstallments).orElse(1);
    }

    public BigDecimal calculatePriceWithDiscount(BigDecimal discountAmount) {
        BigDecimal discountFactor = discountAmount.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal discountValue = getPriceFromActiveOffer().multiply(discountFactor);
        return getPriceFromActiveOffer().subtract(discountValue);
    }

    public void updateConfirmationTime(int confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public void updateFees(BigDecimal fees) {
        this.fee = fees;
    }

    private void disable() {
        if (!this.active) return;
        this.active = false;
    }

    private void activate() {
        if (this.active) return;
        this.active = true;
    }

    private boolean existsOfferActive() {
        return this.offers.stream().anyMatch(Offer::isActive);
    }
}