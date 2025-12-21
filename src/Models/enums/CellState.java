package Models.enums;

/**
 * Represents the state of a cell in the Sudoku board
 */
public enum CellState {
    EMPTY,      // Cell is empty (value = 0)
    FILLED,     // Cell was filled by user
    INITIAL     // Cell was part of original puzzle (not editable)
}

