package main.java.Models;

import main.java.Models.enums.DifficultyLevel;

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

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", difficulty=" + difficulty +
                ", timestamp=" + timestamp +
                '}';
    }
}