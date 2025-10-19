package ProductManagement;

import Common.GenericDatabase;

public class ProductDatabase extends GenericDatabase<Product> {

    public ProductDatabase(String filename) {
        super(filename);
    }

    @Override
    public Product createRecordFrom(String line) {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            return null;
        }
        try {
            return new Product(
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    Integer.parseInt(parts[4].trim()),
                    Float.parseFloat(parts[5].trim())
            );
        } catch (NumberFormatException e) {
            System.out.println("Error parsing product: " + e.getMessage());
            return null;
        }
    }
}