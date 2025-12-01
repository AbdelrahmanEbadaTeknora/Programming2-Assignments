package com.sudoku.factory;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.SequentialValidator;
import com.sudoku.threading.mode3.ThreeThreadValidator;
import com.sudoku.threading.mode27.TwentySevenThreadValidator;

import java.util.concurrent.ExecutionException;

/**
 * Factory class for creating appropriate validators based on mode.
 * Implements the Factory Design Pattern (required by project specification).
 *
 * This factory creates different validator strategies:
 * - Mode 0: Sequential validator (single thread)
 * - Mode 3: Three-thread validator (1 main + 3 workers)
 * - Mode 27: Twenty-seven-thread validator (1 main + 27 workers)
 *
 * @author All Members (Shared)
 */
public class ValidatorFactory {

    /**
     * Creates and returns the appropriate validator based on the mode.
     *
     * @param board The Sudoku board to validate
     * @param mode The validation mode (0, 3, or 27)
     * @return A Validator interface implementation
     * @throws IllegalArgumentException if mode is invalid
     */
    public static Validator createValidator(SudokuBoard board, int mode) {
        switch (mode) {
            case 0:
                return new SequentialValidatorAdapter(board);
            case 3:
                return new ThreeThreadValidatorAdapter(board);
            case 27:
                return new TwentySevenThreadValidatorAdapter(board);
            default:
                throw new IllegalArgumentException(
                        "Invalid mode: " + mode + ". Valid modes are 0, 3, or 27."
                );
        }
    }

    /**
     * Validator interface that all validators must implement.
     * This provides a uniform interface for all validation strategies.
     */
    public interface Validator {
        /**
         * Validates the Sudoku board
         * @return ValidationResult containing the validation outcome
         * @throws InterruptedException if validation is interrupted
         * @throws ExecutionException if an execution error occurs
         */
        ValidationResult validate() throws InterruptedException, ExecutionException;
    }

    /**
     * Adapter for SequentialValidator (Mode 0)
     * Wraps the sequential validator to implement the Validator interface
     */
    private static class SequentialValidatorAdapter implements Validator {
        private final SequentialValidator validator;

        public SequentialValidatorAdapter(SudokuBoard board) {
            this.validator = new SequentialValidator(board);
        }

        @Override
        public ValidationResult validate() {
            return validator.validate();
        }
    }

    /**
     * Adapter for ThreeThreadValidator (Mode 3)
     * Wraps the three-thread validator to implement the Validator interface
     */
    private static class ThreeThreadValidatorAdapter implements Validator {
        private final ThreeThreadValidator validator;

        public ThreeThreadValidatorAdapter(SudokuBoard board) {
            this.validator = new ThreeThreadValidator(board);
        }

        @Override
        public ValidationResult validate() throws InterruptedException, ExecutionException {
            return validator.validate();
        }
    }

    /**
     * Adapter for TwentySevenThreadValidator (Mode 27)
     * Wraps the twenty-seven-thread validator to implement the Validator interface
     */
    private static class TwentySevenThreadValidatorAdapter implements Validator {
        private final TwentySevenThreadValidator validator;

        public TwentySevenThreadValidatorAdapter(SudokuBoard board) {
            this.validator = new TwentySevenThreadValidator(board);
        }

        @Override
        public ValidationResult validate() throws InterruptedException, ExecutionException {
            return validator.validate();
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a factory class and should only be used via static methods.
     */
    private ValidatorFactory() {
        throw new UnsupportedOperationException(
                "ValidatorFactory is a utility class and cannot be instantiated"
        );
    }
}