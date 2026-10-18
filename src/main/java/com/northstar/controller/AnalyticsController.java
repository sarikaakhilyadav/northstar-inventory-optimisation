package com.northstar.controller;

import com.northstar.dto.InventoryAnalyticsResponse;
import com.northstar.dto.InventoryPriorityResponse;
import com.northstar.service.InventoryService;
import com.northstar.dto.CategoryRevenueResponse;
import com.northstar.dto.InventoryRiskResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final InventoryService inventoryService;

    public AnalyticsController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public InventoryAnalyticsResponse getAnalytics() {
        return inventoryService.getInventoryAnalytics();
    }

    @GetMapping("/category-revenue")
    public List<CategoryRevenueResponse> getCategoryRevenue() {

        return inventoryService.getCategoryRevenue();
    }

    @GetMapping("/inventory-risks")
    public List<InventoryRiskResponse> getInventoryRisks() {

        return inventoryService.getInventoryRisks();
    }

    @GetMapping("/inventory-priorities")
    public List<InventoryPriorityResponse> getInventoryPriorities() {

        return inventoryService.getInventoryPriorities();
    }

}
