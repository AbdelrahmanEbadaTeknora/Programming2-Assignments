package main.java.verification;

import main.java.Models.Board;
import main.java.Models.Position;
import main.java.Models.VerificationResult;
import main.java.Models.enums.GameState;

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

        // Check for incomplete cells (zeros)
        if (hasEmptyCells(grid)) {
            return new VerificationResult(GameState.INCOMPLETE,
                    new ArrayList<>(), "Board has empty cells");
        }

        // Check for invalid duplicates
        List<Position> invalidPositions = findInvalidPositions(grid);

        if (!invalidPositions.isEmpty()) {
            return new VerificationResult(GameState.INVALID,
                    invalidPositions, "Board has duplicate numbers");
        }

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

        // Check all rows
        for (int row = 0; row < BOARD_SIZE; row++) {
            invalidPositions.addAll(checkRow(grid, row));
        }

        // Check all columns
        for (int col = 0; col < BOARD_SIZE; col++) {
            invalidPositions.addAll(checkColumn(grid, col));
        }

        // Check all 3x3 boxes
        for (int boxRow = 0; boxRow < BOARD_SIZE; boxRow += BOX_SIZE) {
            for (int boxCol = 0; boxCol < BOARD_SIZE; boxCol += BOX_SIZE) {
                invalidPositions.addAll(checkBox(grid, boxRow, boxCol));
            }
        }

        return invalidPositions;
    }

    private List<Position> checkRow(int[][] grid, int row) {
        List<Position> duplicates = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicateValues = new HashSet<>();

        for (int col = 0; col < BOARD_SIZE; col++) {
            int value = grid[row][col];
            if (value != 0) {
                if (seen.contains(value)) {
                    duplicateValues.add(value);
                }
                seen.add(value);
            }
        }

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

        for (int row = 0; row < BOARD_SIZE; row++) {
            int value = grid[row][col];
            if (value != 0) {
                if (seen.contains(value)) {
                    duplicateValues.add(value);
                }
                seen.add(value);
            }
        }

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

        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = grid[row][col];
                if (value != 0) {
                    if (seen.contains(value)) {
                        duplicateValues.add(value);
                    }
                    seen.add(value);
                }
            }
        }

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