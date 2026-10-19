package com.northstar.service;

import com.northstar.model.Product;
import com.northstar.dto.ProductRequest;
import com.northstar.repository.ProductRepository;
import com.northstar.dto.RestockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.northstar.dto.SaleRequest;
import com.northstar.dto.CategoryRevenueResponse;
import com.northstar.dto.InventoryRiskResponse;
import java.util.Map;
import java.util.LinkedHashMap;
import com.northstar.dto.InventoryAnalyticsResponse;
import com.northstar.dto.InventoryPriorityResponse;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

@Service
public class InventoryService {

    private ProductRepository productRepository;
    private ArrayList<Product> products;

    @Autowired
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = new ArrayList<>();
    }

    public InventoryService() {
        this.products = new ArrayList<>();

        this.products.add(new Product(
                1, "Laptop", "Electronics",
                450.0, 650.0, 8, 10, 42));

        this.products.add(new Product(
                2, "Wireless Mouse", "Accessories",
                10.0, 20.0, 50, 20, 120));

        this.products.add(new Product(
                3, "Mechanical Keyboard", "Accessories",
                40.0, 80.0, 5, 10, 150));

        this.products.add(new Product(
                4, "24-inch Monitor", "Electronics",
                120.0, 180.0, 30, 10, 200));

        this.products.add(new Product(
                5, "Office Printer", "Office Equipment",
                100.0, 160.0, 25, 10, 20));
    }

    public InventoryService(ArrayList<Product> products) {
        this.products = products;
    }

    public Product createProduct(ProductRequest request) {

        Product product = new Product(
                request.productName(),
                request.category(),
                request.costPrice(),
                request.sellingPrice(),
                request.currentStock(),
                request.reorderLevel(),
                request.unitsSold());

        return productRepository.save(product);
    }

    public Product updateProduct(int productId, ProductRequest request) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId));

        existingProduct.setProductName(request.productName());
        existingProduct.setCategory(request.category());
        existingProduct.setCostPrice(request.costPrice());
        existingProduct.setSellingPrice(request.sellingPrice());
        existingProduct.setCurrentStock(request.currentStock());
        existingProduct.setReorderLevel(request.reorderLevel());
        existingProduct.setUnitsSold(request.unitsSold());

        return productRepository.save(existingProduct);
    }

    public void deleteProductFromDatabase(int productId) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId));

        productRepository.delete(existingProduct);
    }

    public Product getProductFromDatabaseById(int productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId));
    }

    public Product restockProductInDatabase(
            int productId,
            RestockRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId));

        int quantity = request.quantity();

        if (quantity > Integer.MAX_VALUE - product.getCurrentStock()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Restock quantity exceeds the maximum allowed stock");
        }

        product.setCurrentStock(
                product.getCurrentStock() + quantity);

        return productRepository.save(product);
    }

    @Transactional
    public Product recordSaleInDatabase(
            int productId,
            SaleRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + productId));

        int quantity = request.quantity();

        if (quantity > product.getCurrentStock()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient stock. Available: "
                            + product.getCurrentStock());
        }

        if (quantity > Integer.MAX_VALUE - product.getUnitsSold()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sale quantity exceeds the maximum units sold");
        }

        product.setCurrentStock(
                product.getCurrentStock() - quantity);

        product.setUnitsSold(
                product.getUnitsSold() + quantity);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public InventoryAnalyticsResponse getInventoryAnalytics() {

        List<Product> products = productRepository.findAll();

        int totalProducts = products.size();

        double totalRevenue = 0;
        double totalInventoryCost = 0;
        double totalPotentialProfit = 0;

        long productsRequiringReorder = 0;
        long slowMovingProducts = 0;

        for (Product product : products) {

            totalRevenue += product.calculateRevenueGenerated();

            totalInventoryCost += product.calculateInventoryCost();

            totalPotentialProfit += product.calculatePotentialProfit();

            if (product.isReorderRequired()) {
                productsRequiringReorder++;
            }

            if (product.isSlowMoving()) {
                slowMovingProducts++;
            }
        }

        return new InventoryAnalyticsResponse(
                totalProducts,
                totalRevenue,
                totalInventoryCost,
                totalPotentialProfit,
                productsRequiringReorder,
                slowMovingProducts);
    }

    @Transactional(readOnly = true)
    public List<CategoryRevenueResponse> getCategoryRevenue() {

        List<Product> products = productRepository.findAll();

        Map<String, Double> categoryRevenue = new LinkedHashMap<>();

        for (Product product : products) {

            String category = product.getCategory();

            double revenue = product.calculateRevenueGenerated();

            categoryRevenue.merge(
                    category,
                    revenue,
                    Double::sum);
        }

        List<CategoryRevenueResponse> results = new ArrayList<>();

        for (Map.Entry<String, Double> entry : categoryRevenue.entrySet()) {

            results.add(new CategoryRevenueResponse(
                    entry.getKey(),
                    entry.getValue()));
        }

        return results;
    }

    @Transactional(readOnly = true)
    public List<InventoryRiskResponse> getInventoryRisks() {

        List<Product> products = productRepository.findAll();

        List<InventoryRiskResponse> risks = new ArrayList<>();

        for (Product product : products) {

            if (product.isReorderRequired()
                    || product.isSlowMoving()) {

                risks.add(new InventoryRiskResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getCategory(),
                        product.getCurrentStock(),
                        product.getReorderLevel(),
                        product.getUnitsSold(),
                        product.getInventoryRiskStatus(),
                        product.getInventoryRiskExplanation(),
                        product.getReorderRecommendation()));
            }
        }

        return risks;
    }

    @Transactional(readOnly = true)
    public List<InventoryPriorityResponse> getInventoryPriorities() {

        List<Product> products = productRepository.findAll();

        List<InventoryPriorityResponse> priorities = new ArrayList<>();

        for (Product product : products) {

            boolean reorderRequired = product.isReorderRequired();
            boolean slowMoving = product.isSlowMoving();

            String priority;
            String reason;

            if (reorderRequired && slowMoving) {

                priority = "HIGH";
                reason = "Stock is below reorder level and sales are slow. Review demand before replenishing.";

            } else if (reorderRequired) {

                priority = "HIGH";
                reason = "Stock is below reorder level. Replenishment may be required.";

            } else if (slowMoving) {

                priority = "LOW";
                reason = "Stock is sufficient, but sales are slow. Review demand and excess inventory.";

            } else {
                continue;
            }

            priorities.add(new InventoryPriorityResponse(
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getCurrentStock(),
                    product.getReorderLevel(),
                    product.getUnitsSold(),
                    priority,
                    reason));
        }

        return priorities;
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

    public List<Product> getProductsFromDatabase() {
        return productRepository.findAll();
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

    public boolean restockProduct(int productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Restock quantity must be greater than zero.");
        }

        Product product = findProductById(productId);

        if (product == null) {
            return false;
        }

        product.setCurrentStock(
                Math.addExact(product.getCurrentStock(), quantity));

        return true;
    }

    public boolean recordSale(int productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Sale quantity must be greater than zero.");
        }

        Product product = findProductById(productId);

        if (product == null) {
            return false;
        }

        if (quantity > product.getCurrentStock()) {
            throw new IllegalArgumentException(
                    "Insufficient stock to complete the sale.");
        }

        int updatedUnitsSold = Math.addExact(
                product.getUnitsSold(), quantity);

        product.setCurrentStock(
                product.getCurrentStock() - quantity);

        product.setUnitsSold(updatedUnitsSold);

        return true;
    }

}
