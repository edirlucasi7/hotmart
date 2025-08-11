package com.hotmart.infrastructure.adapter.in.coupon;

import com.hotmart.application.core.domain.coupon.Coupon;
import com.hotmart.application.core.domain.product.Product;
import com.hotmart.application.core.service.coupon.CouponServicePort;
import com.hotmart.infrastructure.adapter.in.product.ProductServicePort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponServicePort couponServicePort;
    private final ProductServicePort productServicePort;

    public CouponController(CouponServicePort couponServicePort, ProductServicePort productServicePort) {
        this.couponServicePort = couponServicePort;
        this.productServicePort = productServicePort;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CouponRequest request) {
        Optional<Product> possibleProduct = productServicePort.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.status(NOT_FOUND)
                .body("Product not found with code: " + request.productCode());

        Product product = possibleProduct.get();
        couponServicePort.invalidate(request.code(), product);

        Coupon coupon = couponServicePort.save(request.code(), request.discountValue(), request.expirationAt(), product);
        URI uri = URI.create("/coupon/" + coupon.getId());
        return ResponseEntity.created(uri).body("coupon created successfully");
    }
}