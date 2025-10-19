package CustomerInteractionAndEmployeeRole;

import ProductManagement.Product;
import ProductManagement.ProductDatabase;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeRole {
    private ProductDatabase productDatabase;
    private CustomerProductDatabase customerProductDatabase;

    // Constructor takes both databases as parameters
    public EmployeeRole(ProductDatabase productDatabase,
                        CustomerProductDatabase customerProductDatabase) {
        this.productDatabase = productDatabase;
        this.customerProductDatabase = customerProductDatabase;
    }

    // Method for adding a new product (if employee can add products)
    public void addProduct(String productID, String productName, String manufacturerName,
                           String supplierName, int quantity, float price) {
        Product newProduct = new Product(productID, productName, manufacturerName,
                supplierName, quantity, price);
        productDatabase.insertRecord(newProduct);
    }

    // Method to get list of all products
    public ArrayList<Product> getListOfProducts() {
        return productDatabase.returnAllRecords();
    }

    // Method to get list of all purchasing operations (customer-product records)
    public ArrayList<CustomerProduct> getListOfPurchasingOperations() {
        return customerProductDatabase.getAllCustomerProducts();
    }

    // Method for recording a new purchase (Customer buys a product)
    public void purchaseProduct(String customerSSN, String productID, LocalDate purchaseDate) {
        // Verify product exists
        if (!productDatabase.contains(productID)) {
            System.out.println("Error: Product ID " + productID + " does not exist");
            return;
        }

        // Create and record the purchase
        CustomerProduct newPurchase = new CustomerProduct(customerSSN, productID, purchaseDate);
        customerProductDatabase.addCustomerProduct(newPurchase);
        System.out.println("Purchase recorded successfully");
    }

    // Method for returning a product (with 14-day rule)
    public boolean returnProduct(String customerSSN, String productID, LocalDate returnDate) {
        String searchKey = customerSSN + "," + productID + "," + formatDate(returnDate);
        CustomerProduct cp = customerProductDatabase.findCustomerProduct(searchKey);

        if (cp == null) {
            System.out.println("Error: Purchase not found");
            return false;
        }

        // Check 14-day rule
        long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(cp.getPurchaseDate(), returnDate);
        if (daysDifference > 14) {
            System.out.println("Error: Cannot return product. 14-day return window has expired");
            return false;
        }

        // Remove the purchase record
        boolean removed = customerProductDatabase.removeCustomerProduct(searchKey);
        if (removed) {
            System.out.println("Product returned successfully");
        }
        return removed;
    }

    // Method for marking a purchase as paid
    public boolean applyPayment(String customerSSN, String productID, LocalDate purchaseDate) {
        String searchKey = customerSSN + "," + productID + "," + formatDate(purchaseDate);
        CustomerProduct cp = customerProductDatabase.findCustomerProduct(searchKey);

        if (cp != null) {
            cp.setPaid(true);
            System.out.println("Payment applied successfully");
            return true;
        } else {
            System.out.println("Error: Purchase record not found");
            return false;
        }
    }

    // Save all data back to files
    public void logout() {
        productDatabase.saveToFile();
        customerProductDatabase.saveToFile();
        System.out.println("All data saved. Logout successful");
    }

    // Helper method to format date to dd-MM-yyyy
    private String formatDate(LocalDate date) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}