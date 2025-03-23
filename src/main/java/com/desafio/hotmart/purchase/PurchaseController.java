package com.desafio.hotmart.purchase;

import com.desafio.hotmart.clientPayout.PayoutRepository;
import com.desafio.hotmart.coupon.CouponService;
import com.desafio.hotmart.coupon.CouponValidator;
import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
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
    private final PurchaseValidator purchaseValidator;
    private final PixPurchaseRepository pixPurchaseRepository;
    private final PayoutRepository payoutRepository;
    private final CouponService couponService;
    private final PurchaseService purchaseService;

    public PurchaseController(UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, PurchaseValidator purchaseValidator, PixPurchaseRepository pixPurchaseRepository, PayoutRepository payoutRepository, CouponService couponService, PurchaseService purchaseService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseValidator = purchaseValidator;
        this.pixPurchaseRepository = pixPurchaseRepository;
        this.payoutRepository = payoutRepository;
        this.couponService = couponService;
        this.purchaseService = purchaseService;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest request, @RequestParam(required = false) String coupon,
                                    @RequestParam(required = false) boolean smartPayment) {
        Optional<Product> possibleProduct = productRepository.findByCodeAndActiveIsTrue(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Optional<User> possibleUser = userRepository.findByEmail(request.email());
        if (possibleUser.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        User client = possibleUser.get();
        if (!purchaseValidator.isValid(product, client, request, smartPayment)) {
            return ResponseEntity.unprocessableEntity().body(new ProductEventResultBody(purchaseValidator.getErrors()));
        }

        Optional<BigDecimal> discount = Optional.of(BigDecimal.ZERO);
        if (CouponValidator.isValid(coupon)) {
            discount = couponService.tryGetDiscount(coupon, product);
            if (discount.isEmpty()) return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
        }

        Purchase newPurchase = purchaseService.save(request, client, product, discount.get(), smartPayment);

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
                    .body(new GenericPaymentResponse<>(new PaymentResponseDTO("pix purchase has expired code")));
        }

        Purchase purchase = pixPurchase.confirmPayment();
        payoutRepository.updateStatusForConfirmedAndUpdatedAt(purchase.getId());

        pixPurchaseRepository.save(pixPurchase);
        purchaseRepository.save(purchase);

        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase confirmed")));
    }
}