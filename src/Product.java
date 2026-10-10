public class Product {

    private int productId;
    private String productName;
    private String category;
    private double costPrice;
    private double sellingPrice;
    private int currentStock;
    private int reorderLevel;
    private int unitsSold;

    public Product(int productId, String productName, String category, double costPrice, double sellingPrice,
            int currentStock, int reorderLevel, int unitsSold) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.unitsSold = unitsSold;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(int unitsSold) {
        this.unitsSold = unitsSold;
    }

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

}