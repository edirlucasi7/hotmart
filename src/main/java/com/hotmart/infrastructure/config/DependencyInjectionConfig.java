package com.hotmart.infrastructure.config;

import com.hotmart.application.core.service.coupon.CouponService;
import com.hotmart.application.core.service.coupon.CouponServicePort;
import com.hotmart.application.core.service.product.ProductService;
import com.hotmart.application.core.service.purchase.PurchaseCreateService;
import com.hotmart.application.port.coupon.CouponRepositoryPort;
import com.hotmart.application.port.product.ProductRepositoryPort;
import com.hotmart.application.port.purchase.PurchaseRepositoryPort;
import com.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import com.hotmart.infrastructure.adapter.in.purchase.PurchaseServicePort;
import com.hotmart.infrastructure.adapter.in.user.UserServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    @Bean
    public CouponServicePort couponService(CouponRepositoryPort couponRepositoryPort, ProductServicePort productServicePort) {
        return new CouponService(couponRepositoryPort, productServicePort);
    }

    @Bean
    public ProductServicePort productService(ProductRepositoryPort productRepositoryPort) {
        return new ProductService((productRepositoryPort));
    }

    @Bean
    public PurchaseServicePort purchaseService(PurchaseRepositoryPort purchaseRepositoryPort, ProductServicePort productServicePort, UserServicePort userServicePort) {
        return new PurchaseCreateService(purchaseRepositoryPort, productServicePort, userServicePort);
    }
}
