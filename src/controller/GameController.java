package controller;

// Model imports
import Models.*;
import Models.enums.*;

// Exception imports
import exceptions.*;

// Generator imports
import generator.*;

// Storage and Logging imports
import storageAndLogging.*;

// Solver imports
import solver.*;

// Helper imports
import OptionalHelperClasses.UserAction;
import OptionalHelperClasses.Position;

// Verification imports
import verification.*;

import java.io.IOException;
import java.util.List;


public class GameController implements Viewable, Controllable {

    private final verification.SudokuVerifier verifier;
    private final generator.GameDriver gameDriver;
    private final GameLoader gameLoader;
    private final GameCatalogService catalogService;
    private final GameLogger gameLogger;
    private final UndoManager undoManager;
    private final solver.SudokuSolver solver;

    private Models.Game currentGame;

    public GameController() {
        this.verifier = new verification.SudokuVerifier();
        this.gameDriver = new generator.GameDriver(verifier);
        this.gameLoader = new GameLoader();
        this.catalogService = new GameCatalogService();
        this.gameLogger = new GameLogger();
        this.undoManager = new UndoManager(gameLogger);
        this.solver = new solver.SudokuSolver(verifier);
    }

    // ==================== Viewable Interface Implementation ====================

    @Override
    public Catalog getCatalog() {
        return catalogService.checkGames();
    }






    // ==================== Controllable Interface Implementation ====================

    @Override
    public int[][] getGame(char level) throws exceptions.NotFoundException {
        Models.enums.DifficultyLevel difficulty;
        switch (Character.toLowerCase(level)) {
            case 'e':
                difficulty = Models.enums.DifficultyLevel.EASY;
                break;
            case 'm':
                difficulty = Models.enums.DifficultyLevel.MEDIUM;
                break;
            case 'h':
                difficulty = Models.enums.DifficultyLevel.HARD;
                break;
            default:
                throw new exceptions.NotFoundException("Invalid difficulty level: " + level);
        }

        Models.Game game = gameLoader.loadGame(difficulty);
        currentGame = game;
        return game.getBoard().getGrid();
    }

    @Override
    public void driveGames(int[][] source) throws SolutionInvalidException {
        System.out.println("DEBUG [GameController.driveGames]: Called");
        System.out.println("DEBUG: Source array dimensions: " +
                (source != null ? source.length + "x" + (source.length > 0 ? source[0].length : 0) : "null"));

        if (source == null) {
            System.out.println("DEBUG: Source is null!");
            throw new IllegalArgumentException("Source cannot be null");
        }

        Models.Board board = new Models.Board(source);
        System.out.println("DEBUG: Board created successfully");

        Models.Game sourceGame = new Models.Game(board, Models.enums.DifficultyLevel.EASY);
        System.out.println("DEBUG: Source game created");

        System.out.println("DEBUG: Calling gameDriver.generateGames()...");
        try {
            gameDriver.generateGames(sourceGame);
            System.out.println("DEBUG: gameDriver.generateGames() completed successfully");
        } catch (Exception e) {
            System.err.println("DEBUG: Exception in gameDriver.generateGames(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] grid) {
        Models.Board board = new Models.Board(grid);
        verification.VerificationResult result = verifier.verify(board);

        boolean[][] invalidCells = new boolean[9][9];

        // Initialize all to false (all valid by default)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                invalidCells[i][j] = false;
            }
        }

        // Mark invalid positions as true
        if (result.isInvalid()) {
            for (OptionalHelperClasses.Position pos : result.getInvalidPositions()) {
                invalidCells[pos.getRow()][pos.getCol()] = true;
            }
        }

        return invalidCells;
    }

    @Override
    public int[][] solveGame(int[][] grid) throws exceptions.InvalidGameException {
        Models.Board board = new Models.Board(grid);
        solver.SolutionResult result = solver.solve(board);

        if (result == null) {
            throw new exceptions.InvalidGameException("No solution found");
        }

        // Convert solution array to 2D grid format
        int[] solution = result.getSolution();
        List<OptionalHelperClasses.Position> emptyPositions = result.getEmptyPositions();

        int[][] solvedGrid = new int[9][9];

        // Copy original grid
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, solvedGrid[i], 0, 9);
        }

        // Fill in solutions
        for (int i = 0; i < emptyPositions.size(); i++) {
            OptionalHelperClasses.Position pos = emptyPositions.get(i);
            solvedGrid[pos.getRow()][pos.getCol()] = solution[i];
        }

        return solvedGrid;
    }



    // ==================== Additional Helper Methods ====================


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