package Models.enums;

/**
 * Represents difficulty levels for Sudoku games
 */
public enum DifficultyLevel {
    EASY(10),      // Remove 10 cells
    MEDIUM(20),    // Remove 20 cells
    HARD(25);      // Remove 25 cells

    private  int cellsToRemove;

    DifficultyLevel(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }

    public String getFolderName() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}