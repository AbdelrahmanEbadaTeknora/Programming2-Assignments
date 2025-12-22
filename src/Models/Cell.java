package Models;

import Models.enums.CellState;


public class Cell {
    private int value;              // 0-9 (0 = empty)
    private int row;                // 0-8
    private int col;                // 0-8
    private CellState state;        // EMPTY, FILLED, or INITIAL
    private int previousValue;      // For undo functionality


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


    public Cell(int row, int col, int value, CellState state) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.previousValue = value;
        this.state = state;
    }

    // ==================== Getters ====================


    public int getValue() {
        return value;
    }


    public int getRow() {
        return row;
    }


    public int getCol() {
        return col;
    }


    public CellState getState() {
        return state;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    // ==================== Setters ====================


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


    public void setState(CellState state) {
        if (state != null) {
            this.state = state;
        }
    }


    public void setPreviousValue(int previousValue) {
        this.previousValue = previousValue;
    }

    // ==================== State Checks ====================

    public boolean isEmpty() {
        return value == 0;
    }


    public boolean isFilled() {
        return value != 0;
    }


    public boolean isEditable() {
        return state != CellState.INITIAL;
    }


    // ==================== Reset & Undo ====================

    public void clear() {
        if (isEditable()) {
            setValue(0);
        }
    }


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


    public void reset() {
        if (isEditable()) {
            this.value = 0;
            this.previousValue = 0;
            this.state = CellState.EMPTY;
        }
    }

    // ==================== Utility Methods ====================

    public Cell copy() {
        Cell copy = new Cell(this.row, this.col, this.value, this.state);
        copy.setPreviousValue(this.previousValue);
        return copy;
    }

    public String getPosition() {
        return row + "," + col;
    }


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


    @Override
    public int hashCode() {
        int result = value;
        result = 31 * result + row;
        result = 31 * result + col;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }


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