package com.example.desafio_tecnico_backend_ecommerce.repository;

import com.example.desafio_tecnico_backend_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
