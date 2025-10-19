package CustomerInteractionAndEmployeeRole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Represents a record of a customer's purchase of a product
// Each object of this class stores one transaction: who bought which product, when, and whether it's paid for

public class CustomerProduct {

    // Private attributes as required by assignment
    private String customerSSN;  // customer's SSN
    private String productID;    // ID of the purchased product
    private LocalDate purchaseDate; // date of purchase
    private boolean paid;        // whether purchase is paid for

    // Constructor: takes 3 attributes (the assignment required only these)
    public CustomerProduct(String customerSSN, String productID, LocalDate purchaseDate) {
        this.customerSSN = customerSSN;
        this.productID = productID;
        this.purchaseDate = purchaseDate;
        this.paid = false; // Default to false when created (not paid yet)
    }

    // Getter methods (created by me)
    public String getCustomerSSN() {
        return customerSSN;
    }

    public String getProductID() {
        return productID;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    // Returns true if the purchase is paid, false otherwise (created by me)
    public boolean isPaid() {
        return paid;
    }

    // Setter to change payment status (created by me)
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    // Returns the object's data as a line of text (comma-separated)
    // This will be used when saving to CustomersProducts.txt
    public String lineRepresentation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return customerSSN + "," + productID + "," + purchaseDate.format(formatter) + "," + paid;
    }

    // Returns a search key in format: customerSSN,productID,DD-MM-YYYY
    // This helps the database uniquely identify records
    public String getSearchKey() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return customerSSN + "," + productID + "," + purchaseDate.format(formatter);
    }

    // Optional: makes printing objects easier
    @Override
    public String toString() {
        return "Customer SSN: " + customerSSN + ", Product ID: " + productID +
                ", Purchase Date: " + purchaseDate + ", Paid: " + paid;
    }
}