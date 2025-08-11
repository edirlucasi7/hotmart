package com.hotmart.application.core.service.coupon;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.port.coupon.CouponRepositoryPort;
import com.hotmart.infrastructure.adapter.in.product.ProductServicePort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class CouponService implements CouponServicePort {

    private final CouponRepositoryPort couponRepositoryPort;
    private final ProductServicePort productServicePort;

    public CouponService(CouponRepositoryPort couponRepositoryPort, ProductServicePort productServicePort) {
        this.couponRepositoryPort = couponRepositoryPort;
        this.productServicePort = productServicePort;
    }

    @Override
    public Optional<Coupon> tryGetDiscount(String coupon, String productCode){
        Product product = productServicePort.findByCode(productCode).orElseThrow(IllegalArgumentException::new);
        return couponRepositoryPort.findCouponByCodeAndProduct(coupon, product.getId());
    }

    @Override
    public void invalidate(String code, Product product) {
        couponRepositoryPort.invalidate(code, product);
    }

    @Override
    public Coupon save(String code, BigDecimal discountValue, LocalDateTime expirationAt, Product product) {
        return couponRepositoryPort.save(new Coupon(code, discountValue, expirationAt, product)).toCoupon();
    }
}
