
public class InventoryReport {

    public void displayFinancialSummary(InventoryService service) {

        System.out.println("\n===== FINANCIAL SUMMARY =====");

        System.out.printf(
                "Electronics Revenue: £%.2f%n",
                service.calculateRevenueByCategory("Electronics"));

        System.out.printf(
                "Accessories Revenue: £%.2f%n",
                service.calculateRevenueByCategory("Accessories"));

        System.out.printf(
                "Office Equipment Revenue: £%.2f%n",
                service.calculateRevenueByCategory("Office Equipment"));

        System.out.printf(
                "Total Revenue Generated: £%.2f%n",
                service.calculateTotalRevenue());

        System.out.printf(
                "Total Potential Profit: £%.2f%n",
                service.calculateTotalPotentialProfit());

        System.out.printf(
                "Total Inventory Cost: £%.2f%n",
                service.calculateTotalInventoryCost());

        Product highestProfitProduct = service.findHighestProfitPerUnitProduct();

        if (highestProfitProduct != null) {
            System.out.printf(
                    "Highest Profit Per Unit: £%.2f%n",
                    highestProfitProduct.calculateProfitPerUnit());

            System.out.println(
                    "Highest Profit Product: " + highestProfitProduct.getProductName());
        }

        Product highestTotalProfitProduct = service.findHighestTotalSalesProfitProduct();

        if (highestTotalProfitProduct != null) {
            System.out.printf(
                    "Highest Total Sales Profit: £%.2f%n",
                    highestTotalProfitProduct.calculateTotalSalesProfit());

            System.out.println(
                    "Product with Highest Total Sales Profit: "
                            + highestTotalProfitProduct.getProductName());
        }
    }

    public void displayProfitabilityRanking(InventoryService service) {

        System.out.println("\n===== PRODUCT PROFITABILITY RANKING =====");

        int rank = 1;

        for (Product product : service.getProductsByTotalSalesProfit()) {

            System.out.printf(
                    "%d. %s - £%.2f%n",
                    rank,
                    product.getProductName(),
                    product.calculateTotalSalesProfit());

            rank++;
        }
    }

    public void displayInventorySummary(InventoryService service) {

        System.out.println("\n===== INVENTORY SUMMARY REPORT =====");

        int totalProducts = service.getProducts().size();
        int productsAtRisk = service.countProductsAtRisk();

        double riskPercentage = totalProducts == 0
                ? 0
                : (double) productsAtRisk / totalProducts * 100;

        System.out.println("Total Products: " + totalProducts);
        System.out.println("Products Needing Reorder: "
                + service.countProductsNeedingReorder());
        System.out.println("Critical Stock Products: "
                + service.countCriticalStockProducts());
        System.out.println("Slow-Moving Products: "
                + service.countSlowMovingProducts());

        System.out.printf(
                "Products At Risk: %d (%.1f%%)%n",
                productsAtRisk,
                riskPercentage);
    }

}
