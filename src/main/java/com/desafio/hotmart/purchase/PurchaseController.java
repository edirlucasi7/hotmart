package com.desafio.hotmart.purchase;

import com.desafio.hotmart.application.core.service.coupon.CouponService;
import com.desafio.hotmart.application.core.domain.coupon.validator.CouponValidator;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.repository.ProductEntityRepository;
import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import com.desafio.hotmart.purchase.validator.PurchaseHandlerValidator;
import com.desafio.hotmart.purchase.validator.ResultErrorResponse;
import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.application.core.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final ProductEntityRepository productEntityRepository;
    private final PurchaseHandlerValidator purchaseHandlerValidator;
    private final CouponService couponService;
    private final PurchaseService purchaseService;
    private final UserService userService;

    public PurchaseController(ProductEntityRepository productEntityRepository, PurchaseHandlerValidator purchaseHandlerValidator, CouponService couponService, PurchaseService purchaseService, UserService userService) {
        this.productEntityRepository = productEntityRepository;
        this.purchaseHandlerValidator = purchaseHandlerValidator;
        this.couponService = couponService;
        this.purchaseService = purchaseService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest request, @RequestParam(required = false) String coupon,
                                    @RequestParam(required = false) boolean smartPayment) {
        try {
            Optional<ProductEntity> possibleProduct = productEntityRepository.findByCodeAndActiveIsTrue(request.productCode());
            if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

            User client = userService.getBy(request.email());
            ProductEntity productEntity = possibleProduct.get();

            Optional<ResultErrorResponse> possibleErrorResponse = purchaseHandlerValidator.handler(productEntity, client, request, smartPayment);
            if (possibleErrorResponse.isPresent()) {
                ResultErrorResponse resultErrorResponse = possibleErrorResponse.get();
                return ResponseEntity.unprocessableEntity().body(new PurchaseEventResultBody(resultErrorResponse.errorMessage()));
            }

            Optional<BigDecimal> discount = Optional.of(BigDecimal.ZERO);
            if (CouponValidator.isValid(coupon)) {
                discount = couponService.tryGetDiscount(coupon, productEntity);
                if (discount.isEmpty()) return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
            }

            // PurchaseType.getByType(request.type()) processar no provedor

            purchaseService.save(request, client, productEntity, discount.get(), smartPayment);

            return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase received")));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("payment failed")));
        }
    }
}