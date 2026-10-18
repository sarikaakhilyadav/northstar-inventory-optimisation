package com.northstar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    // Product details and inventory data
    @Id
    private int productId;
    private String productName;
    private String category;
    private double costPrice;
    private double sellingPrice;
    private int currentStock;
    private int reorderLevel;
    private int unitsSold;

    // Initialise a product with its details, prices and stock information.

    protected Product() {
        // Required by JPA when loading products from the database.
    }

    public Product(int productId, String productName, String category, double costPrice, double sellingPrice,
            int currentStock, int reorderLevel, int unitsSold) {

        if (productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Product category cannot be empty.");
        }

        if (costPrice < 0 || sellingPrice < 0) {
            throw new IllegalArgumentException("Product prices cannot be negative.");
        }

        if (currentStock < 0 || reorderLevel < 0 || unitsSold < 0) {
            throw new IllegalArgumentException("Stock, reorder level and units sold cannot be negative.");
        }

        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.unitsSold = unitsSold;
    }

    // Getters and setters for accessing and updating product data.

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

    // Inventory checks, recommendations and risk assessment.

    public boolean isReorderRequired() {
        if (currentStock < reorderLevel) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSlowMoving() {
        return unitsSold < 50;
    }

    public String getReorderRecommendation() {
        if (isReorderRequired() && isSlowMoving()) {
            return "Review before reordering";
        } else if (isReorderRequired()) {
            return "Reorder";
        } else {
            return "No reorder needed";
        }
    }

    public String getInventoryRiskStatus() {
        if (currentStock <= reorderLevel * 0.5) {
            return "CRITICAL STOCK";
        } else if (currentStock < reorderLevel) {
            return "REORDER REQUIRED";
        } else {
            return "HEALTHY STOCK";
        }
    }

    // Financial calculations for revenue, profit and inventory value.

    public double calculateRevenueGenerated() {
        return sellingPrice * unitsSold;
    }

    public double calculateProfitPerUnit() {
        return sellingPrice - costPrice;
    }

    public double calculatePotentialProfit() {
        return calculateProfitPerUnit() * currentStock;
    }

    public double calculateInventoryCost() {
        return costPrice * currentStock;
    }

    public double calculateTotalSalesProfit() {
        return calculateProfitPerUnit() * unitsSold;
    }

    public String getInventoryRiskExplanation() {
        if (currentStock <= reorderLevel * 0.5) {
            return "Stock is critically low. Replenishment should be prioritised.";
        }

        if (currentStock < reorderLevel) {
            return "Stock is below the reorder level. Consider replenishment.";
        }

        return "Stock is at or above the reorder level.";
    }

}