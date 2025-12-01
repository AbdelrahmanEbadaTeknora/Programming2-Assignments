package com.sudoku.validator;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.List;

/**
 * Validates all rows in the Sudoku board
 */
public class RowValidator extends Validator {

    public RowValidator(SudokuBoard board) {
        super(board);
    }

    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        // Validate all 9 rows
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            ValidationResult rowResult = validateRow(rowIndex);
            result.merge(rowResult);
        }

        return result;
    }

    /**
     * Validates a single row
     * @param rowIndex the row index (0-8)
     * @return ValidationResult containing any duplicates found
     */
    public ValidationResult validateRow(int rowIndex) {
        ValidationResult result = new ValidationResult();

        // Get the row values
        int[] rowValues = board.getRow(rowIndex);

        // Find duplicates in this row
        List<Integer> duplicates = findDuplicates(rowValues);

        // For each duplicate, record its positions
        for (int duplicate : duplicates) {
            List<Integer> positions = findPositions(rowValues, duplicate);
            result.addRowDuplicate(rowIndex + 1, duplicate, positions); // +1 for 1-based indexing
        }

        return result;
    }
}