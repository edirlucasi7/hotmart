package com.hotmart.application.core.domain.product;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.user.User;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private static final BigDecimal STANDARD_INTEREST_IN_PERCENTAGE = BigDecimal.valueOf(15.0);

    private Long id;

    private User user;

    private String code;

    private boolean active = true;

    private BigDecimal fee;

    private List<ProductOffer> offers = new ArrayList<>();

    public Product(Long id, User user, String code, boolean active, BigDecimal fee, List<ProductOffer> offers) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.active = active;
        this.fee = fee;
        this.offers = offers;
    }

    public Product(User user, String code, ProductOffer offer) {
        Assert.notNull(offer, "offer cannot be null");
        this.user = user;
        this.code = code;
        this.fee = STANDARD_INTEREST_IN_PERCENTAGE;
        this.offers.add(offer);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public List<ProductOffer> getOffers() {
        return offers;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public int getMaximumNumberOfInstallmentsFromActiveOffer() {
        Assert.isTrue(this.active, "Product must be active to get maximum number of installments");
        return this.offers
                .stream()
                .filter(ProductOffer::isActive)
                .findFirst()
                .map(ProductOffer::getMaximumNumberOfInstallments)
                .orElse(1);
    }

    public BigDecimal calculatePriceWithDiscount(Coupon coupon) {
        BigDecimal discountPercentage = discount(coupon);
        BigDecimal discountRatio = discountPercentage.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal originalPrice = getPriceFromActiveOffer().multiply(discountRatio);
        return getPriceFromActiveOffer().subtract(originalPrice);
    }

    private BigDecimal discount(Coupon coupon) {
        return coupon != null ? coupon.getDiscountValue() : BigDecimal.ZERO;
    }

    public BigDecimal getPriceFromActiveOffer() {
        return this.offers.stream()
                .filter(ProductOffer::isActive)
                .map(ProductOffer::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isPaidByProducer() {
        return getActiveOffer().isPaidByProducer();
    }

    private ProductOffer getActiveOffer() {
        return this.offers.stream()
                .filter(ProductOffer::isActive)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public boolean hasValidNumberOfInstallments(Integer installments) {
        return installments == this.getMaximumNumberOfInstallmentsFromActiveOffer();
    }
}
