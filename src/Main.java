import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {

        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.00,
                650.00,
                8,
                10,
                42);

        Product mouse = new Product(
                102,
                "Wireless Mouse",
                "Accessories",
                10,
                20,
                50,
                20,
                120);

        Product keyboard = new Product(
                103,
                "Mechanical Keyboard",
                "Accessories",
                40,
                80,
                5,
                10,
                150);

        Product monitor = new Product(
                104,
                "24-inch Monitor",
                "Electronics",
                120,
                180,
                30,
                10,
                200);

        Product printer = new Product(
                105,
                "Office Printer",
                "Office Equipment",
                100,
                160,
                25,
                10,
                20);

        ArrayList<Product> products = new ArrayList<>();

        products.add(laptop);
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(printer);

        double totalRevenueGenerated = 0;
        double totalPotentialProfit = 0;
        double totalInventoryCost = 0;

        double electronicsRevenue = 0;
        double accessoriesRevenue = 0;
        double officeEquipmentRevenue = 0;

        double highProfitPerUnit = 0;

        Product hightProfitProduct = null;

        double highestTotalProfit = 0;
        Product highestTotalProfitProduct = null;

        for (Product product : products) {

            if (product.isReorderRequired()) {
                System.out.println(product.getProductName() + " needs reordering");
            }

            if (product.isSlowMoving()) {
                System.out.println(product.getProductName() + " is slow-moving");
            }

            if (product.getCategory().equals("Electronics")) {
                electronicsRevenue = electronicsRevenue + product.calculateRevenueGenerated();
            }

            if (product.getCategory().equals("Accessories")) {
                accessoriesRevenue = accessoriesRevenue + product.calculateRevenueGenerated();
            }

            if (product.getCategory().equals("Office Equipment")) {
                officeEquipmentRevenue = officeEquipmentRevenue + product.calculateRevenueGenerated();
            }

            System.out
                    .println(product.getProductName() + " : £" + product.calculateProfitPerUnit() + " profit per unit");

            if (product.calculateProfitPerUnit() > highProfitPerUnit) {
                highProfitPerUnit = product.calculateProfitPerUnit();
                hightProfitProduct = product;
            }

            double totalProfit = product.calculateProfitPerUnit() * product.getUnitsSold();

            if (totalProfit > highestTotalProfit) {
                highestTotalProfit = totalProfit;
                highestTotalProfitProduct = product;
            }

            System.out.println(product.getProductName() + " : " + product.getReorderRecommendation());

            System.out.println(product.getProductName() + " : " + product.getInventoryRiskStatus());

            totalRevenueGenerated = totalRevenueGenerated + product.calculateRevenueGenerated();

            totalPotentialProfit = totalPotentialProfit + product.calculatePotentialProfit();

            totalInventoryCost = totalInventoryCost + product.calculateInventoryCost();

        }

        System.out.println("Electronics Revenue: " + electronicsRevenue);

        System.out.println("Accessories Revenue: " + accessoriesRevenue);

        System.out.println("Office Equipment Revenue: " + officeEquipmentRevenue);

        System.out.println("Total Revenue Generated: " + totalRevenueGenerated);

        System.out.println("Total Potential Profit: " + totalPotentialProfit);

        System.out.println("Total Inventory Cost: " + totalInventoryCost);

        System.out.println("Highest Profit Per Unit: £" + highProfitPerUnit);

        System.out.println("Highest Profit Product: " + hightProfitProduct.getProductName());

        System.out.println("Highest Total Sales Profit: £" + highestTotalProfit);

        System.out.println("Product with Highest Total Sales Profit: " + highestTotalProfitProduct.getProductName());

        // Rank products by total sales profit
        products.sort(
                Comparator.comparingDouble(
                        (Product product) -> product.calculateProfitPerUnit() * product.getUnitsSold()).reversed());

        System.out.println("\n===== PRODUCT PROFITABILITY RANKING =====");

        int rank = 1;

        for (Product product : products) {
            double totalProfit = product.calculateProfitPerUnit() * product.getUnitsSold();

            System.out.println(
                    rank + ". " + product.getProductName()
                            + " - £" + totalProfit);

            rank++;
        }

    }
}
