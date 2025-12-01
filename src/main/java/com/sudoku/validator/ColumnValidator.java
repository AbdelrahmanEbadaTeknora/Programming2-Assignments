package com.sudoku.validator;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.List;

/**
 * Validates all columns in the Sudoku board.
 * Checks each column for duplicate values.
 *
 * @author Member 2
 */
public class ColumnValidator extends Validator {

    /**
     * Constructor for ColumnValidator
     * @param board the Sudoku board to validate
     */
    public ColumnValidator(SudokuBoard board) {
        super(board);
    }

    /**
     * Validates all 9 columns of the Sudoku board
     * Iterates through each column and checks for duplicates
     *
     * @return ValidationResult containing all column duplicates found
     */
    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        // Validate all 9 columns
        for (int colIndex = 0; colIndex < 9; colIndex++) {
            ValidationResult colResult = validateColumn(colIndex);
            result.merge(colResult);
        }

        return result;
    }

    /**
     * Validates a single column for duplicates
     *
     * @param colIndex the column index (0-8)
     * @return ValidationResult containing any duplicates found in this column
     */
    public ValidationResult validateColumn(int colIndex) {
        ValidationResult result = new ValidationResult();

        // Get the column values from the board
        int[] columnValues = board.getColumn(colIndex);

        // Find all duplicates in this column
        List<Integer> duplicates = findDuplicates(columnValues);

        // For each duplicate value, find all positions where it appears
        for (int duplicate : duplicates) {
            List<Integer> positions = findPositions(columnValues, duplicate);
            // Add to result with 1-based column numbering
            result.addColumnDuplicate(colIndex + 1, duplicate, positions);
        }

        return result;
    }
}
