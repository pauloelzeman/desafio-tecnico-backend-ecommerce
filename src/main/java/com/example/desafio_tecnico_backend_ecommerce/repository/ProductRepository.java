package com.example.desafio_tecnico_backend_ecommerce.repository;

import com.example.desafio_tecnico_backend_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
