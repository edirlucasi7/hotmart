package com.desafio.hotmart.purchase;

import com.desafio.hotmart.coupon.CouponService;
import com.desafio.hotmart.coupon.CouponValidator;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import com.desafio.hotmart.purchase.validator.PurchaseHandlerValidator;
import com.desafio.hotmart.purchase.validator.ResultErrorResponse;
import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final ProductRepository productRepository;
    private final PurchaseHandlerValidator purchaseHandlerValidator;
    private final CouponService couponService;
    private final PurchaseService purchaseService;
    private final UserService userService;

    public PurchaseController(ProductRepository productRepository, PurchaseHandlerValidator purchaseHandlerValidator, CouponService couponService, PurchaseService purchaseService, UserService userService) {
        this.productRepository = productRepository;
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
            Optional<Product> possibleProduct = productRepository.findByCodeAndActiveIsTrue(request.productCode());
            if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

            User client = userService.getBy(request.email());
            Product product = possibleProduct.get();

            Optional<ResultErrorResponse> possibleErrorResponse = purchaseHandlerValidator.handler(product, client, request, smartPayment);
            if (possibleErrorResponse.isPresent()) {
                ResultErrorResponse resultErrorResponse = possibleErrorResponse.get();
                return ResponseEntity.unprocessableEntity().body(new PurchaseEventResultBody(resultErrorResponse.errorMessage()));
            }

            Optional<BigDecimal> discount = Optional.of(BigDecimal.ZERO);
            if (CouponValidator.isValid(coupon)) {
                discount = couponService.tryGetDiscount(coupon, product);
                if (discount.isEmpty()) return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
            }

            // PurchaseType.getByType(request.type()) processar no provedor

            purchaseService.save(request, client, product, discount.get(), smartPayment);

            return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase received")));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("payment failed")));
        }
    }
}