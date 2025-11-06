package com.example.desafio_tecnico_backend_ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AuthResponse {
    private final String token;
}
