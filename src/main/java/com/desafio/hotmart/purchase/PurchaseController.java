package com.desafio.hotmart.purchase;

import com.desafio.hotmart.coupon.CouponService;
import com.desafio.hotmart.coupon.CouponValidator;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
import com.desafio.hotmart.product.ProductValidator;
import com.desafio.hotmart.purchase.errors.ProductEventResultBody;
import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import com.desafio.hotmart.purchase.response.PixPaymentResponseDTO;
import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductValidator productValidator;
    private final PixPurchaseRepository pixPurchaseRepository;
    private final CouponService couponService;

    public PurchaseController(UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, ProductValidator productValidator, PixPurchaseRepository pixPurchaseRepository, CouponService couponService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.productValidator = productValidator;
        this.pixPurchaseRepository = pixPurchaseRepository;
        this.couponService = couponService;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest request, @RequestParam(required = false) String coupon) {
        Optional<Product> possibleProduct = productRepository.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Optional<User> possibleUser = userRepository.findByEmail(request.email());
        if (possibleUser.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        User client = possibleUser.get();
        if (!productValidator.isValid(request, product, client)) {
            return ResponseEntity.unprocessableEntity().body(new ProductEventResultBody(productValidator.getErrors()));
        }

        Optional<BigDecimal> discount = Optional.of(BigDecimal.ZERO);
        if (CouponValidator.isValid(coupon)) {
            discount = couponService.tryGetDiscount(coupon, product);
            if (discount.isEmpty()) return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
        }

        Purchase newPurchase = request.toPurchaseWithDiscount(client, product, discount.get());
        purchaseRepository.save(newPurchase);

        if (PurchaseType.PIX == PurchaseType.getByName(request.type())) {
            PixPurchase pixPurchase = pixPurchaseRepository.save(PixPurchase.create(newPurchase, product.getConfirmationTime(), request.generatePixCode()));
            PixPaymentResponseDTO pixPaymentResponseDTO = new PixPaymentResponseDTO(pixPurchase.getCodeToPay(), "Payment confirmation must be made within %s minutes".formatted(product.getConfirmationTime()));
            return ResponseEntity.ok(new GenericPaymentResponse<>(pixPaymentResponseDTO));
        }

        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase received")));
    }

    @Transactional
    @PostMapping("/confirm/pix/code/{code}")
    public ResponseEntity<?> confirm(@PathVariable String code) {
        Optional<PixPurchase> byCodeToPay = pixPurchaseRepository.findByCodeToPay(code);
        if (byCodeToPay.isEmpty()) return ResponseEntity.notFound().build();

        PixPurchase pixPurchase = byCodeToPay.get();
        if (pixPurchase.shouldSkipConfirmation()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase has already been made or has an exired code or it's not a pix on hold")));
        }

        Purchase purchase = pixPurchase.confirmPayment();
        pixPurchaseRepository.save(pixPurchase);
        purchaseRepository.save(purchase);

        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase confirmed")));
    }
}