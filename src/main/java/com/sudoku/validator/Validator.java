package com.sudoku.validator;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for all validators.
 * Provides common functionality for duplicate detection.
 */
public abstract class Validator {
    protected SudokuBoard board;

    public Validator(SudokuBoard board) {
        this.board = board;
    }

    /**
     * Template method - each validator implements its own validation logic
     */
    public abstract ValidationResult validate();

    /**
     * Finds all duplicate values in an array
     * @param values array of integers to check
     * @return list of values that appear more than once
     */
    protected List<Integer> findDuplicates(int[] values) {
        Map<Integer, Integer> frequency = new HashMap<>();
        List<Integer> duplicates = new ArrayList<>();

        // Count frequency of each value
        for (int value : values) {
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
        }

        // Find values that appear more than once
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > 1) {
                duplicates.add(entry.getKey());
            }
        }

        return duplicates;
    }

    /**
     * Finds all positions where a duplicate value appears
     * @param values array to search in
     * @param duplicateValue the value to find
     * @return list of 0-based positions where the value appears
     */
    protected List<Integer> findPositions(int[] values, int duplicateValue) {
        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            if (values[i] == duplicateValue) {
                positions.add(i);
            }
        }

        return positions;
    }

    /**
     * Checks if an array contains duplicates
     * @param values array to check
     * @return true if duplicates exist, false otherwise
     */
    protected boolean hasDuplicates(int[] values) {
        return !findDuplicates(values).isEmpty();
    }
}