package com.example.desafio_tecnico_backend_ecommerce.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class CreateOrderRequest {
    private List<Item> items;
    @Getter @Setter
    public static class Item {
        private UUID productId;
        private Integer quantity;
    }
}
