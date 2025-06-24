package com.desafio.hotmart.infrastructure.adapter.out.product.entity;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "product")
public class ProductEntity {

    private static final BigDecimal STANDARD_INTEREST_IN_PERCENTAGE = new BigDecimal("15.0");

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

    @NotNull
    @ElementCollection
    @CollectionTable(name = "product_offers", joinColumns = @JoinColumn(name = "product_id"))
    private List<ProductOfferEntity> offers = new ArrayList<>();

    @Deprecated
    public ProductEntity() { }

    public ProductEntity(User user, String code, ProductOfferEntity productOfferEntity) {
        Assert.notNull(user, "User must not be null!");
        Assert.notNull(productOfferEntity, "Offer must not be null!");
        Assert.notNull(code, "Code must not be null!");
        this.user = user;
        this.code = code;
        this.fee = STANDARD_INTEREST_IN_PERCENTAGE;
        this.addOffer(productOfferEntity);
    }

    public ProductEntity(Product product) {
        this.id = product.getId();
        this.user = product.getUser();
        this.code = product.getCode();
        this.active = product.isActive();
        this.fee = product.getFee();
        this.offers = product.getOffers().stream()
                .map(ProductOfferEntity::new)
                .toList();
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

    public List<ProductOfferEntity> getOffers() {
        return offers;
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    public void addOffer(ProductOfferEntity productOfferEntity) {
        this.offers.forEach(ProductOfferEntity::disable);
        this.offers.add(productOfferEntity);
        this.activate();
    }

    public BigDecimal getPriceFromActiveOffer() {
        return this.offers.stream()
                .filter(ProductOfferEntity::isActive)
                .map(ProductOfferEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isPaidByProducer() {
        return getActiveOffer().isPaidByProducer();
    }

    public int getMaximumNumberOfInstallmentsFromActiveOffer() {
        Assert.isTrue(this.active, "Product must be active to get maximum number of installments");
        return this.offers
                .stream()
                .filter(ProductOfferEntity::isActive)
                .findFirst()
                .map(ProductOfferEntity::getMaximumNumberOfInstallments)
                .orElse(1);
    }

    public BigDecimal calculatePriceWithDiscount(BigDecimal discountAmount) {
        BigDecimal discountFactor = discountAmount.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal discountValue = getPriceFromActiveOffer().multiply(discountFactor);
        return getPriceFromActiveOffer().subtract(discountValue);
    }

    public void updateFees(BigDecimal fees) {
        this.fee = fees;
    }

    private void activate() {
        if (this.active) return;
        this.active = true;
    }

    private ProductOfferEntity getActiveOffer() {
        return this.offers.stream()
                .filter(ProductOfferEntity::isActive)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public Product toProduct() {
        return new Product(
                this.id,
                this.user,
                this.code,
                this.active,
                this.fee,
                this.offers.stream().map(ProductOfferEntity::toProductOffer).toList()
        );
    }
}