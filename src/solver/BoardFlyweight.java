package main.java.solver;

import main.java.Models.Board;
import OptionalHelperClasses.Position;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Flyweight pattern for efficient board verification
 * Verifies if a board state is valid WITHOUT copying or modifying the board
 * Used during solver to test permutations without memory overhead
 */
public class BoardFlyweight {
    private static final int BOARD_SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    /**
     * Verifies if board is valid with a specific permutation applied to empty cells
     * Does NOT modify the board - tests permutation virtually
     *
     * @param board Original board with empty cells (0s)
     * @param emptyPositions Positions of empty cells
     * @param permutation Values to fill in the empty positions
     * @return true if resulting board would be valid
     */
    public static boolean isValidWithPermutation(Board board,
                                                 List<Position> emptyPositions,
                                                 int[] permutation) {
        if (board == null || emptyPositions == null || permutation == null) {
            return false;
        }

        if (emptyPositions.size() != permutation.length) {
            return false;
        }

        int[][] grid = board.getGrid();

        // Create a virtual board state (only in memory, not on actual board)
        int[][] virtualGrid = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(grid[i], 0, virtualGrid[i], 0, BOARD_SIZE);
        }

        // Apply permutation to virtual grid
        for (int i = 0; i < emptyPositions.size(); i++) {
            Position pos = emptyPositions.get(i);
            virtualGrid[pos.getRow()][pos.getCol()] = permutation[i];
        }

        // Verify the virtual grid
        return isValidBoard(virtualGrid);
    }

    /**
     * Checks if a complete board is valid (no duplicates in rows, columns, boxes)
     */
    private static boolean isValidBoard(int[][] grid) {
        // Check rows
        if (!checkAllRows(grid)) {
            return false;
        }

        // Check columns
        if (!checkAllColumns(grid)) {
            return false;
        }

        // Check 3x3 boxes
        if (!checkAllBoxes(grid)) {
            return false;
        }

        return true;
    }

    /**
     * Verifies all rows for duplicates and valid values
     */
    private static boolean checkAllRows(int[][] grid) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (!isRowValid(grid, row)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies all columns for duplicates and valid values
     */
    private static boolean checkAllColumns(int[][] grid) {
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (!isColumnValid(grid, col)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies all 3x3 boxes for duplicates and valid values
     */
    private static boolean checkAllBoxes(int[][] grid) {
        for (int boxRow = 0; boxRow < BOARD_SIZE; boxRow += BOX_SIZE) {
            for (int boxCol = 0; boxCol < BOARD_SIZE; boxCol += BOX_SIZE) {
                if (!isBoxValid(grid, boxRow, boxCol)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a specific row is valid
     */
    private static boolean isRowValid(int[][] grid, int row) {
        Set<Integer> seen = new HashSet<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            int value = grid[row][col];
            if (value < MIN_VALUE || value > MAX_VALUE) {
                return false;
            }
            if (!seen.add(value)) {
                return false; // Duplicate found
            }
        }
        return true;
    }

    /**
     * Checks if a specific column is valid
     */
    private static boolean isColumnValid(int[][] grid, int col) {
        Set<Integer> seen = new HashSet<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            int value = grid[row][col];
            if (value < MIN_VALUE || value > MAX_VALUE) {
                return false;
            }
            if (!seen.add(value)) {
                return false; // Duplicate found
            }
        }
        return true;
    }

    /**
     * Checks if a specific 3x3 box is valid
     */
    private static boolean isBoxValid(int[][] grid, int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                int value = grid[startRow + i][startCol + j];
                if (value < MIN_VALUE || value > MAX_VALUE) {
                    return false;
                }
                if (!seen.add(value)) {
                    return false; // Duplicate found
                }
            }
        }
        return true;
    }
}