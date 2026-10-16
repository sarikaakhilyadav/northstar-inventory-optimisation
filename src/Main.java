import java.util.ArrayList;

public class Main {
        public static void main(String[] args) {

                // ==================================================
                // 1. CREATE SAMPLE PRODUCTS
                // ==================================================

                // Create sample products with their prices, stock levels and sales data.

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

                // ==================================================
                // 2. STORE PRODUCTS IN A COLLECTION
                // ==================================================

                // Store all products in one collection for analysis.

                ArrayList<Product> products = new ArrayList<>();

                products.add(laptop);
                products.add(mouse);
                products.add(keyboard);
                products.add(monitor);
                products.add(printer);

                // Create the service responsible for inventory analysis.

                InventoryService service = new InventoryService(products);

                Product newProduct = new Product(
                                106, "USB-C Hub", "Accessories",
                                15, 30, 25, 10, 60);

                service.addProduct(newProduct);

                System.out.println("Product added: " + newProduct.getProductName());
                System.out.println("Total products: " + service.getProducts().size());

                Product foundProduct = service.findProductById(999);

                if (foundProduct != null) {
                        System.out.println("Product found: " + foundProduct.getProductName());
                } else {
                        System.out.println("Product not found.");
                }

                InventoryReport report = new InventoryReport();

                // ========================================
                // 6. ANALYSE EACH PRODUCT
                // ========================================

                for (Product product : products) {

                        // Calculate reusable product metrics once.

                        double profitPerUnit = product.calculateProfitPerUnit();

                        String riskStatus = product.getInventoryRiskStatus();

                        // Check whether each product needs reordering or is slow-moving.

                        if (product.isReorderRequired()) {
                                System.out.println(product.getProductName() + " needs reordering");
                        }

                        if (product.isSlowMoving()) {
                                System.out.println(product.getProductName() + " is slow-moving");
                        }

                        // Display the product's profit per unit.

                        System.out.printf(
                                        "%s: £%.2f profit per unit%n",
                                        product.getProductName(),
                                        profitPerUnit);

                        // Display the recommended action and current stock risk status.

                        System.out.println(product.getProductName() + " : " + product.getReorderRecommendation());

                        System.out.println(product.getProductName() + " : " + riskStatus);

                }

                // ==================================================
                // 7. DISPLAY FINANCIAL SUMMARY
                // ==================================================

                report.displayFinancialSummary(service);

                // ==================================================
                // 8. DISPLAY PRODUCT PROFITABILITY RANKING
                // ==================================================

                report.displayProfitabilityRanking(service);

                // ==================================================
                // 9. DISPLAY INVENTORY SUMMARY REPORT
                // ==================================================

                report.displayInventorySummary(service);

        }
}
