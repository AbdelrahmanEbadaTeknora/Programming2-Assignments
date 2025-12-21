package solver;

import java.util.List;

/**
 * Contains the solution mapping for a Sudoku puzzle
 */
public class SolutionResult {
    private int[] solution;           // Array of values for empty cells
    private List<Position> emptyPositions;  // Positions of empty cells
    private boolean solved;

    public SolutionResult(int[] solution, List<Position> emptyPositions, boolean solved) {
        this.solution = solution;
        this.emptyPositions = emptyPositions;
        this.solved = solved;
    }

    public int[] getSolution() {
        return solution;
    }

    public List<Position> getEmptyPositions() {
        return emptyPositions;
    }

    public boolean isSolved() {
        return solved;
    }

    @Override
    public String toString() {
        return "SolutionResult{" +
                "solved=" + solved +
                ", solutions=" + (solution != null ? solution.length : 0) +
                '}';
    }
}