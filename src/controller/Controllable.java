package main.java.controller;

import main.java.Models.Catalog;
import main.java.exceptions.InvalidGameException;
import main.java.exceptions.NotFoundException;
import main.java.exceptions.SolutionInvalidException;
import main.java.utils.UserAction;

import java.io.IOException;

/**
 * View interface for the Controller layer
 */
public interface Controllable {

    /**
     * Returns catalog information
     */
    Catalog getCatalog();

    /**
     * Returns a game grid for specified difficulty level
     * @param level 'e' for easy, 'm' for medium, 'h' for hard
     */
    int[][] getGame(char level) throws NotFoundException;

    /**
     * Generates games from source solution
     */
    void driveGames(int[][] source) throws SolutionInvalidException;

    /**
     * Returns boolean array indicating which cells are invalid
     */
    boolean[][] verifyGame(int[][] game);

    /**
     * Returns solved grid with solutions filled in
     * Contains cell coordinates and solution for each missing cell
     */
    int[][] solveGame(int[][] game) throws InvalidGameException;

    /**
     * Logs user action
     */
    void logUserAction(UserAction userAction) throws IOException;
}