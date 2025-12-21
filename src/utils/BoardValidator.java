package utils;

import main.java.Models.Board;

public class BoardValidator {

    private static final int BOARD_SIZE = 9;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    public static boolean isValidValue(int value) {
        return value == 0 || (value >= MIN_VALUE && value <= MAX_VALUE);
    }

    public static boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public static boolean hasValidValues(Board board) {
        if (board == null) return false;

        int[][] grid = board.getGrid();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!isValidValue(grid[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean hasValidDimensions(int[][] grid) {
        if (grid == null) return false;
        if (grid.length != BOARD_SIZE) return false;

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (grid[i] == null || grid[i].length != BOARD_SIZE) {
                return false;
            }
        }
        return true;
    }

    public static int countEmptyCells(Board board) {
        if (board == null) return 0;

        int count = 0;
        int[][] grid = board.getGrid();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (grid[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }

    public static boolean isBoardStructureValid(Board board) {
        if (board == null) return false;
        int[][] grid = board.getGrid();
        return hasValidDimensions(grid);
    }
}