package controller;

import Models.Catalog;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import OptionalHelperClasses.UserAction;

import java.io.IOException;


public interface Controllable {


    Catalog getCatalog();


    int[][] getGame(char level) throws NotFoundException;


    void driveGames(int[][] source) throws SolutionInvalidException;


    boolean[][] verifyGame(int[][] game);


    int[][] solveGame(int[][] game) throws InvalidGameException;


}