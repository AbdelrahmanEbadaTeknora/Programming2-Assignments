package storageAndLogging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single log entry for a user action
 * Stores row, column, new value, and previous value
 */
public class LogEntry {
    private int row;
    private int col;
    private int newValue;
    private int previousValue;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogEntry(int row, int col, int newValue, int previousValue) {
        this.row = row;
        this.col = col;
        this.newValue = newValue;
        this.previousValue = previousValue;
        this.timestamp = LocalDateTime.now();
    }

    public LogEntry(int row, int col, int newValue, int previousValue,
                    LocalDateTime timestamp) {
        this.row = row;
        this.col = col;
        this.newValue = newValue;
        this.previousValue = previousValue;
        this.timestamp = timestamp;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getNewValue() {
        return newValue;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Converts to log file format: (x,y,val,prev)
     */
    public String toLogFormat() {
        return String.format("(%d,%d,%d,%d)", row, col, newValue, previousValue);
    }

    /**
     * Gets human-readable description
     */
    public String getDescription() {
        return String.format("Cell (%d,%d): %d → %d at %s",
                row, col, previousValue, newValue,
                timestamp.format(formatter));
    }

    @Override
    public String toString() {
        return toLogFormat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntry logEntry = (LogEntry) o;
        return row == logEntry.row &&
                col == logEntry.col &&
                newValue == logEntry.newValue &&
                previousValue == logEntry.previousValue;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(row, col, newValue, previousValue);
    }
}