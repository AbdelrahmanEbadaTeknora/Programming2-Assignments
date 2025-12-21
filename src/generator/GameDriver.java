package main.java.generator;

import main.java.Models.Game;
import main.java.exceptions.SolutionInvalidException;
import main.java.verification.SudokuVerifier;

/**
 * MEMBER 2: Implement this class
 * Validates source solution and generates difficulty levels
 */
public class GameDriver {
    private SudokuVerifier verifier;

    public GameDriver(SudokuVerifier verifier) {
        this.verifier = verifier;
    }

    /**
     * Validates source and generates three difficulty levels
     * @throws SolutionInvalidException if source is invalid/incomplete
     */
    public void generateGames(Game source) throws SolutionInvalidException {
        // TODO: Member 2 - Implement game generation
        throw new UnsupportedOperationException("Member 2: Implement this method");
    }
}