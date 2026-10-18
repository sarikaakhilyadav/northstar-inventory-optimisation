package com.northstar.dto;

public record InventoryRiskResponse(

        int productId,

        String productName,

        String category,

        int currentStock,

        int reorderLevel,

        int unitsSold,

        String riskStatus,

        String riskExplanation,

        String recommendation

) {
}
