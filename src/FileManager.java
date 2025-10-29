import java.io.*;
import java.util.*;

public class FileManager implements IDataStorage {
    private String filePath;
    private static final String BACKUP_PATH = "data/backup.csv";

    public FileManager(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
    }

    private boolean checkFileExists(String path, boolean showWarning) {
        File file = new File(path);
        boolean exists = file.exists();

        if (!exists && showWarning) {
            System.err.println("WARNING: File does not exist: " + path);
        }

        return exists;
    }

    private void createFileIfNotExists() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created directory: data/");
        }

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

    @Override
    public List<student> loadData() throws IOException {
        List<student> students = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist yet. Returning empty list.");
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

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
                }
            }

            System.out.println("Loaded " + students.size() + " student(s) from file.");
        }

        return students;
    }

    @Override
    public boolean saveData(List<student> students) throws IOException {
        if (students == null) {
            System.err.println("Cannot save null student list.");
            return false;
        }

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
            throw e;
        }
    }

    @Override
    public boolean deleteData() throws IOException {
        File file = new File(filePath);

        if (!checkFileExists(filePath, false)) {
            System.err.println("File does not exist: " + filePath);
            return false;
        }

        boolean deleted = file.delete();

        if (deleted) {
            System.out.println("File deleted successfully: " + filePath);
        } else {
            System.err.println("Failed to delete file: " + filePath);
        }

        return deleted;
    }

    @Override
    public boolean backupData() throws IOException {
        File sourceFile = new File(filePath);

        if (!sourceFile.exists()) {
            System.err.println("Source file not found: " + filePath);
            return false;
        }

        File backupFile = new File(BACKUP_PATH);
        File backupDir = backupFile.getParentFile();
        if (backupDir != null && !backupDir.exists()) {
            backupDir.mkdirs();
        }

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

    public boolean fileExists() {
        return checkFileExists(filePath, false);
    }

    public String getFilePath() {
        return filePath;
    }
}