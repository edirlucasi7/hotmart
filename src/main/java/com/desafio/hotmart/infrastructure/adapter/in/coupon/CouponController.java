package com.desafio.hotmart.infrastructure.adapter.in.coupon;

import com.desafio.hotmart.application.core.domain.coupon.Coupon;
import com.desafio.hotmart.application.core.service.coupon.CouponServicePort;
import com.desafio.hotmart.infrastructure.adapter.out.coupon.entity.CouponEntity;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponServicePort couponServicePort;
    private final ProductRepository productRepository;

    public CouponController(CouponServicePort couponServicePort, ProductRepository productRepository) {
        this.couponServicePort = couponServicePort;
        this.productRepository = productRepository;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CouponRequest request) {
        Optional<Product> possibleProduct = productRepository.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        Coupon activeCoupon = couponServicePort.invalidate(request.code(), request.discountValue(), product);

        Coupon coupon = couponServicePort.save(activeCoupon);
        URI uri = URI.create("/coupon/" + coupon.getId());
        return ResponseEntity.created(uri).body("coupon created successfully");
    }
}