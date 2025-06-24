package com.desafio.hotmart.application.core.domain.product;

import com.desafio.hotmart.user.User;

import java.math.BigDecimal;
import java.util.List;

public class Product {

    private Long id;

    private User user;

    private String code;

    private boolean active = true;

    private BigDecimal fee;

    private List<ProductOffer> offers;

    public Product(Long id, User user, String code, boolean active, BigDecimal fee, List<ProductOffer> offers) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.active = active;
        this.fee = fee;
        this.offers = offers;
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
