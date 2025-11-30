package com.sudoku.threading.mode3;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator that uses 4 threads total (main + 3 workers)
 * One thread for all rows, one for all columns, one for all boxes
 */
public class ThreeThreadValidator {
    private SudokuBoard board;
    private ExecutorService executorService;

    public ThreeThreadValidator(SudokuBoard board) {
        this.board = board;
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public ValidationResult validate() throws InterruptedException, ExecutionException {
        // Create 3 workers
        RowsWorker rowsWorker = new RowsWorker(board);
        ColumnsWorker columnsWorker = new ColumnsWorker(board);
        BoxesWorker boxesWorker = new BoxesWorker(board);

        // Submit to executor
        List<Future<ValidationResult>> futures = new ArrayList<>();
        futures.add(executorService.submit(rowsWorker));
        futures.add(executorService.submit(columnsWorker));
        futures.add(executorService.submit(boxesWorker));

        // Collect and merge results
        ValidationResult finalResult = new ValidationResult();

        for (Future<ValidationResult> future : futures) {
            ValidationResult partialResult = future.get(); // This blocks until the thread completes
            finalResult.merge(partialResult); // Merge this thread's results into final result
        }

        // Shutdown executor
        executorService.shutdown();

        return finalResult;
    }
}