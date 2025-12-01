package com.sudoku.model;

import com.sudoku.util.BoxIndexCalculator;
import com.sudoku.util.Constants;

import java.util.Arrays;

/**
 * Represents a Sudoku board with methods to access rows, columns, and boxes
 */
public class SudokuBoard {
    private final int[][] board;

    /**
     * Constructor that creates a deep copy of the board
     * @param board 9x9 array representing the Sudoku board
     */
    public SudokuBoard(int[][] board) {
        if (board == null || board.length != Constants.BOARD_SIZE) {
            throw new IllegalArgumentException("Board must be 9x9");
        }

        // Create deep copy to ensure immutability
        this.board = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (board[i] == null || board[i].length != Constants.BOARD_SIZE) {
                throw new IllegalArgumentException("Each row must have 9 elements");
            }
            System.arraycopy(board[i], 0, this.board[i], 0, Constants.BOARD_SIZE);
        }
    }

    /**
     * Get a specific row from the board
     * @param rowIndex Row index (0-8)
     * @return Array of 9 values representing the row
     */
    public int[] getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= Constants.BOARD_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Invalid row index: %d. Must be between 0 and 8", rowIndex)
            );
        }

        int[] row = new int[Constants.BOARD_SIZE];
        System.arraycopy(board[rowIndex], 0, row, 0, Constants.BOARD_SIZE);
        return row;
    }

    /**
     * Get a specific column from the board
     * @param colIndex Column index (0-8)
     * @return Array of 9 values representing the column
     */
    public int[] getColumn(int colIndex) {
        if (colIndex < 0 || colIndex >= Constants.BOARD_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Invalid column index: %d. Must be between 0 and 8", colIndex)
            );
        }

        int[] column = new int[Constants.BOARD_SIZE];
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            column[i] = board[i][colIndex];
        }
        return column;
    }

    /**
     * Get a specific 3x3 box from the board
     * IMPORTANT: This method now accepts 0-based index (0-8) for consistency
     * @param boxIndex Box index (0-8)
     * @return Array of 9 values representing the box
     */
    public int[] getBox(int boxIndex) {
        if (boxIndex < 0 || boxIndex >= Constants.NUM_BOXES) {
            throw new IllegalArgumentException(
                    String.format("Invalid box index: %d. Must be between 0 and 8", boxIndex)
            );
        }

        // Convert to 1-based for BoxIndexCalculator if it expects 1-based
        return BoxIndexCalculator.extractBoxValues(board, boxIndex + 1);
    }

    /**
     * Get value at specific cell
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @return Cell value
     */
    public int getCell(int row, int col) {
        if (row < 0 || row >= Constants.BOARD_SIZE ||
                col < 0 || col >= Constants.BOARD_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Invalid cell position: row=%d, col=%d", row, col)
            );
        }
        return board[row][col];
    }

    /**
     * Get a copy of the entire board
     * @return 2D array representing the board
     */
    public int[][] getBoard() {
        int[][] copy = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, Constants.BOARD_SIZE);
        }
        return copy;
    }

    /**
     * Get the size of the board (always 9)
     * @return Board size
     */
    public int getSize() {
        return Constants.BOARD_SIZE;
    }

    /**
     * Print the board to console (for debugging)
     */
    public void printBoard() {
        System.out.println("Sudoku Board:");
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            sb.append(Arrays.toString(board[i]));
            if (i < Constants.BOARD_SIZE - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SudokuBoard that = (SudokuBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}