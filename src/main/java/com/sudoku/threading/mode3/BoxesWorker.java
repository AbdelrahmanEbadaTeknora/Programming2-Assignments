package com.sudoku.threading.mode3;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.BoxValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates all 9 boxes (3x3 subgrids)
 */
public class BoxesWorker implements Callable<ValidationResult> {
    private SudokuBoard board;

    public BoxesWorker(SudokuBoard board) {
        this.board = board;
    }

    @Override
    public ValidationResult call() {
        // Validate all 9 boxes using BoxValidator
        BoxValidator validator = new BoxValidator(board);
        return validator.validate();
    }
}