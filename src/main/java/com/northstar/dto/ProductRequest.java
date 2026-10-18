package com.northstar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(

        @NotNull(message = "Product ID is required") @Positive(message = "Product ID must be positive") Integer productId,

        @NotBlank(message = "Product name cannot be empty") String productName,

        @NotBlank(message = "Category cannot be empty") String category,

        @PositiveOrZero(message = "Cost price cannot be negative") double costPrice,

        @PositiveOrZero(message = "Selling price cannot be negative") double sellingPrice,

        @Min(value = 0, message = "Current stock cannot be negative") int currentStock,

        @Min(value = 0, message = "Reorder level cannot be negative") int reorderLevel,

        @Min(value = 0, message = "Units sold cannot be negative") int unitsSold

) {
}
