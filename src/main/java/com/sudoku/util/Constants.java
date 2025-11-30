package com.sudoku.util;

/**
 * com.sudoku.util.Constants used throughout the Sudoku Verifier application
 */
public class Constants {
    // Board dimensions
    public static final int BOARD_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int NUM_BOXES = 9;

    // Valid value range
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;

    // Execution modes
    public static final int MODE_SEQUENTIAL = 0;
    public static final int MODE_THREE_THREADS = 3;
    public static final int MODE_TWENTY_SEVEN_THREADS = 27;

    // Output labels
    public static final String VALID_OUTPUT = "VALID";
    public static final String INVALID_OUTPUT = "INVALID";
    public static final String ROW_LABEL = "ROW";
    public static final String COL_LABEL = "COL";
    public static final String BOX_LABEL = "BOX";

    // Separator for output
    public static final String SEPARATOR = "------------------------------------------";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("com.sudoku.util.Constants class cannot be instantiated");
    }
}