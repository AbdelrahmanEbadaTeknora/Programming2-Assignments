package CustomerInteractionAndEmployeeRole;

import ProductManagement.Product;
import ProductManagement.ProductDatabase;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeRole {
    private ProductDatabase productDatabase;
    private CustomerProductDatabase customerProductDatabase;

    public EmployeeRole(ProductDatabase productDatabase,
                        CustomerProductDatabase customerProductDatabase) {
        this.productDatabase = productDatabase;
        this.customerProductDatabase = customerProductDatabase;
    }

    public void addProduct(String productID, String productName, String manufacturerName,
                           String supplierName, int quantity, float price) {
        Product newProduct = new Product(productID, productName, manufacturerName,
                supplierName, quantity, price);
        productDatabase.insertRecord(newProduct);
    }

    public ArrayList<Product> getListOfProducts() {
        return productDatabase.returnAllRecords();
    }

    public ArrayList<CustomerProduct> getListOfPurchasingOperations() {
        return customerProductDatabase.returnAllRecords();
    }

    public boolean purchaseProduct(String customerSSN, String productID, LocalDate purchaseDate) {
        if (!productDatabase.contains(productID)) {
            System.out.println("Error: Product ID " + productID + " does not exist");
            return false;
        }

        Product product = productDatabase.getRecord(productID);
        if (product.getQuantity() <= 0) {
            System.out.println("Error: Product out of stock");
            return false;
        }

        product.setQuantity(product.getQuantity() - 1);
        CustomerProduct purchase = new CustomerProduct(customerSSN, productID, purchaseDate);
        customerProductDatabase.insertRecord(purchase);
        return true;
    }

    public double returnProduct(String customerSSN, String productID, LocalDate purchaseDate, LocalDate returnDate) {
        String searchKey = customerSSN + "," + productID + "," + formatDate(purchaseDate);
        CustomerProduct cp = customerProductDatabase.getRecord(searchKey);

        if (cp == null) {
            System.out.println("Error: Purchase not found");
            return -1;
        }

        long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(cp.getPurchaseDate(), returnDate);
        if (daysDifference > 14) {
            System.out.println("Error: Cannot return product. 14-day return window expired");
            return -1;
        }

        Product product = productDatabase.getRecord(productID);
        if (product == null) {
            System.out.println("Error: Product not found");
            return -1;
        }

        product.setQuantity(product.getQuantity() + 1);
        customerProductDatabase.deleteRecord(searchKey);
        System.out.println("Product returned successfully");
        return product.getPrice();
    }

    public boolean applyPayment(String customerSSN, String productID, LocalDate purchaseDate) {
        String searchKey = customerSSN + "," + productID + "," + formatDate(purchaseDate);
        CustomerProduct cp = customerProductDatabase.getRecord(searchKey);

        if (cp != null) {
            cp.setPaid(true);
            System.out.println("Payment applied successfully");
            return true;
        }
        System.out.println("Error: Purchase record not found");
        return false;
    }

    public void logout() {
        productDatabase.saveToFile();
        customerProductDatabase.saveToFile();
        System.out.println("All data saved , Logout successful");
    }

    private String formatDate(LocalDate date) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}