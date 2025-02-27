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

    @ManyToOne
    @JoinColumn
    private User user;

    @NotBlank
    private String code;

    @NotNull
    private BigDecimal fees;

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
        this.fees = STANDARD_INTEREST_IN_PERCENTAGE;
        this.addOffer(offer);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCode() {
        return code;
    }

    public int getConfirmationTime() {
        return confirmationTime;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void addOffer(Offer offer) {
        offers.forEach(Offer::disable);
        offers.add(offer);
        offer.setPost(this);
    }

    public void removeOffer(Offer offer) {
        if (offer.isActive()) throw new IllegalArgumentException("Offer is active!");
        offers.remove(offer);
        offer.setPost(null);
    }

    public BigDecimal getPriceFromActiveOffer() {
        return this.offers.stream().filter(Offer::isActive).map(Offer::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculatePriceWithDiscount(BigDecimal discountAmount) {
        BigDecimal discountFactor = discountAmount.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal discountValue = getPriceFromActiveOffer().multiply(discountFactor);
        return getPriceFromActiveOffer().subtract(discountValue);
    }

    public void updateConfirmationTime(int confirmationTime) {
        this.confirmationTime = confirmationTime;
    }
}