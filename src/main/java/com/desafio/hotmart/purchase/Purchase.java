package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @NotNull
    private final LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private final LocalDateTime expirationAt = LocalDateTime.now().plusYears(1);

    private LocalDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PurchaseType purchaseType;

    @Min(0)
    private BigDecimal price;

    private boolean recurring;

    @Min(value = 1) @Max(value = 12)
    private int numberOfInstallments;

    @ManyToOne
    @JoinColumn
    private Product product;

    @Enumerated(EnumType.STRING)
    private State state;

    @Deprecated
    public Purchase() { }

    private Purchase(User user, PurchaseType purchaseType, BigDecimal price, boolean recurring, int numberOfInstallments, Product product, State state) {
        this.user = user;
        this.purchaseType = purchaseType;
        this.price = price;
        this.recurring = recurring;
        this.numberOfInstallments = purchaseType.setNumberOfInstallments(numberOfInstallments);
        this.product = product;
        this.state = state;
    }

    public static Purchase newPurchase(User user, PurchaseType purchaseType, BigDecimal price, boolean recurring, int numberOfInstallments, Product product) {
        return new Purchase(user, purchaseType, price, recurring, numberOfInstallments, product, purchaseType.getState());
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

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public Product getProduct() {
        return product;
    }

    public State getState() {
        return state;
    }

    public boolean isWait() {
        return this.state == State.WAIT;
    }

    public BigDecimal getFeeProduct() {
        return product.getFee();
    }

    public Purchase process() {
        this.state = State.PROCESSED;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public BigDecimal calculateAmountForPayout() {
        BigDecimal discountFactor = this.getFeeProduct().divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal discountValue = this.price.multiply(discountFactor);
        return this.price.subtract(discountValue);
    }
}