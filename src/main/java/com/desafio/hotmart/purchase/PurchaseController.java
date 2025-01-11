package com.desafio.hotmart.purchase;

import com.desafio.hotmart.coupon.CouponService;
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
            ProductEventResultBody body = new ProductEventResultBody(productValidator.getErrors());
            return ResponseEntity.unprocessableEntity().body(body);
        }

        Optional<BigDecimal> discountAmount = couponService.tryGetDiscount(coupon, product);
        if (couponService.exists(coupon) && discountAmount.isEmpty()) {
            return ResponseEntity.unprocessableEntity().body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
        }

        Purchase newPurchase = discountAmount
                .map(discount -> request.toPurchaseWithDiscount(client, product, discount))
                .orElseGet(() -> request.toPurchase(client, product));
        purchaseRepository.save(newPurchase);

        // TODO o confirmation time precisa ter uma regra mais clara
        // TODO ele deveria deveria ser definido no contexto geral da aplicação? e configurável por quem? dono do produto ou dev?
        // TODO se for dono do produto, deveria ser customizável, se for dev é para a aplicação inteira? não sei ainda!
        if (PurchaseType.PIX == PurchaseType.getByName(request.type())) {
            PixPurchase pixPurchase = pixPurchaseRepository.save(PixPurchase.create(newPurchase, request.confirmationTime(), "pixtopay"));
            PixPaymentResponseDTO pixPaymentResponseDTO = new PixPaymentResponseDTO(pixPurchase.getCodeToPay(), "Payment confirmation must be made within %s minutes".formatted(request.confirmationTime()));
            return ResponseEntity.ok(new GenericPaymentResponse<>(pixPaymentResponseDTO));
        }

        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase received")));
    }

    @Transactional
    @PutMapping("/update/pix/code/{code}")
    public ResponseEntity<?> update(@PathVariable String code, @RequestParam("confirmationTime") Integer confirmationTime) {
        Optional<PixPurchase> byCodeToPay = pixPurchaseRepository.findByCodeToPay(code);
        if (byCodeToPay.isEmpty()) return ResponseEntity.notFound().build();

        PixPurchase pixPurchase = byCodeToPay.get();
        if (pixPurchase.isConfirmed()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase has already been made")));
        }

        pixPurchase.updateConfirmationTime(confirmationTime);
        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("confirmationTime updated")));
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