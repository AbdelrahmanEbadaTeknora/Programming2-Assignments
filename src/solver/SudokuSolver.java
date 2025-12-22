package solver;

import Models.Board;
import solver.SolutionResult;
import verification.SudokuVerifier;
import OptionalHelperClasses.Position;

import java.util.ArrayList;
import java.util.List;


public class SudokuSolver {
    private SudokuVerifier verifier;
    private static final int MAX_EMPTY_CELLS = 5;

    public SudokuSolver(SudokuVerifier verifier) {
        this.verifier = verifier;
    }


    public SolutionResult solve(Board board) {
        if (board == null) {
            return null;
        }

        // Find all empty positions
        List<Position> emptyPositions = findEmptyPositions(board);

        // Check if board has exactly 5 empty cells
        if (emptyPositions.size() != MAX_EMPTY_CELLS) {
            return null;
        }

        // Create iterator for permutations
        PermutationIterator iterator = new PermutationIterator(emptyPositions);

        // Try each permutation
        while (iterator.hasNext()) {
            int[] permutation = iterator.next();

            // Check if this permutation results in a valid board
            if (BoardFlyweight.isValidWithPermutation(board, emptyPositions, permutation)) {
                // Found valid solution
                return new SolutionResult(permutation, emptyPositions, true);
            }
        }

        // No solution found
        return new SolutionResult(null, emptyPositions, false);
    }

    private List<Position> findEmptyPositions(Board board) {
        List<Position> emptyPositions = new ArrayList<>();
        int[][] grid = board.getGrid();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    emptyPositions.add(new Position(row, col));
                }
            }
        }

        return emptyPositions;
    }

    public static int getMaxEmptyCells() {
        return MAX_EMPTY_CELLS;
    }
}