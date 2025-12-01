package com.sudoku.threading.mode27;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.BoxValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates a single 3x3 box
 */
public class SingleBoxWorker implements Callable<ValidationResult> {
    private SudokuBoard board;
    private int boxIndex;

    public SingleBoxWorker(SudokuBoard board, int boxIndex) {
        this.board = board;
        this.boxIndex = boxIndex;
    }

    @Override
    public ValidationResult call() {
        // Validate single box using BoxValidator
        BoxValidator validator = new BoxValidator(board);
        return validator.validateBox(boxIndex);
    }
}