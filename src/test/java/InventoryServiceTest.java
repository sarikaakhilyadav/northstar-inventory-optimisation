import com.northstar.repository.ProductRepository;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.northstar.service.InventoryService;
import com.northstar.model.Product;
import com.northstar.dto.InventoryPriorityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    private Product laptop;
    private InventoryService service;

    @BeforeEach
    void setUp() {

        laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        service = new InventoryService(
                new java.util.ArrayList<Product>());

        service.addProduct(laptop);
    }

    @Test
    void inventoryPriorityResponseStoresExpectedValues() {

        InventoryPriorityResponse response = new InventoryPriorityResponse(
                1,
                "Laptop",
                "Electronics",
                8,
                10,
                42,
                "HIGH",
                "Stock is below reorder level");

        assertEquals(1, response.productId());
        assertEquals("Laptop", response.productName());
        assertEquals("HIGH", response.priority());
        assertEquals(8, response.currentStock());
    }

    @Test
    void shouldRestockProductSuccessfully() {

        // Act: Restock 12 units
        boolean result = service.restockProduct(101, 12);

        // Assert: Verify the result
        assertTrue(result);
        assertEquals(20, laptop.getCurrentStock());
    }

    @Test
    void shouldRecordSaleSuccessfully() {

        // Act: Sell 3 Laptops
        boolean result = service.recordSale(101, 3);

        // Assert: Verify the sale
        assertTrue(result);
        assertEquals(5, laptop.getCurrentStock());
        assertEquals(45, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectSaleWhenStockIsInsufficient() {

        // Act and Assert: Attempt to sell 10 units
        // when only 8 are available
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, 10);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectZeroQuantitySale() {

        // Act and Assert: Attempt to sell zero units
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, 0);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectNegativeQuantitySale() {

        // Act and Assert: Attempt to sell -5 units
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, -5);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

    @Test
    void shouldAssignCorrectInventoryPriorities() {

        // Arrange: Create a mock database repository
        ProductRepository repository = mock(ProductRepository.class);

        InventoryService databaseService = new InventoryService(repository);

        // Low stock and slow-moving
        Product laptop = new Product(
                1, "Laptop", "Electronics",
                450, 650, 8, 10, 42);

        // Low stock but selling well
        Product keyboard = new Product(
                2, "Keyboard", "Accessories",
                40, 80, 5, 10, 150);

        // Sufficient stock but slow-moving
        Product printer = new Product(
                3, "Printer", "Office Equipment",
                100, 160, 25, 10, 20);

        // Healthy stock and sales
        Product mouse = new Product(
                4, "Mouse", "Accessories",
                10, 20, 50, 20, 120);

        when(repository.findAll()).thenReturn(
                List.of(laptop, keyboard, printer, mouse));

        // Act: Execute the real service method
        List<InventoryPriorityResponse> results = databaseService.getInventoryPriorities();

        // Assert: Only three products require attention
        assertEquals(3, results.size());

        assertEquals("Laptop", results.get(0).productName());
        assertEquals("HIGH", results.get(0).priority());

        assertEquals("Keyboard", results.get(1).productName());
        assertEquals("HIGH", results.get(1).priority());

        assertEquals("Printer", results.get(2).productName());
        assertEquals("LOW", results.get(2).priority());

        // Healthy mouse should be excluded
        assertTrue(results.stream()
                .noneMatch(result -> result.productName().equals("Mouse")));
    }

}
