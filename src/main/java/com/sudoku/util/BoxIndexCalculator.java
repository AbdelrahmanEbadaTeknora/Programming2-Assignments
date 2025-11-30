package com.sudoku.util;

/**
 * Utility class for calculating box indices and extracting box values
 * Box numbering: 1-9 (left-to-right, top-to-bottom)
 *
 * Box layout:
 * 1 2 3
 * 4 5 6
 * 7 8 9
 */
public class BoxIndexCalculator {

    /**
     * Calculate which box (1-9) a cell belongs to based on its row and column
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @return Box index (1-9)
     */
    public static int getBoxIndex(int row, int col) {
        if (row < 0 || row >= Constants.BOARD_SIZE || col < 0 || col >= Constants.BOARD_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Invalid cell position: row=%d, col=%d", row, col)
            );
        }

        int boxRow = row / Constants.BOX_SIZE;  // 0, 1, or 2
        int boxCol = col / Constants.BOX_SIZE;  // 0, 1, or 2

        // Convert to 1-based index: 1, 2, 3, 4, 5, 6, 7, 8, 9
        return boxRow * Constants.BOX_SIZE + boxCol + 1;
    }

    /**
     * Get all cell coordinates (row, col) that belong to a specific box
     * @param boxIndex Box number (1-9)
     * @return 2D array where each row is [row, col] coordinate
     */
    public static int[][] getBoxCells(int boxIndex) {
        if (boxIndex < 1 || boxIndex > Constants.NUM_BOXES) {
            throw new IllegalArgumentException(
                    String.format("Invalid box index: %d. Must be between 1 and 9", boxIndex)
            );
        }

        int[][] cells = new int[Constants.NUM_BOXES][2];

        // Convert box index (1-9) to box position
        int boxIndex0 = boxIndex - 1;  // Convert to 0-based
        int boxRow = boxIndex0 / Constants.BOX_SIZE;
        int boxCol = boxIndex0 % Constants.BOX_SIZE;

        // Calculate starting position of the box
        int startRow = boxRow * Constants.BOX_SIZE;
        int startCol = boxCol * Constants.BOX_SIZE;

        // Fill in all 9 cells of the box
        int index = 0;
        for (int r = startRow; r < startRow + Constants.BOX_SIZE; r++) {
            for (int c = startCol; c < startCol + Constants.BOX_SIZE; c++) {
                cells[index][0] = r;
                cells[index][1] = c;
                index++;
            }
        }

        return cells;
    }

    /**
     * Extract all 9 values from a specific box
     * @param board The Sudoku board
     * @param boxIndex Box number (1-9)
     * @return Array of 9 values from the box
     */
    public static int[] extractBoxValues(int[][] board, int boxIndex) {
        if (board == null || board.length != Constants.BOARD_SIZE) {
            throw new IllegalArgumentException("Invalid board");
        }

        int[][] cells = getBoxCells(boxIndex);
        int[] values = new int[Constants.NUM_BOXES];

        for (int i = 0; i < cells.length; i++) {
            int row = cells[i][0];
            int col = cells[i][1];
            values[i] = board[row][col];
        }

        return values;
    }

    /**
     * Get the starting row of a box
     * @param boxIndex Box number (1-9)
     * @return Starting row index (0, 3, or 6)
     */
    public static int getBoxStartRow(int boxIndex) {
        if (boxIndex < 1 || boxIndex > Constants.NUM_BOXES) {
            throw new IllegalArgumentException("Invalid box index: " + boxIndex);
        }
        int boxIndex0 = boxIndex - 1;
        return (boxIndex0 / Constants.BOX_SIZE) * Constants.BOX_SIZE;
    }

    /**
     * Get the starting column of a box
     * @param boxIndex Box number (1-9)
     * @return Starting column index (0, 3, or 6)
     */
    public static int getBoxStartCol(int boxIndex) {
        if (boxIndex < 1 || boxIndex > Constants.NUM_BOXES) {
            throw new IllegalArgumentException("Invalid box index: " + boxIndex);
        }
        int boxIndex0 = boxIndex - 1;
        return (boxIndex0 % Constants.BOX_SIZE) * Constants.BOX_SIZE;
    }
}