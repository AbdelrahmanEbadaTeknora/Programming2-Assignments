package controller;

import Models.Catalog;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import OptionalHelperClasses.UserAction;

import java.io.IOException;

/**
 * View interface for the Controller layer
 */
public interface Controllable {

    /**
     * Returns catalog information
     */
    Catalog getCatalog();


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


}