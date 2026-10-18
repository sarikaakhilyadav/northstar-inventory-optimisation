package com.northstar.dto;

public record InventoryAnalyticsResponse(

        int totalProducts,

        double totalRevenue,

        double totalInventoryCost,

        double totalPotentialProfit,

        long productsRequiringReorder,

        long slowMovingProducts

) {
}