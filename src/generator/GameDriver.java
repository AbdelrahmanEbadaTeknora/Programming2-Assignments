package main.java.generator;

import main.java.Models.Game;
import main.java.Models.Board;
import main.java.Models.enums.DifficultyLevel;
import main.java.exceptions.SolutionInvalidException;
import main.java.verification.SudokuVerifier;
import main.java.storage.GameLoader;
import verification.VerificationResult;
import main.java.Models.enums.GameState;

import java.io.IOException;
import java.util.List;

/**
 * Validates source solution and generates three difficulty levels
 * Generates games by removing cells from a valid, complete Sudoku solution
 */
public class GameDriver {
    private SudokuVerifier verifier;
    private GameLoader gameLoader;
    private RandomPairs randomPairs;

    public GameDriver(SudokuVerifier verifier) {
        this.verifier = verifier;
        this.gameLoader = new GameLoader();
        this.randomPairs = new RandomPairs();
    }

    /**
     * Validates source solution and generates three difficulty levels
     * Removes cells according to difficulty:
     * - Easy: 10 cells
     * - Medium: 25 cells
     * - Hard: 20 cells
     *
     * @param source Source game with a valid, complete solution
     * @throws SolutionInvalidException if source is invalid or incomplete
     */
    public void generateGames(Game source) throws SolutionInvalidException {
        if (source == null) {
            throw new SolutionInvalidException("Source game cannot be null");
        }

        // Verify the source solution
        Board sourceBoard = source.getBoard();
        VerificationResult result = verifier.verify(sourceBoard);

        if (!result.isValid()) {
            if (result.getState() == GameState.INCOMPLETE) {
                throw new SolutionInvalidException(
                        "Source solution is incomplete - contains empty cells");
            } else {
                throw new SolutionInvalidException(
                        "Source solution is invalid - contains duplicates or violations");
            }
        }

        // Generate games for each difficulty level
        try {
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.EASY);
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.MEDIUM);
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.HARD);
        } catch (IOException e) {
            throw new SolutionInvalidException("Failed to save generated games", e);
        }
    }

    /**
     * Generates a game for a specific difficulty by removing cells
     */
    private void generateAndSaveGameForDifficulty(Board sourceBoard,
                                                  DifficultyLevel difficulty)
            throws IOException {
        // Create a copy of the source board
        Board gameBoard = sourceBoard.copy();

        // Get the number of cells to remove for this difficulty
        int cellsToRemove = difficulty.getCellsToRemove();

        // Generate random distinct pairs for cell positions
        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);

        // Remove cells from the board
        for (int[] pos : positionsToRemove) {
            int row = pos[0];
            int col = pos[1];
            gameBoard.setValue(row, col, 0);
        }

        // Create game and save it
        Game generatedGame = new Game(gameBoard, difficulty);
        gameLoader.saveGame(generatedGame, difficulty);
    }

    /**
     * Gets number of cells to remove for a difficulty level
     */
    public int getCellsToRemoveCount(DifficultyLevel difficulty) {
        return difficulty.getCellsToRemove();
    }
}