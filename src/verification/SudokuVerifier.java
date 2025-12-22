package verification;

import Models.Board;
import OptionalHelperClasses.Position;
import Models.enums.GameState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuVerifier {

    private static final int BOARD_SIZE = 9;
    private static final int BOX_SIZE = 3;


    public VerificationResult verify(Board board) {
        if (board == null) {
            return new VerificationResult(GameState.INVALID,
                    new ArrayList<>(), "Board is null");
        }

        int[][] grid = board.getGrid();


        List<Position> invalidPositions = findInvalidPositions(grid);

        if (!invalidPositions.isEmpty()) {
            // Board has duplicates - it's INVALID
            return new VerificationResult(GameState.INVALID,
                    invalidPositions, "Board has duplicate numbers");
        }


        if (hasEmptyCells(grid)) {
            // No duplicates but has empty cells - it's INCOMPLETE
            return new VerificationResult(GameState.INCOMPLETE,
                    new ArrayList<>(), "Board has empty cells");
        }

        // No duplicates and no empty cells - it's VALID
        return new VerificationResult(GameState.VALID,
                new ArrayList<>(), "Board is valid");
    }

    private boolean hasEmptyCells(int[][] grid) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (grid[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Position> findInvalidPositions(int[][] grid) {
        List<Position> invalidPositions = new ArrayList<>();
        Set<Position> invalidSet = new HashSet<>();

        // Check rows
        for (int row = 0; row < BOARD_SIZE; row++) {
            List<Position> rowInvalids = checkRow(grid, row);
            invalidSet.addAll(rowInvalids);
        }

        // Check columns
        for (int col = 0; col < BOARD_SIZE; col++) {
            List<Position> colInvalids = checkColumn(grid, col);
            invalidSet.addAll(colInvalids);
        }

        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < BOARD_SIZE; boxRow += BOX_SIZE) {
            for (int boxCol = 0; boxCol < BOARD_SIZE; boxCol += BOX_SIZE) {
                List<Position> boxInvalids = checkBox(grid, boxRow, boxCol);
                invalidSet.addAll(boxInvalids);
            }
        }

        // Convert set to list
        invalidPositions.addAll(invalidSet);

        // Debug output
        if (!invalidPositions.isEmpty()) {
            System.out.println("Found " + invalidPositions.size() + " invalid positions:");
            for (Position p : invalidPositions) {
                System.out.println("  Position (" + p.getRow() + "," + p.getCol() + ") = " + grid[p.getRow()][p.getCol()]);
            }
        }

        return invalidPositions;
    }


    private List<Position> checkRow(int[][] grid, int row) {
        List<Position> duplicates = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();

        // First pass: find which values are duplicated (only check filled cells)
        for (int col = 0; col < BOARD_SIZE; col++) {
            int value = grid[row][col];
            if (value != 0) {  // Only check filled cells
                if (seen.contains(value)) {
                    duplicateValues.add(value);
                }
                seen.add(value);
            }
        }

        // Second pass: mark all positions with duplicate values
        for (int col = 0; col < BOARD_SIZE; col++) {
            int value = grid[row][col];
            if (duplicateValues.contains(value)) {
                duplicates.add(new Position(row, col));
            }
        }

        return duplicates;
    }


    private List<Position> checkColumn(int[][] grid, int col) {
        List<Position> duplicates = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();

        // First pass: find which values are duplicated (only check filled cells)
        for (int row = 0; row < BOARD_SIZE; row++) {
            int value = grid[row][col];
            if (value != 0) {  // Only check filled cells
                if (seen.contains(value)) {
                    duplicateValues.add(value);
                }
                seen.add(value);
            }
        }

        // Second pass: mark all positions with duplicate values
        for (int row = 0; row < BOARD_SIZE; row++) {
            int value = grid[row][col];
            if (duplicateValues.contains(value)) {
                duplicates.add(new Position(row, col));
            }
        }

        return duplicates;
    }


    private List<Position> checkBox(int[][] grid, int startRow, int startCol) {
        List<Position> duplicates = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();

        // First pass: find which values are duplicated (only check filled cells)
        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = grid[row][col];
                if (value != 0) {  // Only check filled cells
                    if (seen.contains(value)) {
                        duplicateValues.add(value);
                    }
                    seen.add(value);
                }
            }
        }

        // Second pass: mark all positions with duplicate values
        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = grid[row][col];
                if (duplicateValues.contains(value)) {
                    duplicates.add(new Position(row, col));
                }
            }
        }

        return duplicates;
    }

    public boolean isValid(Board board) {
        return verify(board).isValid();
    }

    public boolean isComplete(Board board) {
        if (board == null) return false;
        return !hasEmptyCells(board.getGrid());
    }
}