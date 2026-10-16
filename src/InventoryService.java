import java.util.ArrayList;
import java.util.Comparator;

public class InventoryService {

    private ArrayList<Product> products;

    public InventoryService(ArrayList<Product> products) {
        this.products = products;
    }

    public int countProductsNeedingReorder() {
        int count = 0;

        for (Product product : products) {
            if (product.isReorderRequired()) {
                count++;
            }
        }

        return count;
    }

    public int countSlowMovingProducts() {
        int count = 0;

        for (Product product : products) {
            if (product.isSlowMoving()) {
                count++;
            }
        }

        return count;
    }

    public int countCriticalStockProducts() {
        int count = 0;

        for (Product product : products) {
            if (product.getInventoryRiskStatus().equals("CRITICAL STOCK")) {
                count++;
            }
        }

        return count;
    }

    public int countProductsAtRisk() {
        int count = 0;

        for (Product product : products) {
            if (!product.getInventoryRiskStatus().equals("HEALTHY STOCK")) {
                count++;
            }
        }

        return count;
    }

    public double calculateTotalRevenue() {
        double total = 0;

        for (Product product : products) {
            total += product.calculateRevenueGenerated();
        }

        return total;
    }

    public double calculateTotalPotentialProfit() {
        double total = 0;

        for (Product product : products) {
            total += product.calculatePotentialProfit();
        }

        return total;
    }

    public double calculateTotalInventoryCost() {
        double total = 0;

        for (Product product : products) {
            total += product.calculateInventoryCost();
        }

        return total;
    }

    public double calculateRevenueByCategory(String category) {
        double total = 0;

        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                total += product.calculateRevenueGenerated();
            }
        }

        return total;
    }

    public Product findHighestProfitPerUnitProduct() {
        Product highestProfitProduct = null;

        for (Product product : products) {
            if (highestProfitProduct == null
                    || product.calculateProfitPerUnit() > highestProfitProduct.calculateProfitPerUnit()) {
                highestProfitProduct = product;
            }
        }

        return highestProfitProduct;
    }

    public Product findHighestTotalSalesProfitProduct() {
        Product highestProfitProduct = null;

        for (Product product : products) {
            if (highestProfitProduct == null
                    || product.calculateTotalSalesProfit() > highestProfitProduct.calculateTotalSalesProfit()) {
                highestProfitProduct = product;
            }
        }

        return highestProfitProduct;
    }

    public ArrayList<Product> getProductsByTotalSalesProfit() {

        ArrayList<Product> rankedProducts = new ArrayList<>(products);

        rankedProducts.sort(
                Comparator.comparingDouble(Product::calculateTotalSalesProfit)
                        .reversed());

        return rankedProducts;
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public Product findProductById(int productId) {

        for (Product product : products) {

            if (product.getProductId() == productId) {
                return product;
            }
        }

        return null;
    }

    public void addProduct(Product product) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null.");
        }

        if (findProductById(product.getProductId()) != null) {
            throw new IllegalArgumentException(
                    "A product with this ID already exists.");
        }

        products.add(product);
    }

    public boolean removeProductById(int productId) {

        Product product = findProductById(productId);

        if (product == null) {
            return false;
        }

        products.remove(product);
        return true;
    }

}
