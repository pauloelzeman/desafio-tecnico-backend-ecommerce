package com.example.desafio_tecnico_backend_ecommerce.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AnalyticsRepository {
    List<Map<String,Object>> top5UsersBySpent();
    List<Map<String,Object>> avgTicketPerUser();
    BigDecimal totalRevenueForMonth(int year, int month);
}
