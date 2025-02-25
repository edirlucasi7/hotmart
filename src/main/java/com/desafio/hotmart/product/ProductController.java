package com.desafio.hotmart.product;

import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductRequestValidator productRequestValidator;

    public ProductController(ProductRepository productRepository, UserRepository userRepository, ProductRequestValidator productRequestValidator) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productRequestValidator = productRequestValidator;
    }

    @InitBinder("productRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(productRequestValidator);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isEmpty()) return ResponseEntity.notFound().build();

        Product product = request.toProduct(user.get());
        productRepository.save(product);
        return ResponseEntity.ok().build();
    }
}