package storageAndLogging;

import main.java.utils.Constants;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Logs user actions to file in format: (x, y, val, prev)
 * Each line represents one move: row, column, new value, previous value
 */
public class GameLogger {
    private String logFilePath;

    public GameLogger() {
        this.logFilePath = Constants.GAMES_FOLDER + File.separator +
                Constants.INCOMPLETE_FOLDER + File.separator +
                Constants.LOG_FILE;
    }

    /**
     * Logs an action to the log file
     * Format: (x, y, val, prev)
     * Example: (3,5,3,0) means at row 3, col 5, entered 3, was previously 0
     *
     * @param action String representation of action
     * @throws IOException if write fails
     */
    public void log(String action) throws IOException {
        createLogFileIfNotExists();

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(action);
            writer.write("\n");
            writer.flush();
        }
    }

    /**
     * Logs a move with explicit parameters
     */
    public void logMove(int row, int col, int newValue, int previousValue)
            throws IOException {
        String action = String.format("(%d,%d,%d,%d)", row, col, newValue, previousValue);
        log(action);
    }

    /**
     * Removes the last entry from the log file
     * Used for undo operations
     *
     * @throws IOException if operation fails
     */
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

    /**
     * Gets the last log entry
     */
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

    /**
     * Clears all log entries
     */
    public void clearLog() throws IOException {
        createLogFileIfNotExists();

        try (FileWriter writer = new FileWriter(logFilePath)) {
            // Just open and close to clear file
        }
    }

    /**
     * Gets all log entries
     */
    public List<String> getAllEntries() throws IOException {
        if (!logFileExists()) {
            return new ArrayList<>();
        }
        return readAllLines();
    }

    /**
     * Reads all lines from log file
     */
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

    /**
     * Writes all lines to log file
     */
    private void writeAllLines(List<String> lines) throws IOException {
        try (FileWriter writer = new FileWriter(logFilePath)) {
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
            writer.flush();
        }
    }

    /**
     * Checks if log file exists
     */
    private boolean logFileExists() {
        File file = new File(logFilePath);
        return file.exists() && file.isFile();
    }

    /**
     * Creates log file if it doesn't exist
     */
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

    /**
     * Sets custom log file path
     */
    public void setLogFilePath(String path) {
        this.logFilePath = path;
    }

    /**
     * Gets current log file path
     */
    public String getLogFilePath() {
        return logFilePath;
    }
}