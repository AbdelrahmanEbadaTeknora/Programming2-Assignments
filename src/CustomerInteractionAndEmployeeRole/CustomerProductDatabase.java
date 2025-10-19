package CustomerInteractionAndEmployeeRole;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomerProductDatabase {

    private ArrayList<CustomerProduct> customerProducts;
    private final String fileName = "CustomersProducts.txt";

    // Constructor: reads data from file and fills the ArrayList
    public CustomerProductDatabase() {
        customerProducts = new ArrayList<>();
        readFromFile();
    }

    // Reads purchase records from the text file and stores them in the ArrayList
    private void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while ((line = reader.readLine()) != null) {
                // Example line format: 7845345678,P2568,12-02-2022,true
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    String customerSSN = parts[0];
                    String productID = parts[1];
                    LocalDate purchaseDate = LocalDate.parse(parts[2], formatter);
                    boolean paid = Boolean.parseBoolean(parts[3]);

                    // Create the object and add it to the list
                    CustomerProduct cp = new CustomerProduct(customerSSN, productID, purchaseDate);
                    cp.setPaid(paid);
                    customerProducts.add(cp);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Writes all CustomerProduct objects back to the file
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (CustomerProduct cp : customerProducts) {
                writer.write(cp.lineRepresentation());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Adds a new CustomerProduct to the list
    public void addCustomerProduct(CustomerProduct cp) {
        customerProducts.add(cp);
    }

    // Removes a CustomerProduct by search key
    public boolean removeCustomerProduct(String searchKey) {
        for (CustomerProduct cp : customerProducts) {
            if (cp.getSearchKey().equals(searchKey)) {
                customerProducts.remove(cp);
                return true;
            }
        }
        return false;
    }

    // Finds and returns a CustomerProduct by search key
    public CustomerProduct findCustomerProduct(String searchKey) {
        for (CustomerProduct cp : customerProducts) {
            if (cp.getSearchKey().equals(searchKey)) {
                return cp;
            }
        }
        return null;
    }

    // Returns all CustomerProduct objects (for testing or listing)
    public ArrayList<CustomerProduct> getAllCustomerProducts() {
        return customerProducts;
    }
}
