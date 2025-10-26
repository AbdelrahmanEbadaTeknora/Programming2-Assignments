import java.io.*;
import java.util.*;

public class FileManager implements IDataStorage {
    private String filePath;
    private static final String BACKUP_PATH = "data/backup.csv";
    
    // Constructor
    public FileManager(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }
    
    /**
     * Helper method to check if a file exists
     * @param path The file path to check
     * @param showWarning If true, displays a warning message if file doesn't exist
     * @return true if file exists, false otherwise
     */
    private boolean checkFileExists(String path, boolean showWarning) {
        File file = new File(path);
        boolean exists = file.exists();
        
        if (!exists && showWarning) {
            System.err.println("WARNING: File does not exist: " + path);
        }
        
        return exists;
    }
    
    /**
     * Creates the data directory and file if they don't exist
     */
    private void createFileIfNotExists() {
        // Create data folder
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created directory: data/");
        }
        
        // Create students.csv file
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new data file: " + filePath);
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Load all students from CSV file
     * @return List of student objects
     * @throws IOException if file cannot be read
     */
    @Override
    public List<student> loadData() throws IOException {
        List<student> students = new ArrayList<>();
        
        // Check if file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist yet. Returning empty list.");
            return students; // Return empty list, not error
        }
        
        // Read from file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    student s = student.fromCSV(line);
                    if (s != null) {
                        students.add(s);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                    // Continue reading other lines
                }
            }
            
            System.out.println("Loaded " + students.size() + " student(s) from file.");
        }
        
        return students;
    }
    
    /**
     * Save all students to CSV file
     * @param students List of student objects to save
     * @return true if successful, false otherwise
     * @throws IOException if file cannot be written
     */
    @Override
    public boolean saveData(List<student> students) throws IOException {
        if (students == null) {
            System.err.println("Cannot save null student list.");
            return false;
        }
        
        // Write to file (overwrites existing content)
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (student s : students) {
                if (s != null) {
                    pw.println(s.toCSV());
                }
            }
            System.out.println("Saved " + students.size() + " student(s) to file.");
            return true;
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            throw e; // Re-throw to let caller handle it
        }
    }
    
    /**
     * Delete the data file
     * @return true if deleted, false otherwise
     * @throws IOException if deletion fails
     */
    @Override
    public boolean deleteData() throws IOException {
        File file = new File(filePath);
        
        // Check if file exists
        if (!checkFileExists(filePath, false)) {
            System.err.println("File does not exist: " + filePath);
            return false;
        }
        
        // Delete the file
        boolean deleted = file.delete();
        
        if (deleted) {
            System.out.println("File deleted successfully: " + filePath);
        } else {
            System.err.println("Failed to delete file: " + filePath);
        }
        
        return deleted;
    }
    
    /**
     * Create a backup copy of the data file
     * @return true if backup created, false otherwise
     * @throws IOException if backup fails
     */
    @Override
    public boolean backupData() throws IOException {
        File sourceFile = new File(filePath);
        
        // Check if source file exists
        if (!sourceFile.exists()) {
            System.err.println("Source file not found: " + filePath);
            return false;
        }
        
        // Ensure backup directory exists
        File backupFile = new File(BACKUP_PATH);
        File backupDir = backupFile.getParentFile();
        if (backupDir != null && !backupDir.exists()) {
            backupDir.mkdirs();
        }
        
        // Copy content from students.csv to backup.csv
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PrintWriter pw = new PrintWriter(new FileWriter(BACKUP_PATH))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                pw.println(line);
            }
            
            System.out.println("Backup created successfully: " + BACKUP_PATH);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Public method to check if the main file exists
     * @return true if file exists, false otherwise
     */
    public boolean fileExists() {
        return checkFileExists(filePath, false);
    }
    
    /**
     * Get the current file path
     * @return file path string
     */
    public String getFilePath() {
        return filePath;
    }
}