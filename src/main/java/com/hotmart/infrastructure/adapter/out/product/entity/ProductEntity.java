package com.hotmart.infrastructure.adapter.out.product.entity;

import com.hotmart.application.core.domain.product.Product;
import com.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "product")
public class ProductEntity {

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
        return new Product(id, userEntity.toUser(), code, active, fee,
                this.offers.stream()
                        .map(ProductOfferEntity::toProductOffer)
                        .toList());
    }
}