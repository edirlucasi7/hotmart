package com.desafio.hotmart.application.core.domain.purchase;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.user.User;
import com.github.f4b6a3.tsid.Tsid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.desafio.hotmart.application.core.domain.purchase.PurchaseStatus.REGULAR;
import static com.desafio.hotmart.application.core.domain.purchase.PurchaseStatus.SMART;

public class Purchase {

    private Long id;

    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expirationAt = LocalDateTime.now().plusYears(1);

    private LocalDateTime updatedAt;

    private BigDecimal price;

    private int retryAttempt = 0;

    private String cartUUID = Tsid.fast().toString();

    private Product product;

    private PurchaseStatus status;

    private PurchaseType type;

    public Purchase() { }

    public Purchase(User user, BigDecimal price, Product product, PurchaseType type) {
        this.user = user;
        this.price = price;
        this.product = product;
        this.status = REGULAR;
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PurchaseStatus getStatus() {
        return status;
    }

    public boolean isInterestBorneByProductOwner() {
        return SMART != this.status && this.product.isPaidByProducer();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public User getProductOwner() {
        return product.getUser();
    }

    public BigDecimal getFeeProduct() {
        return product.getFee();
    }

    public void updatedStatusToSmart() {
        this.status = SMART;
    }

    public PurchaseStatus assignStatus(boolean isSmart) {
        return isSmart ? SMART : REGULAR;
    }

    public String getProductCode() {
        return this.product.getCode();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public PurchaseType getType() {
        return this.type;
    }
}