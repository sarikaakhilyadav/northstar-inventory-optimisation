package com.northstar.dto;

public record InventoryPriorityResponse(

        int productId,

        String productName,

        String category,

        int currentStock,

        int reorderLevel,

        int unitsSold,

        String priority,

        String reason

) {
}
