package storageAndLogging;

import utils.Constants;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.GAMES_FOLDER;


public class GameLogger {
    private String logFilePath;

    public GameLogger() {
        this.logFilePath = GAMES_FOLDER + File.separator +
                Constants.INCOMPLETE_FOLDER + File.separator +
                Constants.LOG_FILE;
    }


    public void log(String action) throws IOException {
        createLogFileIfNotExists();

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(action);
            writer.write("\n");
            writer.flush();
        }
    }




    public void removeLastEntry() throws IOException {
        if (!logFileExists()) {
            return;
        }

        // Read all lines except the last
        List<String> lines = readAllLines();

        if (!lines.isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        // Rewrite the file
        writeAllLines(lines);
    }


    public String getLastEntry() throws IOException {
        if (!logFileExists()) {
            return null;
        }

        List<String> lines = readAllLines();
        if (lines.isEmpty()) {
            return null;
        }

        return lines.get(lines.size() - 1);
    }


    public void clearLog() throws IOException {
        createLogFileIfNotExists();

        try (FileWriter writer = new FileWriter(logFilePath)) {
            // Just open and close to clear file
        }
    }


    public List<String> getAllEntries() throws IOException {
        if (!logFileExists()) {
            return new ArrayList<>();
        }
        return readAllLines();
    }


    private List<String> readAllLines() throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }


    private void writeAllLines(List<String> lines) throws IOException {
        try (FileWriter writer = new FileWriter(logFilePath)) {
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
        }
    }


    private boolean logFileExists() {
        File file = new File(logFilePath);
        return file.exists() && file.isFile();
    }


    private void createLogFileIfNotExists() throws IOException {
        File file = new File(logFilePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create parent directories for log file");
            }
        }

        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                System.err.println("Failed to create log file");
            }
        }
    }

}