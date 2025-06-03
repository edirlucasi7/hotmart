package com.desafio.hotmart.infrastructure.config;

import com.desafio.hotmart.application.core.service.coupon.CouponService;
import com.desafio.hotmart.application.core.service.coupon.CouponServicePort;
import com.desafio.hotmart.application.port.coupon.CouponRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DependencyInjectionConfig {

    @Bean
    public CouponServicePort productService(CouponRepositoryPort couponRepositoryPort) {
        return new CouponService((couponRepositoryPort));
    }
}
