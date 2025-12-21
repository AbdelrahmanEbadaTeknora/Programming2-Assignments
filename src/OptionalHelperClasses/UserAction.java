package OptionalHelperClasses;

/**
 * Encapsulates a user action for main.javaa.logging
 */
public class UserAction {
    private int row;
    private int col;
    private int newValue;
    private int previousValue;
    private long timestamp;

    public UserAction(int row, int col, int newValue, int previousValue) {
        this.row = row;
        this.col = col;
        this.newValue = newValue;
        this.previousValue = previousValue;
        this.timestamp = System.currentTimeMillis();
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

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d,%d)", row, col, newValue, previousValue);
    }
}