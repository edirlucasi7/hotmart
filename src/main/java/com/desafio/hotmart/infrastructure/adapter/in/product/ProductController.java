package com.desafio.hotmart.infrastructure.adapter.in.product;

import com.desafio.hotmart.application.core.domain.product.Product;
import com.desafio.hotmart.application.core.domain.user.User;
import com.desafio.hotmart.application.port.PagePort;
import com.desafio.hotmart.application.shared.exception.ProductNotFoundException;
import com.desafio.hotmart.infrastructure.adapter.in.user.UserServicePort;
import com.desafio.hotmart.infrastructure.adapter.out.product.entity.ProductEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final UserServicePort userServicePort;
    private final ProductServicePort productServicePort;
    private final ProductRequestValidator productRequestValidator;

    public ProductController(UserServicePort userServicePort, ProductServicePort productServicePort, ProductRequestValidator productRequestValidator) {
        this.userServicePort = userServicePort;
        this.productServicePort = productServicePort;
        this.productRequestValidator = productRequestValidator;
    }

    @InitBinder("productRequest")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(productRequestValidator);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductEntity> create(@Valid @RequestBody ProductRequest request) {
        Optional<User> user = userServicePort.findByEmail(request.email());
        if (user.isEmpty()) return ResponseEntity.notFound().build();

        Product product = productServicePort.save(request.toProduct(user.get()));
        URI uri = URI.create("/product/" + product.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ProductView> get(@PathVariable Long id) {
        Optional<Product> possibleProduct = productServicePort.findById(id);
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        ProductView productView = ProductView.from(possibleProduct.get());
        return ResponseEntity.ok(productView);
    }

    @GetMapping("/list")
    public ResponseEntity<PagePort<ProductView>> list(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PagePort<ProductView> productsView = productServicePort.findAllByActiveTrue(page, size).map(ProductView::from);
        return ResponseEntity.ok(productsView);
    }

    @Transactional
    @PostMapping("/offer/add")
    public ResponseEntity<?> addOffer(@RequestParam String productCode, @Valid @RequestBody OfferRequest request) {
        try {
            productServicePort.addOffer(productCode, request.toOffer());
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException e) {
            // TODO essa não é a única exceção que pode ocorrer aqui
            return ResponseEntity.status(404).body(Map.of("error: ", e.getMessage()));
        }
    }
}