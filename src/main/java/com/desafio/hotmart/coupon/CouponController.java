package com.desafio.hotmart.coupon;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    public CouponController(ProductRepository productRepository, CouponRepository couponRepository) {
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
    }

    // TODO entender como seria a unicidade de um cupon, quando chega um cadastro para um code que já existe, faz oq?
    // TODO atualiza o cupom com aquele código e muda as datas? ou deixa duplicar code para o mesmo produto e cria estado de invalido e valido, deixando somente um valido por porduto? nao sei ainda!
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CouponRequest request) {
        Optional<Product> possibleProduct = productRepository.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Coupon coupon = request.toCoupon(possibleProduct.get());
        couponRepository.save(coupon);
        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("coupon created successfully")));
    }
}
