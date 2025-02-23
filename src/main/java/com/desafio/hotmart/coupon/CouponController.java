package com.desafio.hotmart.coupon;

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

import java.util.Optional;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;

    public CouponController(ProductRepository productRepository, CouponRepository couponRepository, CouponService couponService) {
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.couponService = couponService;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CouponRequest request) {
        Optional<Product> possibleProduct = productRepository.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        Optional<Coupon> activeCoupon = couponRepository.findCouponByCodeAndActiveStatus(request.code(), product.getId());
        activeCoupon.ifPresent(couponService::invalidate);

        Coupon coupon = request.toCoupon(product);
        couponRepository.save(coupon);
        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon created successfully")));
    }
}