package com.example.desafio_tecnico_backend_ecommerce.controller;

import com.example.desafio_tecnico_backend_ecommerce.repository.AnalyticsRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsController(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @GetMapping("/top5-users")
    public List<Map<String,Object>> top5Users() {
        return analyticsRepository.top5UsersBySpent();
    }

    @GetMapping("/avg-ticket")
    public List<Map<String,Object>> avgTicketPerUser() {
        return analyticsRepository.avgTicketPerUser();
    }

    @GetMapping("/revenue/{year}/{month}")
    public BigDecimal revenue(@PathVariable int year, @PathVariable int month) {
        return analyticsRepository.totalRevenueForMonth(year, month);
    }
}
