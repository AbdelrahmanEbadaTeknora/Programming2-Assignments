package generator;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.SolutionInvalidException;
import verification.SudokuVerifier;
import verification.VerificationResult;
import Models.enums.GameState;
import utils.Constants;

import java.io.File;

/**
 * Validates source solution and generates three difficulty levels
 * Generates games by removing cells from a valid, complete Sudoku solution
 * Saves generated games to appropriate difficulty folders
 */
public class GameDriver {
    private SudokuVerifier verifier;
    private GameGenerator gameGenerator;

    public GameDriver(SudokuVerifier verifier) {
        this.verifier = verifier;
        this.gameGenerator = new GameGenerator(verifier);
    }

    /**
     * Validates source solution and generates three difficulty levels
     * Removes cells according to difficulty:
     * - Easy: 10 cells removed
     * - Medium: 20 cells removed
     * - Hard: 25 cells removed
     *
     * @throws SolutionInvalidException if source is invalid or incomplete
     */
    public void generateGames(Game source) throws SolutionInvalidException {
        if (source == null) {
            throw new SolutionInvalidException("Source game cannot be null");
        }

        System.out.println("\n=== Starting Game Generation ===");

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

        System.out.println("Source solution verified - VALID");

        // Generate games for each difficulty level
        try {
            System.out.println("\nGenerating EASY (10 cells to remove)...");
            Game easyGame = gameGenerator.generateGame(sourceBoard, DifficultyLevel.EASY);

            System.out.println("\nGenerating MEDIUM (25 cells to remove)...");
            Game mediumGame = gameGenerator.generateGame(sourceBoard, DifficultyLevel.MEDIUM);

            System.out.println("\nGenerating HARD (20 cells to remove)...");
            Game hardGame = gameGenerator.generateGame(sourceBoard, DifficultyLevel.HARD);

            // Save the generated games to their respective folders
            System.out.println("\n=== Saving Games ===");

            String easyFolder = Constants.GAMES_FOLDER + File.separator + Constants.EASY_FOLDER;
            String mediumFolder = Constants.GAMES_FOLDER + File.separator + Constants.MEDIUM_FOLDER;
            String hardFolder = Constants.GAMES_FOLDER + File.separator + Constants.HARD_FOLDER;

            gameGenerator.saveGameToFile(easyGame, easyFolder);
            gameGenerator.saveGameToFile(mediumGame, mediumFolder);
            gameGenerator.saveGameToFile(hardGame, hardFolder);

            System.out.println("\n=== Generation Complete ===");
            System.out.println("Easy:   " + easyGame.getEmptyCellCount() + " empty cells");
            System.out.println("Medium: " + mediumGame.getEmptyCellCount() + " empty cells");
            System.out.println("Hard:   " + hardGame.getEmptyCellCount() + " empty cells\n");

        } catch (Exception e) {
            System.err.println("ERROR during generation: " + e.getMessage());
            e.printStackTrace();
            throw new SolutionInvalidException("Failed to generate or save games: " + e.getMessage(), e);
        }
    }
}