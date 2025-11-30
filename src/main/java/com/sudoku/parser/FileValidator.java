import com.sudoku.util.Constants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Validates file paths and board contents
 */
public class FileValidator {

    /**
     * Check if the file path is valid and accessible
     * @param filepath Path to the file
     * @return true if valid, false otherwise
     */
    public static boolean isValidFile(String filepath) {
        if (filepath == null || filepath.trim().isEmpty()) {
            System.err.println("Error: File path is null or empty");
            return false;
        }

        try {
            Path path = Paths.get(filepath);
            File file = path.toFile();

            // Check if file exists
            if (!Files.exists(path)) {
                System.err.println("Error: File does not exist: " + filepath);
                return false;
            }

            // Check if it's a file (not a directory)
            if (!file.isFile()) {
                System.err.println("Error: Path is not a file: " + filepath);
                return false;
            }

            // Check if file is readable
            if (!Files.isReadable(path)) {
                System.err.println("Error: File is not readable: " + filepath);
                return false;
            }

            // Check file extension (should be .csv)
            if (!filepath.toLowerCase().endsWith(".csv")) {
                System.err.println("Warning: File does not have .csv extension: " + filepath);
                // Don't return false, just warn
            }

            // Check if file is not empty
            if (file.length() == 0) {
                System.err.println("Error: File is empty: " + filepath);
                return false;
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error validating file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validate that the board has correct dimensions and values
     * @param board 2D array to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidBoard(int[][] board) {
        if (board == null) {
            System.err.println("Error: Board is null");
            return false;
        }

        // Check number of rows
        if (board.length != Constants.BOARD_SIZE) {
            System.err.println(
                    String.format("Error: Board must have %d rows, found %d",
                            Constants.BOARD_SIZE, board.length)
            );
            return false;
        }

        // Check each row
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                System.err.println("Error: Row " + (i + 1) + " is null");
                return false;
            }

            // Check number of columns
            if (board[i].length != Constants.BOARD_SIZE) {
                System.err.println(
                        String.format("Error: Row %d must have %d columns, found %d",
                                i + 1, Constants.BOARD_SIZE, board[i].length)
                );
                return false;
            }

            // Check values are in valid range (1-9)
            for (int j = 0; j < board[i].length; j++) {
                int value = board[i][j];
                if (value < Constants.MIN_VALUE || value > Constants.MAX_VALUE) {
                    System.err.println(
                            String.format("Error: Invalid value %d at position [%d,%d]. " +
                                            "Values must be between %d and %d",
                                    value, i + 1, j + 1, Constants.MIN_VALUE, Constants.MAX_VALUE)
                    );
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Validate dimensions of board
     * @param rowCount Number of rows parsed
     * @param colCount Number of columns in first row
     * @return true if dimensions are 9x9
     */
    public static boolean isValidDimensions(int rowCount, int colCount) {
        if (rowCount != Constants.BOARD_SIZE) {
            System.err.println(
                    String.format("Error: Expected %d rows, found %d",
                            Constants.BOARD_SIZE, rowCount)
            );
            return false;
        }

        if (colCount != Constants.BOARD_SIZE) {
            System.err.println(
                    String.format("Error: Expected %d columns, found %d",
                            Constants.BOARD_SIZE, colCount)
            );
            return false;
        }

        return true;
    }
}