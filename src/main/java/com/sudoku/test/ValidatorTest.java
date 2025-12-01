package com.sudoku.test;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.*;
import com.sudoku.output.OutputFormatter;

/**
 * Test class for validators.
 * Tests row, column, box, and sequential validation.
 *
 * Contains 5 comprehensive test cases:
 * 1. Valid board (from assignment specification)
 * 2. Invalid board with all 1s (from assignment specification)
 * 3. Row validator specific test
 * 4. Column validator specific test
 * 5. Box validator specific test
 *
 * @author Member 2
 */
public class ValidatorTest {

    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        System.out.println("=== Validator Tests ===\n");

        testValidBoard();
        testInvalidBoard();
        testRowValidator();
        testColumnValidator();
        testBoxValidator();

        System.out.println("\n=== All Tests Completed ===");
    }

    /**
     * Test 1: Valid Sudoku board from assignment specification (Table 1)
     */
    private static void testValidBoard() {
        System.out.println("Test 1: Valid Board");
        System.out.println("-------------------");

        int[][] validBoard = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        SudokuBoard board = new SudokuBoard(validBoard);
        SequentialValidator validator = new SequentialValidator(board);
        ValidationResult result = validator.validate();

        System.out.println(OutputFormatter.format(result));
        System.out.println("Expected: VALID");
        System.out.println("Pass: " + result.isValid());
        System.out.println();
    }

    /**
     * Test 2: Invalid board with all 1s from assignment specification (Table 2)
     */
    private static void testInvalidBoard() {
        System.out.println("Test 2: Invalid Board (All 1s)");
        System.out.println("-------------------------------");

        int[][] invalidBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                invalidBoard[i][j] = 1;
            }
        }

        SudokuBoard board = new SudokuBoard(invalidBoard);
        SequentialValidator validator = new SequentialValidator(board);
        ValidationResult result = validator.validate();

        System.out.println(OutputFormatter.format(result));
        System.out.println("Expected: INVALID with duplicates in all rows, columns, and boxes");
        System.out.println("Pass: " + !result.isValid());
        System.out.println();
    }

    /**
     * Test 3: Row validator with duplicate in first row
     */
    private static void testRowValidator() {
        System.out.println("Test 3: Row Validator");
        System.out.println("---------------------");

        int[][] boardWithDuplicateRows = {
                {1, 1, 3, 4, 5, 6, 7, 8, 9}, // Row 1 has duplicate 1
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9}
        };

        SudokuBoard board = new SudokuBoard(boardWithDuplicateRows);
        RowValidator rowValidator = new RowValidator(board);
        ValidationResult result = rowValidator.validate();

        System.out.println("Row validation result:");
        System.out.println("Has duplicates: " + !result.isValid());
        System.out.println("Number of rows with duplicates: " + result.getRowDuplicates().size());
        System.out.println("Pass: " + (result.getRowDuplicates().size() >= 1));
        System.out.println();
    }

    /**
     * Test 4: Column validator with duplicates in first column
     */
    private static void testColumnValidator() {
        System.out.println("Test 4: Column Validator");
        System.out.println("------------------------");

        int[][] boardWithDuplicateColumns = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {1, 2, 3, 4, 5, 6, 7, 8, 9}, // Column 1 has all 1s
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {8, 9, 1, 2, 3, 4, 5, 6, 7}
        };

        SudokuBoard board = new SudokuBoard(boardWithDuplicateColumns);
        ColumnValidator columnValidator = new ColumnValidator(board);
        ValidationResult result = columnValidator.validate();

        System.out.println("Column validation result:");
        System.out.println("Has duplicates: " + !result.isValid());
        System.out.println("Number of columns with duplicates: " + result.getColumnDuplicates().size());
        System.out.println("Pass: " + (result.getColumnDuplicates().size() >= 1));
        System.out.println();
    }

    /**
     * Test 5: Box validator with duplicates in top-left box
     */
    private static void testBoxValidator() {
        System.out.println("Test 5: Box Validator");
        System.out.println("---------------------");

        int[][] boardWithDuplicateBoxes = {
                {1, 1, 1, 4, 5, 6, 7, 8, 9}, // Top-left box has all 1s
                {1, 1, 1, 4, 5, 6, 7, 8, 9},
                {1, 1, 1, 4, 5, 6, 7, 8, 9},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {3, 4, 5, 6, 7, 8, 9, 1, 2}
        };

        SudokuBoard board = new SudokuBoard(boardWithDuplicateBoxes);
        BoxValidator boxValidator = new BoxValidator(board);
        ValidationResult result = boxValidator.validate();

        System.out.println("Box validation result:");
        System.out.println("Has duplicates: " + !result.isValid());
        System.out.println("Number of boxes with duplicates: " + result.getBoxDuplicates().size());
        System.out.println("Pass: " + (result.getBoxDuplicates().size() >= 1));
        System.out.println();
    }
}