package com.example.desafio_tecnico_backend_ecommerce.repository;

import com.example.desafio_tecnico_backend_ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);
    // Busca pedidos do usuário com itens e produtos já carregados
    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN FETCH o.items i " +
           "JOIN FETCH i.product " +
           "WHERE o.user.id = :userId")
    List<Order> findAllByUserIdWithItems(UUID userId);
    // Exemplos de consultas otimizadas serão implementados em um repositório personalizado ou usando marcadores @Query fornecidos na camada de serviço.
}
