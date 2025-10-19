package CustomerInteractionAndEmployeeRole;

public class EmployeeRole {
}
import java.time.LocalDate;

public class EmployeeRole
{
    // The employee role works with the system databases
    private CustomerDatabase customerDatabase;
    private ProductDatabase productDatabase;
    private CustomerProductDatabase customerProductDatabase;

    // Constructor takes all 3 databases as parameters
    public EmployeeRole(CustomerDatabase customerDatabase,
                        ProductDatabase productDatabase,
                        CustomerProductDatabase customerProductDatabase)
    {
        this.customerDatabase = customerDatabase;
        this.productDatabase = productDatabase;
        this.customerProductDatabase = customerProductDatabase;
    }

    // Method for adding a new customer
    public void addCustomer(String ssn, String name, String address, String phone)
    {
        Customer newCustomer = new Customer(ssn, name, address, phone);
        customerDatabase.addCustomer(newCustomer);
    }

    // Method for adding a new product
    public void addProduct(String productID, String productName, double price)
    {
        Product newProduct = new Product(productID, productName, price);
        productDatabase.addProduct(newProduct);
    }

    // Method for recording a new purchase (Customer buys a product)
    public void recordPurchase(String customerSSN, String productID, LocalDate purchaseDate)
    {
        CustomerProduct newPurchase = new CustomerProduct(customerSSN, productID, purchaseDate);
        customerProductDatabase.addCustomerProduct(newPurchase);
    }

    // Method for marking a purchase as paid
    public boolean markAsPaid(String searchKey)
    {
        CustomerProduct cp = customerProductDatabase.findCustomerProduct(searchKey);

        if (cp != null)
        {
            cp.setPaid(true);
            return true; // success
        }
        else
        {
            return false; // purchase not found
        }
    }

    // Save all data back to files
    public void saveAll()
    {
        customerDatabase.saveToFile();
        productDatabase.saveToFile();
        customerProductDatabase.saveToFile();
    }
}