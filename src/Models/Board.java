package Models;

/**
 * Represents a Sudoku board (9x9 grid)
 */
public class Board {
    private static final int SIZE = 9;
    private int[][] grid;

    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    public Board(int[][] grid) {
        if (grid == null || grid.length != SIZE) {
            throw new IllegalArgumentException("Grid must be 9x9");
        }
        this.grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            if (grid[i] == null || grid[i].length != SIZE) {
                throw new IllegalArgumentException("Grid must be 9x9");
            }
            System.arraycopy(grid[i], 0, this.grid[i], 0, SIZE);
        }
    }

    /**
     * Returns a copy of the grid to prevent external modification
     */
    public int[][] getGrid() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public void setGrid(int[][] grid) {
        if (grid == null || grid.length != SIZE) {
            throw new IllegalArgumentException("Grid must be 9x9");
        }
        for (int i = 0; i < SIZE; i++) {
            if (grid[i] == null || grid[i].length != SIZE) {
                throw new IllegalArgumentException("Grid must be 9x9");
            }
            System.arraycopy(grid[i], 0, this.grid[i], 0, SIZE);
        }
    }

    public int getValue(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return grid[row][col];
        }
        throw new IndexOutOfBoundsException("Invalid position: (" + row + "," + col + ")");
    }

    public void setValue(int row, int col, int value) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            if (value >= 0 && value <= 9) {
                grid[row][col] = value;
            } else {
                throw new IllegalArgumentException("Value must be between 0 and 9");
            }
        } else {
            throw new IndexOutOfBoundsException("Invalid position: (" + row + "," + col + ")");
        }
    }

    public boolean isEmpty(int row, int col) {
        return getValue(row, col) == 0;
    }

    public int getSize() {
        return SIZE;
    }

    /**
     * Creates a deep copy of this board
     */
    public Board copy() {
        return new Board(this.grid);
    }

    /**
     * Clears the entire board (sets all cells to 0)
     */
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = 0;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(grid[i][j]).append(" ");
                if ((j + 1) % 3 == 0 && j < SIZE - 1) {
                    sb.append("| ");
                }
            }
            sb.append("\n");
            if ((i + 1) % 3 == 0 && i < SIZE - 1) {
                sb.append("------+-------+------\n");
            }
        }
        return sb.toString();
    }
}
