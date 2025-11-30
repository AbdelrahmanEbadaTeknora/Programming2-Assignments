package com.sudoku.threading.mode3;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.RowValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates all 9 rows
 */
public class RowsWorker implements Callable<ValidationResult> {
    private SudokuBoard board;

    public RowsWorker(SudokuBoard board) {
        this.board = board;
    }

    @Override
    public ValidationResult call() {
        // Validate all 9 rows using RowValidator
        RowValidator validator = new RowValidator(board);
        return validator.validate();
    }
}