package com.sudoku.threading.mode27;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.RowValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates a single row
 */
public class SingleRowWorker implements Callable<ValidationResult> {
    private SudokuBoard board;
    private int rowIndex;

    public SingleRowWorker(SudokuBoard board, int rowIndex) {
        this.board = board;
        this.rowIndex = rowIndex;
    }

    @Override
    public ValidationResult call() {
        // Validate single row using RowValidator
        RowValidator validator = new RowValidator(board);
        return validator.validateRow(rowIndex);
    }
}