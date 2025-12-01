package com.sudoku.threading.mode27;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator that uses 28 threads total (main + 27 workers)
 * 9 threads for rows, 9 for columns, 9 for boxes
 */
public class TwentySevenThreadValidator {
    private SudokuBoard board;
    private ExecutorService executorService;

    public TwentySevenThreadValidator(SudokuBoard board) {
        this.board = board;
        this.executorService = Executors.newFixedThreadPool(27);
    }

    public ValidationResult validate() throws InterruptedException, ExecutionException {
        List<Future<ValidationResult>> futures = new ArrayList<>();

        // Create 9 row workers (one per row)
        for (int i = 0; i < 9; i++) {
            futures.add(executorService.submit(new SingleRowWorker(board, i)));
        }

        // Create 9 column workers (one per column)
        for (int i = 0; i < 9; i++) {
            futures.add(executorService.submit(new SingleColumnWorker(board, i)));
        }

        // Create 9 box workers (one per box)
        for (int i = 0; i < 9; i++) {
            futures.add(executorService.submit(new SingleBoxWorker(board, i)));
        }

        // Collect and merge all results
        ValidationResult finalResult = new ValidationResult();

        for (Future<ValidationResult> future : futures) {
            ValidationResult partialResult = future.get(); // Block until thread completes
            finalResult.merge(partialResult); // Merge into final result
        }

        // Shutdown executor
        executorService.shutdown();

        return finalResult;
    }
}