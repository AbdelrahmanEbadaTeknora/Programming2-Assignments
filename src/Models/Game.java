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


    public Game(Board board, DifficultyLevel difficulty) {
        this.board = board;
        this.difficulty = difficulty;
        this.id = generateId();
        this.timestamp = System.currentTimeMillis();
    }


    public Game(int[][] grid, DifficultyLevel difficulty) {
        this(new Board(grid), difficulty);
    }


    private String generateId() {
        return "game_" + System.currentTimeMillis();
    }


    public Board getBoard() {
        return board;
    }


    public void setBoard(Board board) {
        this.board = board;
    }


    public DifficultyLevel getDifficulty() {
        return difficulty;
    }


    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }


    public String getId() {
        return id;
    }


    public long getTimestamp() {
        return timestamp;
    }


    public int[][] getGrid() {
        return board.getGrid();
    }


    public Game copy() {
        Game newGame = new Game(board.copy(), difficulty);
        newGame.id = this.id;
        newGame.timestamp = this.timestamp;
        return newGame;
    }


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;
        return id.equals(game.id);
    }


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