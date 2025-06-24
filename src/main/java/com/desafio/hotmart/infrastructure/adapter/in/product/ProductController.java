package com.desafio.hotmart.infrastructure.adapter.in.product;

import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import com.desafio.hotmart.product.OfferRequestDTO;
import com.desafio.hotmart.product.ProductRequest;
import com.desafio.hotmart.product.ProductRequestValidator;
import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final UserRepository userRepository;
    private final ProductServicePort productServicePort;
    private final ProductRequestValidator productRequestValidator;

    public ProductController(UserRepository userRepository, ProductServicePort productServicePort, ProductRequestValidator productRequestValidator) {
        this.userRepository = userRepository;
        this.productServicePort = productServicePort;
        this.productRequestValidator = productRequestValidator;
    }

    @InitBinder("productRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(productRequestValidator);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductEntity> create(@Valid @RequestBody ProductRequest request) {
        Optional<User> user = userRepository.findByEmail_Email(request.email());
        if (user.isEmpty()) return ResponseEntity.notFound().build();

        ProductEntity productEntity = request.toProduct(user.get());
        productServicePort.save(productEntity.toProduct());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<PagePort<ProductView>> list(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PagePort<ProductView> productsView = productServicePort.findAllByActiveTrue(page, size).map(ProductView::from);
        return ResponseEntity.ok(productsView);
    }

    @Transactional
    @PostMapping("/offer/add")
    public ResponseEntity<Void> addOffer(@RequestParam String productCode, @Valid @RequestBody OfferRequestDTO request) throws ProductNotFoundException {
        productServicePort.addOffer(productCode, request);
        return ResponseEntity.ok().build();
    }
}