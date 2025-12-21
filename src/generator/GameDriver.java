package generator;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.SolutionInvalidException;
import verification.SudokuVerifier;
import storageAndLogging.GameLoader;
import verification.VerificationResult;
import Models.enums.GameState;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.*;

/**
 * Validates source solution and generates three difficulty levels
 * Generates games by removing cells from a valid, complete Sudoku solution
 */
public class GameDriver {
    private SudokuVerifier verifier;
    private GameLoader gameLoader;
    private Random random;

    public GameDriver(SudokuVerifier verifier) {
        this.verifier = verifier;
        this.gameLoader = new GameLoader();
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * Validates source solution and generates three difficulty levels
     */
    public void generateGames(Game source) throws SolutionInvalidException {
        System.out.println("\n=== DEBUG [GameDriver.generateGames] ===");
        System.out.println("DEBUG: Source game: " + (source != null ? "not null" : "null"));

        if (source == null) {
            System.out.println("DEBUG: Source is null - throwing exception");
            throw new SolutionInvalidException("Source game cannot be null");
        }

        // Verify the source solution
        System.out.println("DEBUG: Getting source board...");
        Board sourceBoard = source.getBoard();
        System.out.println("DEBUG: Source board obtained");

        System.out.println("DEBUG: Verifying source solution...");
        VerificationResult result = verifier.verify(sourceBoard);
        System.out.println("DEBUG: Verification result: " + result);

        if (!result.isValid()) {
            System.out.println("DEBUG: Source is NOT valid!");
            if (result.getState() == GameState.INCOMPLETE) {
                System.out.println("DEBUG: Throwing INCOMPLETE exception");
                throw new SolutionInvalidException(
                        "Source solution is incomplete - contains empty cells");
            } else {
                System.out.println("DEBUG: Throwing INVALID exception");
                throw new SolutionInvalidException(
                        "Source solution is invalid - contains duplicates or violations");
            }
        }

        System.out.println("DEBUG: Source is valid! Generating games...");

        // Generate games for each difficulty level
        try {
            System.out.println("DEBUG: Generating EASY game...");
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.EASY);
            System.out.println("DEBUG: EASY game generated");

            System.out.println("DEBUG: Generating MEDIUM game...");
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.MEDIUM);
            System.out.println("DEBUG: MEDIUM game generated");

            System.out.println("DEBUG: Generating HARD game...");
            generateAndSaveGameForDifficulty(sourceBoard, DifficultyLevel.HARD);
            System.out.println("DEBUG: HARD game generated");

            System.out.println("DEBUG: All games generated successfully!");
        } catch (IOException e) {
            System.err.println("DEBUG: IOException: " + e.getMessage());
            e.printStackTrace();
            throw new SolutionInvalidException("Failed to save generated games", e);
        }
    }


    /**
     * Generates a game for a specific difficulty by removing cells
     */
    private void generateAndSaveGameForDifficulty(Board sourceBoard,
                                                  DifficultyLevel difficulty)
            throws IOException {
        // Create a DEEP copy of the source board
        int[][] sourceGrid = sourceBoard.getGrid();
        int[][] gameGrid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(sourceGrid[i], 0, gameGrid[i], 0, 9);
        }
        Board gameBoard = new Board(gameGrid);

        // Get the number of cells to remove for this difficulty
        int cellsToRemove = 10;

        // Generate unique positions to remove
        List<int[]> positionsToRemove = generateRandomPositions(cellsToRemove);

        System.out.println("Generated " + difficulty + " game: removing " +
                positionsToRemove.size() + " cells");

        // Remove cells from the board
        for (int[] pos : positionsToRemove) {
            int row = pos[0];
            int col = pos[1];
            gameBoard.setValue(row, col, 0);
        }

        // Create game and save it
        Game generatedGame = new Game(gameBoard, difficulty);
        gameLoader.saveGame(generatedGame, difficulty);

        // Verify we removed the right number
        int actualEmpty = generatedGame.getEmptyCellCount();
        System.out.println("  -> Actual empty cells: " + actualEmpty +
                " (expected: " + cellsToRemove + ")");
    }

    /**
     * Generate unique random positions
     */
    private List<int[]> generateUniquePositions(int count) {
        // Generate all possible positions (0,0) to (8,8)
        java.util.List<int[]> allPositions = new java.util.ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                allPositions.add(new int[]{row, col});
            }
        }

        // Shuffle using Fisher-Yates algorithm
        for (int i = allPositions.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = allPositions.get(i);
            allPositions.set(i, allPositions.get(j));
            allPositions.set(j, temp);
        }

        // Return first 'count' positions
        return allPositions.subList(0, count);
    }

    private List<int[]> generateRandomPositions(int count) {
        Set<String> used = new HashSet<>();
        List<int[]> result = new ArrayList<>();
        Random rand = new Random(System.nanoTime()); // High resolution timer

        while (result.size() < count) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            String key = row + "-" + col;

            if (!used.contains(key)) {
                used.add(key);
                result.add(new int[]{row, col});
            }
        }

        return result;
    }

    /**
     * Gets number of cells to remove for a difficulty level
     */
    public int getCellsToRemoveCount(DifficultyLevel difficulty) {
        return difficulty.getCellsToRemove();
    }
}