package CustomerInteractionAndEmployeeRole;

import Common.IRecord;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerProduct implements IRecord {
    private String customerSSN;
    private String productID;
    private LocalDate purchaseDate;
    private boolean paid;

    public CustomerProduct(String customerSSN, String productID, LocalDate purchaseDate) {
        this.customerSSN = customerSSN;
        this.productID = productID;
        this.purchaseDate = purchaseDate;
        this.paid = false;
    }

    public String getCustomerSSN() { return customerSSN; }
    public String getProductID() { return productID; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    @Override
    public String lineRepresentation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return customerSSN + "," + productID + "," + purchaseDate.format(formatter) + "," + paid;
    }

    @Override
    public String getSearchKey() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return customerSSN + "," + productID + "," + purchaseDate.format(formatter);
    }
}