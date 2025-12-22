package storageAndLogging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogEntry {
    private int row;
    private int col;
    private int newValue;
    private int previousValue;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public String toLogFormat() {
        return String.format("(%d,%d,%d,%d)", row, col, newValue, previousValue);
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