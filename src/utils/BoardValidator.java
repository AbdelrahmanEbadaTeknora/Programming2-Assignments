package utils;

import Models.Board;

public class BoardValidator {

    private static final int BOARD_SIZE = 9;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;

    public static boolean isValidValue(int value) {
        return value == 0 || (value >= MIN_VALUE && value <= MAX_VALUE);
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

}