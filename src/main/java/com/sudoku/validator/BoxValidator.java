package com.sudoku.validator;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.List;

/**
 * Validates all 3x3 boxes in the Sudoku board.
 * Checks each box for duplicate values.
 *
 * Box numbering (0-8 internally, 1-9 for output):
 * ┌─────┬─────┬─────┐
 * │  0  │  1  │  2  │
 * ├─────┼─────┼─────┤
 * │  3  │  4  │  5  │
 * ├─────┼─────┼─────┤
 * │  6  │  7  │  8  │
 * └─────┴─────┴─────┘
 *
 * @author Member 2
 */
public class BoxValidator extends Validator {

    /**
     * Constructor for BoxValidator
     * @param board the Sudoku board to validate
     */
    public BoxValidator(SudokuBoard board) {
        super(board);
    }

    /**
     * Validates all 9 boxes (3x3 regions) of the Sudoku board
     * Iterates through each box and checks for duplicates
     *
     * @return ValidationResult containing all box duplicates found
     */
    @Override
    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        // Validate all 9 boxes
        for (int boxIndex = 0; boxIndex < 9; boxIndex++) {
            ValidationResult boxResult = validateBox(boxIndex);
            result.merge(boxResult);
        }

        return result;
    }

    /**
     * Validates a single 3x3 box for duplicates
     * Uses BoxIndexCalculator to map box index to board positions
     *
     * @param boxIndex the box index (0-8)
     * @return ValidationResult containing any duplicates found in this box
     */
    public ValidationResult validateBox(int boxIndex) {
        ValidationResult result = new ValidationResult();

        // Get the box values from the board using BoxIndexCalculator
        int[] boxValues = board.getBox(boxIndex);

        // Find all duplicates in this box
        List<Integer> duplicates = findDuplicates(boxValues);

        // For each duplicate value, find all positions where it appears
        for (int duplicate : duplicates) {
            List<Integer> positions = findPositions(boxValues, duplicate);
            // Add to result with 1-based box numbering
            result.addBoxDuplicate(boxIndex + 1, duplicate, positions);
        }

        return result;
    }
}