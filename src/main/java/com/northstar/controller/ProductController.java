package com.northstar.controller;

import com.northstar.model.Product;
import com.northstar.service.InventoryService;
import com.northstar.dto.ProductRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.northstar.dto.RestockRequest;
import com.northstar.dto.SaleRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final InventoryService inventoryService;

    public ProductController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return inventoryService.getProductsFromDatabase();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable int productId) {
        return inventoryService.getProductFromDatabaseById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody ProductRequest request) {
        return inventoryService.createProduct(request);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(
            @PathVariable int productId,
            @Valid @RequestBody ProductRequest request) {

        if (!Integer.valueOf(productId).equals(request.productId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Product ID in URL must match request body");
        }

        return inventoryService.updateProduct(productId, request);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int productId) {
        inventoryService.deleteProductFromDatabase(productId);
    }

    @PatchMapping("/{productId}/restock")
    public Product restockProduct(
            @PathVariable int productId,
            @Valid @RequestBody RestockRequest request) {

        return inventoryService.restockProductInDatabase(
                productId,
                request);
    }

    @PatchMapping("/{productId}/sell")
    public Product recordSale(
            @PathVariable int productId,
            @Valid @RequestBody SaleRequest request) {

        return inventoryService.recordSaleInDatabase(
                productId,
                request);
    }

}
