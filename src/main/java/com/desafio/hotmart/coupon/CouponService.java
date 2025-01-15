package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Optional<BigDecimal> tryGetDiscount(String coupon, Product product) {
        return couponRepository.findCouponByCodeAndProduct(coupon, product.getId()).map(Coupon::getDiscountValue);
    }

    public void invalidate(Coupon coupon) {
        coupon.invalidate();
    }
}
