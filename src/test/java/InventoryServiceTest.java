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

}
