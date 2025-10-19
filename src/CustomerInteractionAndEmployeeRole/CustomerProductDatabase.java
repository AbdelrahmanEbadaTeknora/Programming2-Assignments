package CustomerInteractionAndEmployeeRole;

import Common.GenericDatabase;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerProductDatabase extends GenericDatabase<CustomerProduct> {

    public CustomerProductDatabase(String filename) {
        super(filename);
    }

    @Override
    public CustomerProduct createRecordFrom(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate purchaseDate = LocalDate.parse(parts[2].trim(), formatter);
            CustomerProduct cp = new CustomerProduct(
                    parts[0].trim(),
                    parts[1].trim(),
                    purchaseDate
            );
            cp.setPaid(Boolean.parseBoolean(parts[3].trim()));
            return cp;
        } catch (Exception e) {
            System.out.println("Error parsing customer product: " + e.getMessage());
            return null;
        }
    }
}