package Models;

import Models.enums.DifficultyLevel;

/**
 * Represents a Sudoku game with its board and metadata
 */
public class Game {
    private Board board;
    private DifficultyLevel difficulty;
    private String id;
    private long timestamp;

    /**
     * Constructor with board and difficulty
     * @param board The game board
     * @param difficulty The difficulty level
     */
    public Game(Board board, DifficultyLevel difficulty) {
        this.board = board;
        this.difficulty = difficulty;
        this.id = generateId();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Constructor with grid array and difficulty
     * @param grid The 9x9 grid array
     * @param difficulty The difficulty level
     */
    public Game(int[][] grid, DifficultyLevel difficulty) {
        this(new Board(grid), difficulty);
    }

    /**
     * Generates a unique game ID
     */
    private String generateId() {
        return "game_" + System.currentTimeMillis();
    }

    /**
     * Gets the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the game board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the difficulty level
     */
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty level
     */
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the unique game ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the creation timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the grid as a 2D array
     */
    public int[][] getGrid() {
        return board.getGrid();
    }

    /**
     * Creates a deep copy of this game
     */
    public Game copy() {
        Game newGame = new Game(board.copy(), difficulty);
        newGame.id = this.id;
        newGame.timestamp = this.timestamp;
        return newGame;
    }

    /**
     * Checks if game is complete (no empty cells)
     */
    public boolean isComplete() {
        int[][] grid = board.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the number of empty cells
     */
    public int getEmptyCellCount() {
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
     * Checks equality based on ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;
        return id.equals(game.id);
    }

    /**
     * Generates hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", difficulty=" + difficulty +
                ", timestamp=" + timestamp +
                ", emptyCells=" + getEmptyCellCount() +
                '}';
    }
}