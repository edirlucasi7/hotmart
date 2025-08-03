package com.desafio.hotmart.infrastructure.adapter.out.product.entity;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
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
    private UserEntity userEntity;

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
        this.userEntity = new UserEntity(user);
        this.code = code;
        this.fee = STANDARD_INTEREST_IN_PERCENTAGE;
        this.addOffer(productOfferEntity);
    }

    public ProductEntity(Product product) {
        this.id = product.getId();
        this.userEntity = new UserEntity(product.getUser());
        this.code = product.getCode();
        this.active = product.isActive();
        this.fee = product.getFee();
        this.offers = product.getOffers().stream()
                .map(ProductOfferEntity::new)
                .toList();
    }

    public String getCode() {
        return code;
    }

    public void addOffer(ProductOfferEntity productOfferEntity) {
        this.offers.forEach(ProductOfferEntity::disable);
        this.offers.add(productOfferEntity);
        this.activate();
    }

    public void updateFees(BigDecimal fees) {
        this.fee = fees;
    }

    private void activate() {
        if (this.active) return;
        this.active = true;
    }

    public Product toProduct() {
        return new Product(this.id, this.userEntity.toUser(), this.code, this.active, this.fee,
                this.offers.stream()
                .map(ProductOfferEntity::toProductOffer)
                .toList());
    }
}