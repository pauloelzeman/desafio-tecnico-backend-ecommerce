package com.example.desafio_tecnico_backend_ecommerce.controller;
import com.example.desafio_tecnico_backend_ecommerce.model.Product;
import com.example.desafio_tecnico_backend_ecommerce.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) { this.productRepository = productRepository; }

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> get(@PathVariable UUID id) {
        return productRepository.findById(id);
    }

    // ADMIN somente - criar
    @PostMapping
    public Product create(@RequestBody Product p, Authentication auth) {
        requireAdmin(auth);
        p.setId(UUID.randomUUID());
        return productRepository.save(p);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable UUID id, @RequestBody Product p, Authentication auth) {
        requireAdmin(auth);
        var existing = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setCategory(p.getCategory());
        existing.setQuantity(p.getQuantity());
        return productRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, Authentication auth) {
        requireAdmin(auth);
        productRepository.deleteById(id);
    }

    private void requireAdmin(Authentication auth) {
        if (auth == null || auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Privilégios de administrador necessários.");
        }
    }
}
