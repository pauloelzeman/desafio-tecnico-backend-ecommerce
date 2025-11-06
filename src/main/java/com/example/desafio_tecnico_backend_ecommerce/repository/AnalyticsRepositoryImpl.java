package com.example.desafio_tecnico_backend_ecommerce.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class AnalyticsRepositoryImpl implements AnalyticsRepository {

    private final EntityManager em;

    public AnalyticsRepositoryImpl(EntityManager em) { this.em = em; }

    @Override
    public List<Map<String, Object>> top5UsersBySpent() {
        String sql = "SELECT u.username, SUM(o.total) as total_spent FROM orders o JOIN users u ON o.user_id = u.id GROUP BY u.username ORDER BY total_spent DESC LIMIT 5";
        Query q = em.createNativeQuery(sql);
        List<Object[]> rows = q.getResultList();
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] r : rows) {
            out.add(Map.of("username", r[0], "total_spent", r[1]));
        }
        return out;
    }

    @Override
    public List<Map<String, Object>> avgTicketPerUser() {
        String sql = "SELECT u.username, AVG(o.total) as avg_ticket FROM orders o JOIN users u ON o.user_id = u.id GROUP BY u.username";
        Query q = em.createNativeQuery(sql);
        List<Object[]> rows = q.getResultList();
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] r : rows) {
            out.add(Map.of("username", r[0], "avg_ticket", r[1]));
        }
        return out;
    }

    @Override
    public BigDecimal totalRevenueForMonth(int year, int month) {
        String sql = "SELECT COALESCE(SUM(o.total),0) FROM orders o WHERE YEAR(o.created_at)=:y AND MONTH(o.created_at)=:m AND o.status='PAGO'";
        Query q = em.createNativeQuery(sql);
        q.setParameter("y", year);
        q.setParameter("m", month);
        Object single = q.getSingleResult();
        if (single == null) return BigDecimal.ZERO;
        return new BigDecimal(single.toString());
    }
}
