package com.example.service;

import com.example.model.SalesPoint;
import com.example.repository.SalesPointRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class SalesPointService {
    private final SalesPointRepository salesPointRepository;

    public SalesPointService(SalesPointRepository salesPointRepository) {
        this.salesPointRepository = salesPointRepository;
    }

    public SalesPoint createSalesPoint(SalesPoint salesPoint) {
        salesPoint.setId(UUID.randomUUID().toString());
        return salesPointRepository.save(salesPoint);
    }

    public SalesPoint updateSalesPoint(String id, SalesPoint salesPoint) {
        salesPoint.setId(id);
        return salesPointRepository.save(salesPoint);
    }

    public SalesPoint getSalesPoint(String id) {
        return salesPointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales Point not found"));
    }

    public List<SalesPoint> getAllSalesPoints() {
        return salesPointRepository.findAll();
    }

    public void deleteSalesPoint(String id) {
        salesPointRepository.deleteById(id);
    }
}