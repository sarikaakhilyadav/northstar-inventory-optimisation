import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    @Test
    void shouldRestockProductSuccessfully() {

        // Arrange: Create a Laptop with 8 units in stock
        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        InventoryService service = new InventoryService(new java.util.ArrayList<Product>());

        service.addProduct(laptop);

        // Act: Restock 12 units
        boolean result = service.restockProduct(101, 12);

        // Assert: Verify the result
        assertTrue(result);
        assertEquals(20, laptop.getCurrentStock());
    }

    @Test
    void shouldRecordSaleSuccessfully() {

        // Arrange
        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        InventoryService service = new InventoryService(new java.util.ArrayList<Product>());

        service.addProduct(laptop);

        // Act
        boolean result = service.recordSale(101, 3);

        // Assert
        assertTrue(result);
        assertEquals(5, laptop.getCurrentStock());
        assertEquals(45, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectSaleWhenStockIsInsufficient() {

        // Arrange
        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        InventoryService service = new InventoryService(new java.util.ArrayList<Product>());

        service.addProduct(laptop);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, 10);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectZeroQuantitySale() {

        // Arrange
        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        InventoryService service = new InventoryService(new java.util.ArrayList<Product>());

        service.addProduct(laptop);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, 0);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

    @Test
    void shouldRejectNegativeQuantitySale() {

        // Arrange
        Product laptop = new Product(
                101,
                "Laptop",
                "Electronics",
                450.0,
                650.0,
                8,
                10,
                42);

        InventoryService service = new InventoryService(new java.util.ArrayList<Product>());

        service.addProduct(laptop);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            service.recordSale(101, -5);
        });

        // Verify inventory remains unchanged
        assertEquals(8, laptop.getCurrentStock());
        assertEquals(42, laptop.getUnitsSold());
    }

}
