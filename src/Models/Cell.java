package Models;

import Models.enums.CellState;

/**
 * Represents a single cell in the Sudoku board
 * Stores the cell value, state, and position
 */
public class Cell {
    private int value;              // 0-9 (0 = empty)
    private int row;                // 0-8
    private int col;                // 0-8
    private CellState state;        // EMPTY, FILLED, or INITIAL
    private int previousValue;      // For undo functionality

    /**
     * Constructor with position and value
     * @param row Row position (0-8)
     * @param col Column position (0-8)
     * @param value Cell value (0-9, 0 = empty)
     */
    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.previousValue = value;

        // Determine initial state
        if (value == 0) {
            this.state = CellState.EMPTY;
        } else {
            this.state = CellState.INITIAL;
        }
    }

    /**
     * Constructor with position, value, and state
     */
    public Cell(int row, int col, int value, CellState state) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.previousValue = value;
        this.state = state;
    }

    // ==================== Getters ====================

    /**
     * Gets the cell value (0-9)
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the cell state
     */
    public CellState getState() {
        return state;
    }

    /**
     * Gets the previous value (for undo)
     */
    public int getPreviousValue() {
        return previousValue;
    }

    // ==================== Setters ====================

    /**
     * Sets the cell value
     * @param value Value to set (0-9)
     */
    public void setValue(int value) {
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Cell value must be between 0 and 9");
        }

        this.previousValue = this.value;
        this.value = value;

        // Update state based on value
        if (value == 0) {
            if (state != CellState.INITIAL) {
                state = CellState.EMPTY;
            }
        } else {
            if (state != CellState.INITIAL) {
                state = CellState.FILLED;
            }
        }
    }

    /**
     * Sets the cell state
     */
    public void setState(CellState state) {
        if (state != null) {
            this.state = state;
        }
    }

    /**
     * Sets the previous value (for tracking changes)
     */
    public void setPreviousValue(int previousValue) {
        this.previousValue = previousValue;
    }

    // ==================== State Checks ====================

    /**
     * Checks if cell is empty (value = 0)
     */
    public boolean isEmpty() {
        return value == 0;
    }

    /**
     * Checks if cell is filled (value != 0)
     */
    public boolean isFilled() {
        return value != 0;
    }

    /**
     * Checks if cell is editable (not initial)
     */
    public boolean isEditable() {
        return state != CellState.INITIAL;
    }

    /**
     * Checks if cell is initial (part of puzzle)
     */
    public boolean isInitial() {
        return state == CellState.INITIAL;
    }

    /**
     * Checks if cell value is valid (1-9)
     */
    public boolean isValidValue() {
        if (isEmpty()) {
            return true;
        }
        return value >= 1 && value <= 9;
    }

    // ==================== Reset & Undo ====================

    /**
     * Clears the cell (sets to 0)
     */
    public void clear() {
        if (isEditable()) {
            setValue(0);
        }
    }

    /**
     * Reverts to previous value
     */
    public void undo() {
        if (isEditable()) {
            value = previousValue;
            if (value == 0) {
                state = CellState.EMPTY;
            } else {
                state = CellState.FILLED;
            }
        }
    }

    /**
     * Resets cell to initial state (0 and EMPTY)
     */
    public void reset() {
        if (isEditable()) {
            this.value = 0;
            this.previousValue = 0;
            this.state = CellState.EMPTY;
        }
    }

    // ==================== Utility Methods ====================

    /**
     * Creates a copy of this cell
     */
    public Cell copy() {
        Cell copy = new Cell(this.row, this.col, this.value, this.state);
        copy.setPreviousValue(this.previousValue);
        return copy;
    }

    /**
     * Gets the position as string (e.g., "3,5")
     */
    public String getPosition() {
        return row + "," + col;
    }

    /**
     * Checks equality based on row, col, and value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (value != cell.value) return false;
        if (row != cell.row) return false;
        if (col != cell.col) return false;
        return state == cell.state;
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        int result = value;
        result = 31 * result + row;
        result = 31 * result + col;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    /**
     * String representation of cell
     */
    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", value=" + value +
                ", state=" + state +
                '}';
    }
}