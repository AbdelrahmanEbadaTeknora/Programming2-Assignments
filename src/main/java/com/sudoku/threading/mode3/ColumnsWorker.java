package com.sudoku.threading.mode3;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.ColumnValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates all 9 columns
 */
public class ColumnsWorker implements Callable<ValidationResult> {
    private SudokuBoard board;

    public ColumnsWorker(SudokuBoard board) {
        this.board = board;
    }

    @Override
    public ValidationResult call() {
        // Validate all 9 columns using ColumnValidator
        ColumnValidator validator = new ColumnValidator(board);
        return validator.validate();
    }
}