package com.desafio.hotmart.application.core.domain.product;

import com.desafio.hotmart.application.core.domain.user.User;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private static final BigDecimal STANDARD_INTEREST_IN_PERCENTAGE = new BigDecimal("15.0");

    private Long id;

    private User user;

    private String code;

    private boolean active = true;

    private BigDecimal fee;

    private List<ProductOffer> offers = new ArrayList<>();

    public Product(Long id, User user, String code, boolean active, BigDecimal fee, List<ProductOffer> offers) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.active = active;
        this.fee = fee;
        this.offers = offers;
    }

    public Product(User user, String code, ProductOffer offer) {
        Assert.notNull(offer, "offer cannot be null");
        this.user = user;
        this.code = code;
        this.fee = STANDARD_INTEREST_IN_PERCENTAGE;
        this.offers.add(offer);
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

    public boolean isActive() {
        return active;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public List<ProductOffer> getOffers() {
        return offers;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
