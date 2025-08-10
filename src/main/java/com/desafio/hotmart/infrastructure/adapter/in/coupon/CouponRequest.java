package com.desafio.hotmart.infrastructure.adapter.in.coupon;

import jakarta.validation.constraints.Future;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponRequest(String code, String productCode, BigDecimal discountValue,
                            @Future(message = "A data de expiração deve ser futura") LocalDateTime expirationAt) {
}
