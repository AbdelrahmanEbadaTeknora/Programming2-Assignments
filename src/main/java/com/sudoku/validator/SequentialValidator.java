package com.sudoku.validator;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;

/**
 * Sequential validator for Mode 0.
 * Validates the entire Sudoku board using a single thread (main thread).
 *
 * This validator uses composition to combine results from:
 * - RowValidator (validates all 9 rows)
 * - ColumnValidator (validates all 9 columns)
 * - BoxValidator (validates all 9 boxes)
 *
 * @author Member 2
 */
public class SequentialValidator {
    private SudokuBoard board;
    private RowValidator rowValidator;
    private ColumnValidator columnValidator;
    private BoxValidator boxValidator;

    /**
     * Constructor for SequentialValidator
     * Initializes all three validators
     *
     * @param board the Sudoku board to validate
     */
    public SequentialValidator(SudokuBoard board) {
        this.board = board;
        this.rowValidator = new RowValidator(board);
        this.columnValidator = new ColumnValidator(board);
        this.boxValidator = new BoxValidator(board);
    }

    /**
     * Validates the entire Sudoku board sequentially.
     *
     * Process:
     * 1. Validates all rows
     * 2. Validates all columns
     * 3. Validates all boxes
     * 4. Merges all results
     *
     * IMPORTANT: Always processes the entire board (no shortcuts),
     * even if duplicates are found early.
     *
     * @return ValidationResult containing all duplicates found (if any)
     */
    public ValidationResult validate() {
        ValidationResult finalResult = new ValidationResult();

        // Step 1: Validate all rows
        ValidationResult rowResult = rowValidator.validate();
        finalResult.merge(rowResult);

        // Step 2: Validate all columns
        ValidationResult columnResult = columnValidator.validate();
        finalResult.merge(columnResult);

        // Step 3: Validate all boxes
        ValidationResult boxResult = boxValidator.validate();
        finalResult.merge(boxResult);

        // Return the merged result containing all duplicates
        return finalResult;
    }

    /**
     * Gets the board being validated
     *
     * @return the SudokuBoard instance
     */
    public SudokuBoard getBoard() {
        return board;
    }
}