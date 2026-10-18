import com.northstar.model.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void shouldRejectInvalidProductId() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Product(
                    0,
                    "Laptop",
                    "Electronics",
                    450.0,
                    650.0,
                    8,
                    10,
                    42);
        });
    }

    @Test
    void shouldRejectNegativeCostPrice() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Product(
                    101,
                    "Laptop",
                    "Electronics",
                    -450.0,
                    650.0,
                    8,
                    10,
                    42);
        });
    }

}
