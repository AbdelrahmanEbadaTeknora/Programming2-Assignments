package com.sudoku.threading.mode27;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.validator.ColumnValidator;
import java.util.concurrent.Callable;

/**
 * Worker thread that validates a single column
 */
public class SingleColumnWorker implements Callable<ValidationResult> {
    private SudokuBoard board;
    private int columnIndex;

    public SingleColumnWorker(SudokuBoard board, int columnIndex) {
        this.board = board;
        this.columnIndex = columnIndex;
    }

    @Override
    public ValidationResult call() {
        // Validate single column using ColumnValidator
        ColumnValidator validator = new ColumnValidator(board);
        return validator.validateColumn(columnIndex);
    }
}