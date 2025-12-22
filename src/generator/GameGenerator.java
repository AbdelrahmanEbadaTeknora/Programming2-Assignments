package generator;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.SolutionInvalidException;
import verification.SudokuVerifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Generates Sudoku games from a solved board
 * Creates playable puzzles by removing cells based on difficulty level
 * Also handles saving generated games to disk
 */
public class GameGenerator {
    private SudokuVerifier verifier;
    private RandomPairs randomPairs;

    /**
     * Constructor with custom verifier
     */
    public GameGenerator(SudokuVerifier verifier) {
        this.verifier = verifier;
        this.randomPairs = new RandomPairs();
    }

    public Game generateGame(Board solvedBoard, DifficultyLevel difficulty)
            throws SolutionInvalidException {
        validateSourceBoard(solvedBoard);

        // Create NEW RandomPairs instance - CRITICAL for different random sequences
        RandomPairs randomPairs = new RandomPairs();

        return generateGameWithRandomPairs(solvedBoard, difficulty, randomPairs);
    }

    /**
     * Internal method that generates game with specific RandomPairs instance
     */
    private Game generateGameWithRandomPairs(Board solvedBoard, DifficultyLevel difficulty,
                                             RandomPairs randomPairs) throws SolutionInvalidException {
        // Create a copy of the solved board
        Board gameBoard = solvedBoard.copy();

        // Get number of cells to remove for this difficulty
        int cellsToRemove = difficulty.getCellsToRemove();

        System.out.println("Generating " + difficulty + " - removing " + cellsToRemove + " cells");

        // Generate random positions to remove
        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);

        System.out.println("Generated " + positionsToRemove.size() + " random positions");

        // Remove cells from the board
        for (int[] pos : positionsToRemove) {
            int row = pos[0];
            int col = pos[1];
            int oldValue = gameBoard.getValue(row, col);
            gameBoard.setValue(row, col, 0);
            System.out.println("  Removed cell (" + row + "," + col + ") was: " + oldValue);
        }

        // Verify cells were actually removed
        int emptyCells = countEmptyCells(gameBoard);
        System.out.println("Final empty cells: " + emptyCells);

        // Create and return game
        Game game = new Game(gameBoard, difficulty);
        return game;
    }

    /**
     * Counts empty cells in a board
     */
    private int countEmptyCells(Board board) {
        int count = 0;
        int[][] grid = board.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Saves generated game to file
     */
    public void saveGameToFile(Game game, String folderPath) throws IOException {
        // Create folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new IOException("Failed to create folder: " + folderPath);
            }
        }

        // Create filename with timestamp
        String filename = "game_" + System.currentTimeMillis() + ".txt";
        String filePath = folderPath + File.separator + filename;

        // Write board to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int[][] grid = game.getBoard().getGrid();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(String.valueOf(grid[i][j]));
                    if (j < 8) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }
            System.out.println("Saved " + game.getDifficulty() + " game to: " + filePath);
        }
    }

    /**
     * Validates that source board is valid and complete
     * @throws SolutionInvalidException if board is not valid or incomplete
     */
    private void validateSourceBoard(Board board) throws SolutionInvalidException {
        if (board == null) {
            throw new SolutionInvalidException("Source board cannot be null");
        }

        verification.VerificationResult result = verifier.verify(board);

        if (!result.isValid()) {
            if (result.getState() == Models.enums.GameState.INCOMPLETE) {
                throw new SolutionInvalidException(
                        "Source board is incomplete - contains empty cells (0s)");
            } else {
                throw new SolutionInvalidException(
                        "Source board is invalid - contains duplicate numbers or rule violations");
            }
        }
    }

}