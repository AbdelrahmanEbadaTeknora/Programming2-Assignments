package generator;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.SolutionInvalidException;
import verification.SudokuVerifier;
import verification.VerificationResult;
import Models.enums.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates Sudoku games from a solved board
 * Creates playable puzzles by removing cells based on difficulty level
 */
public class GameGenerator {
    private SudokuVerifier verifier;
    private RandomPairs randomPairs;

    /**
     * Constructor
     */
    public GameGenerator() {
        this.verifier = new SudokuVerifier();
        this.randomPairs = new RandomPairs();
    }

    /**
     * Constructor with custom verifier
     */
    public GameGenerator(SudokuVerifier verifier) {
        this.verifier = verifier;
        this.randomPairs = new RandomPairs();
    }

    /**
     * Generates all three difficulty levels from a solved board
     * @param solvedBoard A completely solved and valid Sudoku board
     * @return Array of 3 games [EASY, MEDIUM, HARD]
     * @throws SolutionInvalidException if board is not valid/complete
     */
    public Game[] generateAllDifficulties(Board solvedBoard) throws SolutionInvalidException {
        validateSourceBoard(solvedBoard);

        Game[] games = new Game[3];
        games[0] = generateGame(solvedBoard, DifficultyLevel.EASY);
        games[1] = generateGame(solvedBoard, DifficultyLevel.MEDIUM);
        games[2] = generateGame(solvedBoard, DifficultyLevel.HARD);

        return games;
    }

    /**
     * Generates a game for a specific difficulty
     * @param solvedBoard Solved board
     * @param difficulty Target difficulty level
     * @return Generated game with cells removed
     * @throws SolutionInvalidException if board is invalid
     */
    public Game generateGame(Board solvedBoard, DifficultyLevel difficulty)
            throws SolutionInvalidException {
        validateSourceBoard(solvedBoard);

        // Create a copy of the solved board
        Board gameBoard = solvedBoard.copy();

        // Get number of cells to remove
        int cellsToRemove = difficulty.getCellsToRemove();

        // Generate random positions to remove
        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);

        // Remove cells from the board
        for (int[] pos : positionsToRemove) {
            int row = pos[0];
            int col = pos[1];
            gameBoard.setValue(row, col, 0);
        }

