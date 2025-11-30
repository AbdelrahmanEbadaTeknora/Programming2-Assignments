package com.sudoku.model;

import java.util.Objects;

/**
 * Represents a single cell in the Sudoku board
 */
public class Cell {
    private final int row;
    private final int col;
    private final int value;

    /**
     * Constructor for com.sudoku.model.Cell
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @param value com.sudoku.model.Cell value (1-9)
     */
    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    /**
     * Get the row index (0-based)
     * @return Row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column index (0-based)
     * @return Column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Get the cell value
     * @return com.sudoku.model.Cell value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get row index as 1-based for display purposes
     * @return Row number (1-9)
     */
    public int getRowNumber() {
        return row + 1;
    }

    /**
     * Get column index as 1-based for display purposes
     * @return Column number (1-9)
     */
    public int getColNumber() {
        return col + 1;
    }

    @Override
    public String toString() {
        return String.format("com.sudoku.model.Cell[row=%d, col=%d, value=%d]",
                getRowNumber(), getColNumber(), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col && value == cell.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value);
    }
}