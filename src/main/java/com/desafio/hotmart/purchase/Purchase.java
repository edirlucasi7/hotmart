package com.desafio.hotmart.purchase;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.application.core.domain.user.User;
import com.github.f4b6a3.tsid.Tsid;
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

    private String cartUUID = Tsid.fast().toString();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Enumerated(STRING)
    private PurchaseStatus status;

    @Deprecated
    public Purchase() { }

    public Purchase(User user, BigDecimal price, ProductEntity productEntity) {
        this.user = user;
        this.price = price;
        this.productEntity = productEntity;
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
        return SMART != this.status && this.productEntity.isPaidByProducer();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductEntity getProduct() {
        return productEntity;
    }

    public User getProductOwner() {
        return productEntity.getUserEntity().toUser();
    }

    public BigDecimal getFeeProduct() {
        return productEntity.getFee();
    }

    public void updatedStatusToSmart() {
        this.status = SMART;
    }
}