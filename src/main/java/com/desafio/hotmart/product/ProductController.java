package com.desafio.hotmart.product;

import com.desafio.hotmart.Offer;
import com.desafio.hotmart.product.offer.OfferRepository;
import com.desafio.hotmart.user.User;
import com.desafio.hotmart.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final OfferRepository offerRepository;

    public ProductController(ProductRepository productRepository, UserRepository userRepository, ProductRequestValidator productRequestValidator, OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productRequestValidator = productRequestValidator;
        this.offerRepository = offerRepository;
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

    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseDTO>> list() {
        List<Product> products = productRepository.findByActiveTrue();
        return ResponseEntity.ok(ProductResponseDTO.convert(products));
    }

    @Transactional
    @PostMapping("/offer/add")
    public ResponseEntity<List<ProductResponseDTO>> addOffer(@RequestParam String productCode, @Valid @RequestBody OfferRequestDTO request) {
        Optional<Product> possibleProduct = productRepository.findByCode(productCode);
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        Offer offer = request.toOffer();
        product.addOffer(offer);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/offer/remove/{productCode}/{id}")
    public ResponseEntity<List<ProductResponseDTO>> removeOffer(@PathVariable String productCode, @PathVariable Long id) {
        Optional<Product> possibleProduct = productRepository.findByCode(productCode);
        if (possibleProduct.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Offer> possibleOffer = offerRepository.findById(id);
        if (possibleOffer.isEmpty()) return ResponseEntity.notFound().build();

        Product product = possibleProduct.get();
        product.removeOffer(possibleOffer.get());
        productRepository.save(product);

        return ResponseEntity.ok().build();
    }
}