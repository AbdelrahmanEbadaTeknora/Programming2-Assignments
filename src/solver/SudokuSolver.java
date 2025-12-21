package main.java.solver;

import main.java.Models.Board;
import main.java.Models.SolutionResult;
import main.java.verification.SudokuVerifier;

/**
 * MEMBER 4: Implement this class
 * Solves Sudoku using permutations (max 5 empty cells)
 * Must use Iterator and Flyweight patterns
 */
public class SudokuSolver {
    private SudokuVerifier verifier;

    public SudokuSolver(SudokuVerifier verifier) {
        this.verifier = verifier;
    }

    /**
     * Solves the board if it has exactly 5 empty cells
     * @return SolutionResult with solutions, or null if cannot solve
     */
    public SolutionResult solve(Board board) {
        // TODO: Member 4 - Implement main.javaa.solver with Iterator & Flyweight patterns
        throw new UnsupportedOperationException("Member 4: Implement this method");
    }
}