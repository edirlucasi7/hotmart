package com.desafio.hotmart.application.core.service.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponServicePort {

    Optional<BigDecimal> tryGetDiscount(String coupon, String productCode) throws ProductNotFoundException;

    Coupon invalidate(String code, BigDecimal discountValue, Product product);

    Coupon save(Coupon coupon, Product product);
}
