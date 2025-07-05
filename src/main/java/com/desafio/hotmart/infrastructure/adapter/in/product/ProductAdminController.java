package com.desafio.hotmart.infrastructure.adapter.in.product;

import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.infrastructure.adapter.out.product.repository.ProductEntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/admin/product")
public class ProductAdminController {

    private final ProductEntityRepository productEntityRepository;

    public ProductAdminController(ProductEntityRepository productEntityRepository) {
        this.productEntityRepository = productEntityRepository;
    }

    @Transactional
    @PutMapping("/{productId}/update/fee")
    public ResponseEntity<Void> update(@PathVariable Long productId, @RequestParam("confirmationTime") BigDecimal fee) {
        Optional<ProductEntity> possibleProduct = productEntityRepository.findById(productId);
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        ProductEntity productEntity = possibleProduct.get();
        productEntity.updateFees(fee);
        return ResponseEntity.ok().build();
    }
}