package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.desafio.hotmart.purchase.PurchaseStatus.REGULAR;
import static com.desafio.hotmart.purchase.PurchaseStatus.SMART;
import static jakarta.persistence.EnumType.STRING;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private LocalDateTime expirationAt = LocalDateTime.now().plusYears(1);

    private LocalDateTime updatedAt;

    @Min(0)
    private BigDecimal price;

    @Min(value = 0)
    private int retryAttempt = 0;

    private String cartUUID;

    @ManyToOne
    @JoinColumn
    private Product product;

    @Enumerated(STRING)
    private PurchaseStatus status;

    @Deprecated
    public Purchase() { }

    public Purchase(User user, BigDecimal price, Product product) {
        this.user = user;
        this.price = price;
        this.product = product;
        this.status = REGULAR;
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
        return !SMART.equals(this.status) && this.product.isPaidByProducer();
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
}