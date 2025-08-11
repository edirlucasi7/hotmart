package com.hotmart.infrastructure.adapter.out.purchase.entity;

import com.hotmart.application.core.domain.purchase.Purchase;
import com.hotmart.application.core.domain.purchase.PurchaseStatus;
import com.hotmart.application.core.domain.purchase.PurchaseType;
import com.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.hotmart.infrastructure.adapter.out.user.entity.UserEntity;
import com.github.f4b6a3.tsid.Tsid;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "purchase")
public class PurchaseEntity {

    // TODO faltou associar a compra a oferta usada

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity couponEntity;

    @Deprecated
    public PurchaseEntity() { }

    public PurchaseEntity(Purchase purchase, boolean isSmart) {
        this.userEntity = new UserEntity(purchase.getUser());
        this.couponEntity = new CouponEntity(purchase.getCoupon());
        this.price = purchase.getPrice();
        this.productEntity = new ProductEntity(purchase.getProduct());
        this.status = purchase.assignStatus(isSmart);
        this.purchaseType = purchase.getPurchaseType();
    }

    public Purchase toPurchase() {
        return new Purchase(userEntity.toUser(), couponEntity.toCoupon(), productEntity.toProduct(), purchaseType);
    }
}
