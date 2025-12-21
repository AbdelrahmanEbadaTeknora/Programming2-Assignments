package main.java.controller;

import main.java.Models.*;
import main.java.Models.enums.DifficultyLevel;
import main.java.exceptions.*;
import main.java.generator.GameDriver;
import main.java.logging.GameLogger;
import main.java.logging.UndoManager;
import main.java.solver.SudokuSolver;
import main.java.storage.GameCatalogService;
import main.java.storage.GameLoader;
import main.java.utils.UserAction;
import main.java.verification.SudokuVerifier;

import java.io.IOException;
import java.util.List;

/**
 * Main main.javaa.controller implementing both Viewable and Controllable interfaces
 * Coordinates between Model and View layers
 */
public class GameController implements Viewable, Controllable {

    private final SudokuVerifier verifier;
    private final GameDriver gameDriver;
    private final GameLoader gameLoader;
    private final GameCatalogService catalogService;
    private final GameLogger gameLogger;
    private final UndoManager undoManager;
    private final SudokuSolver solver;

    private Game currentGame;

    public GameController() {
        this.verifier = new SudokuVerifier();
        this.gameDriver = new GameDriver(verifier);
        this.gameLoader = new GameLoader();
        this.catalogService = new GameCatalogService();
        this.gameLogger = new GameLogger();
        this.undoManager = new UndoManager(gameLogger);
        this.solver = new SudokuSolver(verifier);
    }@Override
    public Catalog getCatalog() {
        return catalogService.checkGames();
    }

    @Override
    public Game getGame(DifficultyLevel level) throws NotFoundException {
        currentGame = gameLoader.loadGame(level);
        return currentGame;
    }

    @Override
    public void driveGames(Game source) throws SolutionInvalidException {
        gameDriver.generateGames(source);
    }

    @Override
    public String verifyGame(Game game) {
        if (game == null) {
            return "INVALID: Game is null";
        }

        Board board = game.getBoard();
        VerificationResult result = verifier.verify(board);

        switch (result.getState()) {
            case VALID:
                return "VALID";

            case INCOMPLETE:
                return "INCOMPLETE";

            case INVALID:
                StringBuilder sb = new StringBuilder("INVALID");
                List<Position> invalidPositions = result.getInvalidPositions();
                if (!invalidPositions.isEmpty()) {
                    sb.append(": ");
                    for (int i = 0; i < invalidPositions.size(); i++) {
                        Position pos = invalidPositions.get(i);
                        sb.append("(").append(pos.getRow()).append(",")
                                .append(pos.getCol()).append(")");
                        if (i < invalidPositions.size() - 1) {
                            sb.append(", ");
                        }
                    }
                }
                return sb.toString();

            default:
                return "UNKNOWN";
        }
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        if (game == null) {
            throw new InvalidGameException("Game is null");
        }

        Board board = game.getBoard();
        SolutionResult result = solver.solve(board);

        if (result == null || result.getSolution() == null) {
            throw new InvalidGameException("No solution found");
        }

        return result.getSolution();
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        gameLogger.log(userAction);
    }



    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyLevel difficulty;
        switch (Character.toLowerCase(level)) {
            case 'e':
                difficulty = DifficultyLevel.EASY;
                break;
            case 'm':
                difficulty = DifficultyLevel.MEDIUM;
                break;
            case 'h':
                difficulty = DifficultyLevel.HARD;
                break;
            default:
                throw new NotFoundException("Invalid difficulty level: " + level);
        }

        Game game = gameLoader.loadGame(difficulty);
        currentGame = game;
        return game.getBoard().getGrid();
    }

    @Override
    public void driveGames(int[][] source) throws SolutionInvalidException {
        Board board = new Board(source);
        Game sourceGame = new Game(board, DifficultyLevel.EASY); // Temporary difficulty
        gameDriver.generateGames(sourceGame);
    }

    @Override
    public boolean[][] verifyGame(int[][] grid) {
        Board board = new Board(grid);
        VerificationResult result = verifier.verify(board);

        boolean[][] invalidCells = new boolean[9][9];

        // Initialize all to false
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                invalidCells[i][j] = false;
            }
        }

        // Mark invalid positions as true
        if (result.isInvalid()) {
            for (Position pos : result.getInvalidPositions()) {
                invalidCells[pos.getRow()][pos.getCol()] = true;
            }
        }

        return invalidCells;
    }

    @Override
    public int[][] solveGame(int[][] grid) throws InvalidGameException {
        Board board = new Board(grid);
        SolutionResult result = solver.solve(board);

        if (result == null) {
            throw new InvalidGameException("No solution found");
        }

        // Convert solution array to 2D grid format
        int[] solution = result.getSolution();
        List<Position> emptyPositions = result.getEmptyPositions();

        int[][] solvedGrid = new int[9][9];
        // Copy original grid
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, solvedGrid[i], 0, 9);
        }

        // Fill in solutions
        for (int i = 0; i < emptyPositions.size(); i++) {
            Position pos = emptyPositions.get(i);
            solvedGrid[pos.getRow()][pos.getCol()] = solution[i];
        }

        return solvedGrid;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String logEntry = String.format("(%d,%d,%d,%d)",
                userAction.getRow(),
                userAction.getCol(),
                userAction.getNewValue(),
                userAction.getPreviousValue());
        gameLogger.log(logEntry);
    }

    // ==================== Additional Helper Methods ====================

    /**
     * Performs undo operation
     */
    public boolean undo() {
        try {
            return undoManager.undo(currentGame);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves the current game state
     */
    public void saveCurrentGame() throws IOException {
        if (currentGame != null) {
            gameLoader.saveCurrentGame(currentGame);
        }
    }

    /**
     * Deletes completed game
     */
    public void deleteCompletedGame(DifficultyLevel level) throws IOException {
        gameLoader.deleteGame(level);
    }

    /**
     * Gets the current game
     */
    public Game getCurrentGame() {
        return currentGame;
    }

    /**
     * Sets the current game
     */
    public void setCurrentGame(Game game) {
        this.currentGame = game;
    }

    /**
     * Checks if current game is complete and valid
     */
    public boolean isGameCompleteAndValid() {
        if (currentGame == null) return false;

        VerificationResult result = verifier.verify(currentGame.getBoard());
        return result.isValid();
    }

    /**
     * Gets number of empty cells in current game
     */
    public int getEmptyCellCount() {
        if (currentGame == null) return 0;

        int count = 0;
        int[][] grid = currentGame.getBoard().getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) count++;
            }
        }
        return count;
    }
}