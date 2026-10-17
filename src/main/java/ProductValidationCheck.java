
public class ProductValidationCheck {

public static void main(String[] args) {

    Product product = new Product(
        107, "Clearance Keyboard", "Accessories",
        40, 30, 15, 5, 20
    );

    System.out.println("Product: " + product.getProductName());
    System.out.println("Profit per unit: £" + product.calculateProfitPerUnit());
}


}
