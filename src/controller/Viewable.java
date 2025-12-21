package main.java.controller;

import main.java.Models.Catalog;
import main.java.Models.Game;
import main.java.Models.enums.DifficultyLevel;
import main.java.exceptions.InvalidGameException;
import main.java.exceptions.NotFoundException;
import main.java.exceptions.SolutionInvalidException;

import java.io.IOException;

/**
 * Controller interface for the View layer
 */
public interface Viewable {

    /**
     * Returns catalog information about available games
     */
    Catalog getCatalog();

    /**
     * Returns a random game with the specified difficulty
     */
    Game getGame(DifficultyLevel level) throws NotFoundException;

    /**
     * Gets a source solution and generates three difficulty levels
     */
    void driveGames(Game source) throws SolutionInvalidException;

    /**
     * Verifies a game and returns string representation
     * Returns: "VALID", "INCOMPLETE", or "INVALID: (x,y), (x,y)..."
     */
    String verifyGame(Game game);

    /**
     * Returns the correct combination for missing numbers
     * Array maps empty cell index to its solution value
     */
    int[] solveGame(Game game) throws InvalidGameException;

    /**
     * Logs user action
     */
    void logUserAction(String userAction) throws IOException;
}