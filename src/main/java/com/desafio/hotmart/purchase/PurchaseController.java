package com.desafio.hotmart.purchase;

import com.desafio.hotmart.product.Product;
import com.desafio.hotmart.product.ProductRepository;
import com.desafio.hotmart.purchase.errors.ProductEventResultBody;
import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductValidator productValidator;

    public PurchaseController(UserRepository userRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, ProductValidator productValidator) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.productValidator = productValidator;
    }

    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PurchaseRequest request) {
        // um mesmo usuario nao pode comprar o mesmo produto mais de uma vez

        Optional<User> possibleUser = userRepository.findByEmail(request.email());
        if (possibleUser.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Product> possibleProduct = productRepository.findByCode(request.productCode());
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        User client = possibleUser.get();
        if (!productValidator.isValid(request, product, client)) {
            ProductEventResultBody body = new ProductEventResultBody(productValidator.getErrors());
            return ResponseEntity.unprocessableEntity().body(body);
        }

        Purchase newPurchase = request.toPurchase(client, product);
        purchaseRepository.save(newPurchase);

        return ResponseEntity.ok("purchase received");
    }
}