package main.java.utils;

/**
 * Application-wide constants
 */
public class Constants {
    // Board constants
    public static final int BOARD_SIZE = 9;
    public static final int BOX_SIZE = 3;
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;
    public static final int EMPTY_CELL = 0;

    // Folder names
    public static final String GAMES_FOLDER = "games";
    public static final String EASY_FOLDER = "easy";
    public static final String MEDIUM_FOLDER = "medium";
    public static final String HARD_FOLDER = "hard";
    public static final String INCOMPLETE_FOLDER = "incomplete";

    // File names
    public static final String GAME_FILE = "game.txt";
    public static final String LOG_FILE = "game_log.txt";

    // Solver constants
    public static final int MAX_EMPTY_CELLS_FOR_SOLVER = 5;

    private Constants() {
        // Prevent instantiation
    }}