package Models.enums;

/**
 * Represents difficulty levels for Sudoku games
 * Easy: 10 cells removed (71 filled)
 * Medium: 20 cells removed (61 filled)
 * Hard: 25 cells removed (56 filled)
 */
public enum DifficultyLevel {
    EASY(10),      // Remove 10 cells
    MEDIUM(20),    // Remove 20 cells (CORRECTED from 25)
    HARD(25);      // Remove 25 cells (CORRECTED from 20)

    private final int cellsToRemove;

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