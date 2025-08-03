package com.desafio.hotmart.infrastructure.adapter.in.purchase;

import com.desafio.hotmart.application.core.domain.coupon.validator.CouponValidator;
import com.desafio.hotmart.application.core.service.coupon.CouponServicePort;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseServicePort purchaseServicePort;
    private final CouponServicePort couponServicePort;

    public PurchaseController(PurchaseServicePort purchaseServicePort, CouponServicePort couponServicePort) {
        this.purchaseServicePort = purchaseServicePort;
        this.couponServicePort = couponServicePort;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest request, @RequestParam(required = false) String coupon,
                                    @RequestParam(required = false) boolean smartPayment) throws ProductNotFoundException {

        Optional<BigDecimal> discount = Optional.of(BigDecimal.ZERO);
        if (CouponValidator.isValid(coupon)) {
            discount = couponServicePort.tryGetDiscount(coupon, request.productCode());
            if (discount.isEmpty()) return ResponseEntity.unprocessableEntity()
                    .body(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon not valid")));
        }

        // PurchaseType.getByType(request.type()) processar no provedor de pagamento

        purchaseServicePort.save(request, discount.get(), smartPayment);

        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("purchase received")));
    }
}