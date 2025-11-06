package com.example.desafio_tecnico_backend_ecommerce.controller;

import com.example.desafio_tecnico_backend_ecommerce.dto.CreateOrderRequest;
import com.example.desafio_tecnico_backend_ecommerce.model.*;
import com.example.desafio_tecnico_backend_ecommerce.repository.OrderRepository;
import com.example.desafio_tecnico_backend_ecommerce.repository.ProductRepository;
import com.example.desafio_tecnico_backend_ecommerce.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderController(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest req, Authentication auth) {
        UUID userId = UUID.fromString(auth.getPrincipal().toString());
        var user = userRepository.findById(userId).orElseThrow();
        // Criar itens e calcular o total com base no preço atual
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // Cria o pedido antes para poder associar aos itens
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .user(user)
                .status(OrderStatus.PENDENTE)
                .build();
        for (var it : req.getItems()) {
            var prod = productRepository.findById(it.getProductId()).orElseThrow(() -> new RuntimeException("Produto não encontrado: " + it.getProductId()));
            OrderItem oi = OrderItem.builder()
                    .id(UUID.randomUUID())
                    .product(prod)
                    .quantity(it.getQuantity())
                    .priceAtPurchase(prod.getPrice())
                    .order(order)
                    .build();
            items.add(oi);
            total = total.add(prod.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
        }
        order.setItems(items);
        order.setTotal(total);
        return orderRepository.save(order);
    }

    @PostMapping("/{orderId}/pay")
    public Map<String, Object> pay(@PathVariable UUID orderId, Authentication auth) {
        UUID userId = UUID.fromString(auth.getPrincipal().toString());
        var order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
        if (!order.getUser().getId().equals(userId)) throw new RuntimeException("Não é possível pagar o pedido de outro usuário.");
        if (order.getStatus() != OrderStatus.PENDENTE) throw new RuntimeException("Pedido já processado.");
        // Verificar estoque
        for (OrderItem it : order.getItems()) {
            if (it.getProduct().getQuantity() < it.getQuantity()) {
                order.setStatus(OrderStatus.CANCELADO);
                orderRepository.save(order);
                return Map.of("status", "CANCELADO", "message", "Produto sem estoque: " + it.getProduct().getName());
            }
        }
        // Reduzir o estoque e marcar como pago
        for (OrderItem it : order.getItems()) {
            var p = it.getProduct();
            p.setQuantity(p.getQuantity() - it.getQuantity());
            productRepository.save(p);
        }
        order.setStatus(OrderStatus.PAGO);
        orderRepository.save(order);
        return Map.of("status", "PAGO", "total", order.getTotal());
    }

    @GetMapping("/my")
    public List<Order> listByUser(Authentication auth) {
        UUID userId = UUID.fromString(auth.getPrincipal().toString());
        return orderRepository.findAllByUserIdWithItems(userId);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable UUID id, Authentication auth) {
        UUID userId = UUID.fromString(auth.getPrincipal().toString());
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado a este pedido.");
        }
        return order;
    }
}
