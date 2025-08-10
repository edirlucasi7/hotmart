package com.desafio.hotmart.application.core.domain.purchase;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
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

    private Coupon coupon;

    public Purchase() { }

    public Purchase(User user, Coupon coupon, Product product, PurchaseType type) {
        this.user = user;
        this.coupon = coupon;
        this.price = product.calculatePriceWithDiscount(coupon);
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

    public BigDecimal getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
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

    public BigDecimal getFeeProduct() {
        return this.product.getFee();
    }

    public boolean isInterestBorneByProductOwner() {
        return SMART != this.status && this.product.isPaidByProducer();
    }

    public PurchaseType getPurchaseType() {
        return type;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}