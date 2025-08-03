package com.desafio.hotmart.application.core.service.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.service.product.ProductService;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class CouponService implements CouponServicePort {

    private final CouponRepositoryPort couponRepositoryPort;
    private final ProductServicePort productServicePort;

    public CouponService(CouponRepositoryPort couponRepositoryPort, ProductServicePort productServicePort) {
        this.couponRepositoryPort = couponRepositoryPort;
        this.productServicePort = productServicePort;
    }

    @Override
    public Optional<BigDecimal> tryGetDiscount(String coupon, String productCode) throws ProductNotFoundException{
        Product product = productServicePort.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException(productCode));

        return couponRepositoryPort.findCouponByCodeAndProduct(coupon, product.getId()).map(Coupon::getDiscountValue);
    }

    @Override
    public Coupon invalidate(String code, BigDecimal discountValue, Product product) {
        return couponRepositoryPort.invalidate(code, discountValue, product);
    }

    @Override
    public Coupon save(Coupon coupon, Product product) {
        return couponRepositoryPort.save(coupon, product);
    }
}
