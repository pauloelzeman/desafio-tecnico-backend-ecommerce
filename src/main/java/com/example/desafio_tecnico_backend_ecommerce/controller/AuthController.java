package com.example.desafio_tecnico_backend_ecommerce.controller;

import com.example.desafio_tecnico_backend_ecommerce.dto.AuthRequest;
import com.example.desafio_tecnico_backend_ecommerce.dto.AuthResponse;
import com.example.desafio_tecnico_backend_ecommerce.model.User;
import com.example.desafio_tecnico_backend_ecommerce.repository.UserRepository;
import com.example.desafio_tecnico_backend_ecommerce.security.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("O nome de usuário já existe.");
        }
        User u = User.builder()
                .id(UUID.randomUUID())
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(com.example.desafio_tecnico_backend_ecommerce.model.Role.ROLE_USER)
                .build();
        userRepository.save(u);
        String token = jwtUtils.generateToken(u.getId().toString(), u.getRole().name());
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        var maybe = userRepository.findByUsername(req.getUsername());
        if (maybe.isEmpty()) throw new RuntimeException("Credenciais inválidas.");
        var u = maybe.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) throw new RuntimeException("Credenciais inválidas.");
        String token = jwtUtils.generateToken(u.getId().toString(), u.getRole().name());
        return new AuthResponse(token);
    }
}
