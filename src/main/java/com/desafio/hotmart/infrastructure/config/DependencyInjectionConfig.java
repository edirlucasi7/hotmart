package com.desafio.hotmart.infrastructure.config;

import com.desafio.hotmart.application.core.service.coupon.CouponService;
import com.desafio.hotmart.application.core.service.coupon.CouponServicePort;
import com.desafio.hotmart.application.core.service.product.ProductService;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import com.desafio.hotmart.application.port.product.ProductRepositoryPort;
import com.desafio.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    @Bean
    public CouponServicePort couponService(CouponRepositoryPort couponRepositoryPort) {
        return new CouponService((couponRepositoryPort));
    }

    @Bean
    public ProductServicePort productService(ProductRepositoryPort productRepositoryPort) {
        return new ProductService((productRepositoryPort));
    }
}
