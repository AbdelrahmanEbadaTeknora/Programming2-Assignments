package Common;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class GenericDatabase<T extends IRecord> {
    protected ArrayList<T> records;
    protected String filename;

    public GenericDatabase(String filename) {
        this.filename = filename;
        this.records = new ArrayList<>();
    }

    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T record = createRecordFrom(line);
                    if (record != null) {
                        records.add(record);
                    }
                }
            }
            System.out.println(records.size() + " record(s) loaded from " + filename);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T record : records) {
                writer.write(record.lineRepresentation());
                writer.newLine();
            }
            System.out.println("Saved " + records.size() + " record(s) to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public abstract T createRecordFrom(String line);

    public void insertRecord(T record) {
        if (record == null) {
            System.out.println("Cannot insert a null record");
            return;
        }
        if (contains(record.getSearchKey())) {
            System.out.println("Record with key " + record.getSearchKey() + " already exists.");
            return;
        }
        records.add(record);
        System.out.println("Record added successfully!");
    }

    public void deleteRecord(String key) {
        Iterator<T> iterator = records.iterator();
        while (iterator.hasNext()) {
            T record = iterator.next();
            if (record.getSearchKey().equals(key)) {
                iterator.remove();
                System.out.println("Record deleted successfully");
                return;
            }
        }
        System.out.println("Record not found with key: " + key);
    }

    public boolean contains(String key) {
        for (T record : records) {
            if (record.getSearchKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public T getRecord(String key) {
        for (T record : records) {
            if (record.getSearchKey().equals(key)) {
                return record;
            }
        }
        return null;
    }

    public ArrayList<T> returnAllRecords() {
        return records;
    }
}
