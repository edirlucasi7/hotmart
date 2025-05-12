package com.desafio.hotmart.product;

import com.desafio.hotmart.purchase.response.GenericPaymentResponse;
import com.desafio.hotmart.purchase.response.PaymentResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/admin/product")
public class ProductAdminController {

    private final ProductRepository productRepository;

    public ProductAdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    @PutMapping("/{productId}/update/fee")
    public ResponseEntity<?> update(@PathVariable Long productId, @RequestParam("confirmationTime") BigDecimal fee) {
        Optional<Product> possibleProduct = productRepository.findById(productId);
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        product.updateFees(fee);
        return ResponseEntity.ok(new GenericPaymentResponse<>(new PaymentResponseDTO("fee updated")));
    }
}