        // Create and return game
        return new Game(gameBoard, difficulty);
    }

    /**
     * Generates multiple games of same difficulty
     * @param solvedBoard Solved board
     * @param difficulty Difficulty level
     * @param count Number of games to generate
     * @return List of generated games
     * @throws SolutionInvalidException if board is invalid
     */
    public List<Game> generateMultipleGames(Board solvedBoard, DifficultyLevel difficulty,
                                            int count) throws SolutionInvalidException {
        validateSourceBoard(solvedBoard);

        List<Game> games = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            games.add(generateGame(solvedBoard, difficulty));
        }
        return games;
    }

    /**
     * Generates custom difficulty game with specific cell count
     * @param solvedBoard Solved board
     * @param cellsToRemove Number of cells to remove
     * @return Generated game
     * @throws SolutionInvalidException if board is invalid
     * @throws IllegalArgumentException if cellsToRemove is invalid
     */
    public Game generateCustomDifficulty(Board solvedBoard, int cellsToRemove)
            throws SolutionInvalidException {
        validateSourceBoard(solvedBoard);

        if (cellsToRemove < 0 || cellsToRemove > 81) {
            throw new IllegalArgumentException(
                    "Number of cells to remove must be between 0 and 81");
        }

        Board gameBoard = solvedBoard.copy();
        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);

        for (int[] pos : positionsToRemove) {
            gameBoard.setValue(pos[0], pos[1], 0);
        }

        // Determine difficulty level based on cell count
        DifficultyLevel difficulty;
        if (cellsToRemove <= 10) {
            difficulty = DifficultyLevel.EASY;
        } else if (cellsToRemove <= 20) {
            difficulty = DifficultyLevel.MEDIUM;
        } else {
            difficulty = DifficultyLevel.HARD;
        }

        return new Game(gameBoard, difficulty);
    }

    /**
     * Validates that source board is valid and complete
     * @throws SolutionInvalidException if board is not valid or incomplete
     */
    private void validateSourceBoard(Board board) throws SolutionInvalidException {
        if (board == null) {
            throw new SolutionInvalidException("Source board cannot be null");
        }

        VerificationResult result = verifier.verify(board);

        if (!result.isValid()) {
            if (result.getState() == GameState.INCOMPLETE) {
                throw new SolutionInvalidException(
                        "Source board is incomplete - contains empty cells (0s)");
            } else {
                throw new SolutionInvalidException(
                        "Source board is invalid - contains duplicate numbers or rule violations");
            }
        }
    }

    /**
     * Gets difficulty based on number of cells removed
     * @param cellsRemoved Number of cells removed
     * @return Corresponding DifficultyLevel
     */
    public static DifficultyLevel getDifficultyByRemovedCells(int cellsRemoved) {
        if (cellsRemoved <= 10) {
            return DifficultyLevel.EASY;
        } else if (cellsRemoved <= 20) {
            return DifficultyLevel.MEDIUM;
        } else {
            return DifficultyLevel.HARD;
        }
    }

    /**
     * Gets recommended cell count for difficulty
     * @param difficulty Target difficulty
     * @return Number of cells to remove
     */
    public static int getCellCountForDifficulty(DifficultyLevel difficulty) {
        return difficulty.getCellsToRemove();
    }

    /**
     * Creates a game with all cells shown (complete solution)
     * @param solvedBoard Solved board
     * @param difficulty Difficulty level
     * @return Game with no cells removed
     */
    public Game generateCompleteSolution(Board solvedBoard, DifficultyLevel difficulty) {
        return new Game(solvedBoard.copy(), difficulty);
    }

    /**
     * Gets information about generation
     * @return String with generation info
     */
    public String getGeneratorInfo() {
        return "GameGenerator - Sudoku Puzzle Generator\n" +
                "Difficulties:\n" +
                "  Easy: " + DifficultyLevel.EASY.getCellsToRemove() + " cells removed\n" +
                "  Medium: " + DifficultyLevel.MEDIUM.getCellsToRemove() + " cells removed\n" +
                "  Hard: " + DifficultyLevel.HARD.getCellsToRemove() + " cells removed\n" +
                "Max difficulty (custom): 81 cells removed\n" +
                "Uses RandomPairs for unique cell selection";
    }

    /**
     * Inner class for generation statistics
     */
    public static class GenerationStats {
        public int originalCells;
        public int filledCells;
        public int emptyCells;
        public DifficultyLevel difficulty;
        public long generationTimeMs;

        public GenerationStats(int filled, int empty, DifficultyLevel diff, long time) {
            this.originalCells = 81;
            this.filledCells = filled;
            this.emptyCells = empty;
            this.difficulty = diff;
            this.generationTimeMs = time;
        }

        @Override
        public String toString() {
            return "GenerationStats{" +
                    "difficulty=" + difficulty +
                    ", filled=" + filledCells +
                    ", empty=" + emptyCells +
                    ", time=" + generationTimeMs + "ms" +
                    '}';
        }
    }

    /**
     * Generates game with statistics tracking
     * @param solvedBoard Solved board
     * @param difficulty Target difficulty
     * @return Array containing [Game, GenerationStats]
     * @throws SolutionInvalidException if board is invalid
     */
    public Object[] generateGameWithStats(Board solvedBoard, DifficultyLevel difficulty)
            throws SolutionInvalidException {
        long startTime = System.currentTimeMillis();

        Game game = generateGame(solvedBoard, difficulty);

        long endTime = System.currentTimeMillis();
        long timeMs = endTime - startTime;

        int emptyCells = game.getEmptyCellCount();
        int filledCells = 81 - emptyCells;

        GenerationStats stats = new GenerationStats(filledCells, emptyCells, difficulty, timeMs);

        return new Object[]{game, stats};
    }
}