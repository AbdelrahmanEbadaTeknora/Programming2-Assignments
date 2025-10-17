package ProductManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductDatabase {

    private String filename;
    private ArrayList<Product> records;

    public ProductDatabase(String filename) {
        this.filename = filename;
        this.records = new ArrayList<>();
    }


    public void readFromFile()
    {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 6) {
                    Product product = new Product(parts[0].trim(), parts[1].trim(),parts[2].trim(), parts[3].trim(), Integer.parseInt(parts[4].trim()),Float.parseFloat(parts[5].trim()));
                    records.add( product);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    public Product createRecordFrom(String line) {
        Product product = null;
        String[] parts = line.split(",");
        if (parts.length == 6) {
            product = new Product(parts[0].trim(), parts[1].trim(),parts[2].trim(), parts[3].trim(), Integer.parseInt(parts[4].trim()),Float.parseFloat(parts[5].trim()));
        }
        else {
            System.out.println("line should contain 6 parts");
            return null;
        }
        return product;
    }

    public ArrayList<Product> returnAllRecords(){

        return records;

    }
    public boolean contains(String key )
    {
        for (Product product : records) {
            if (product.getSearchKey().equals(key)) {
                return true;
            }

        }
        System.out.println("doesnt exist!");
        return false;
    }

    public Product getRecord(String key) {
        for (Product product : records) {
            if (product.getSearchKey().equals(key)) {
                return product;
            }
        }
        return null;
    }
    public void insertRecord(Product record){
        if (record == null) {
            System.out.println("cannot insert a null record");
            return;
        }

            for (Product product : records) {
                if (product.getSearchKey().equals(record.getSearchKey())) {
                    System.out.println("Error: Product with ID " + record.getSearchKey() + " already exists.");
                    return;
                }
            }
        records.add(record);
        System.out.println("Record added succesfully!");
        }
    public void deleteRecord(String key){

        for (Product product : records) {
            if (product.getSearchKey().equals(key)) {
                records.remove(product);
                System.out.println("Record deleted succesfully");

                return;
            }
        }
        System.out.println("nothing to delete with that with this key");
    }
    public void saveToFile(){
        try{
        FileWriter writer = new FileWriter(filename);
        for (Product product : records) {
            writer.write(product.lineRepresentation() + "\n");
        }
        writer.flush();
        writer.close();
    }
        catch (IOException e) {
        System.out.println("Error in the reading process " + e.getMessage());}
    }


}
