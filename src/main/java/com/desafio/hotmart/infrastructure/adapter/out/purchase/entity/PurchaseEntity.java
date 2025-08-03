package com.desafio.hotmart.infrastructure.adapter.out.purchase.entity;

import com.desafio.hotmart.application.core.domain.purchase.Purchase;
import com.desafio.hotmart.application.core.domain.purchase.PurchaseStatus;
import com.desafio.hotmart.application.core.domain.purchase.PurchaseType;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import com.github.f4b6a3.tsid.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private UserEntity userEntity;

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

    @Enumerated(STRING)
    private PurchaseType purchaseType;

    @Deprecated
    public PurchaseEntity() { }

    public PurchaseEntity(Purchase purchase, boolean isSmart) {
        this.userEntity = new UserEntity(purchase.getUser());
        this.price = purchase.getPrice();
        this.productEntity = new ProductEntity(purchase.getProduct());
        this.status = purchase.assignStatus(isSmart);
    }

    public Purchase toPurchase() {
        return new Purchase(userEntity.toUser(), price, productEntity.toProduct(), purchaseType);
    }
}
