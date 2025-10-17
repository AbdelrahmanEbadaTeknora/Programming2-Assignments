package EmployeeSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeUserDatabase {
    private ArrayList<EmployeeUser> records;
    private String filename;

    public EmployeeUserDatabase(String filename) {
        this.filename = filename;
        this.records = new ArrayList<>();
    }

    public void readFromFile() 
    {
        File file = new File(filename);

        int count = 0; // Track how many employees are loaded

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) continue;

                EmployeeUser emp = createRecordFrom(line);

                if (emp != null) {
                    records.add(emp);
                    count++;
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
            System.out.println(count + " employee(s) loaded from " + filename);
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public EmployeeUser createRecordFrom(String line) 
    {
        String[] parts = line.split(",");
        
        // Validate the number of fields
        if (parts.length < 5) {
            return null; // invalid 
        }

        return new EmployeeUser(
            parts[0].trim(), // employeeId
            parts[1].trim(), // name
            parts[2].trim(), // email
            parts[3].trim(), // address
            parts[4].trim()  // phoneNumber
        );
    }

    public ArrayList<EmployeeUser> returnAllRecords()
    {
        return records;
    }

    public boolean contains(String key)  
    {
        for(EmployeeUser e : records)
        {
            if(e.getSearchKey().equals(key))
                return true;
        }
        return false;
    }
/*searches the arrayList referenced by the 
instance variable records and returns a reference on the EmployeeUser object whose 
employee id equals the parameter key.  */
    public EmployeeUser getRecord(String key) 
    {
        for(EmployeeUser e : records)
        {
            if(e.getSearchKey().equals(key))
            {
                return e;
            }
        }
        System.out.println("we couldn't find an employee with this is : " + key );
        return null;
    }
/* inserts the object referenced by the 
parameter record into the arrayList referenced by the instance variable records. */
    public void insertRecord(EmployeeUser record) 
    {
        records.add(record);
    }

    public void deleteRecord(String key) 
    {
        records.removeIf(e -> e.getSearchKey().equals(key));
    }
/* deletes the old data stored in the file whose name is stored in 
filename, writes the data stored in the arrayList referenced by the instance variable records to 
the file. Each line represents the data of one employee comma separated. */
    public void saveToFile() {
        try 
        {
            FileWriter writer = new FileWriter(filename); // overwites on the old data

            for (EmployeeUser e : records) {
                writer.write(e.lineRepresentation() + "\n");
            }

            writer.close(); 
            System.out.println("Saved " + records.size() + " employee(s) to " + filename);
        } 
        catch (IOException e) 
        {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
