import com.sudoku.model.SudokuBoard;
import com.sudoku.util.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses CSV files containing Sudoku boards
 */
public class CSVParser {

    /**
     * Parse a CSV file and return a com.sudoku.model.SudokuBoard object
     * @param filepath Path to the CSV file
     * @return com.sudoku.model.SudokuBoard object
     * @throws IOException If file cannot be read
     * @throws IllegalArgumentException If file format is invalid
     */
    public SudokuBoard parseFile(String filepath) throws IOException {
        if (!FileValidator.isValidFile(filepath)) {
            throw new IllegalArgumentException("Invalid file: " + filepath);
        }

        int[][] board = readCSV(filepath);

        if (!FileValidator.isValidBoard(board)) {
            throw new IllegalArgumentException("Invalid board format");
        }

        return new SudokuBoard(board);
    }

    /**
     * Read CSV file and convert to 2D array
     * @param filepath Path to CSV file
     * @return 2D array of integers
     * @throws IOException If file cannot be read
     */
    private int[][] readCSV(String filepath) throws IOException {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse the line
                int[] row = parseLine(line, lineNumber);
                rows.add(row);

                // Stop if we have enough rows
                if (rows.size() >= Constants.BOARD_SIZE) {
                    break;
                }
            }
        }

        // Validate we have exactly 9 rows
        if (rows.size() != Constants.BOARD_SIZE) {
            throw new IOException(
                    String.format("Invalid number of rows: expected %d, found %d",
                            Constants.BOARD_SIZE, rows.size())
            );
        }

        // Convert List to array
        int[][] board = new int[Constants.BOARD_SIZE][];
        for (int i = 0; i < rows.size(); i++) {
            board[i] = rows.get(i);
        }

        return board;
    }

    /**
     * Parse a single line from CSV
     * @param line Line to parse
     * @param lineNumber Line number (for error reporting)
     * @return Array of integers
     * @throws IOException If line format is invalid
     */
    private int[] parseLine(String line, int lineNumber) throws IOException {
        // Split by comma
        String[] tokens = line.split(",");

        // Check we have exactly 9 values
        if (tokens.length != Constants.BOARD_SIZE) {
            throw new IOException(
                    String.format("Line %d: Expected %d values, found %d",
                            lineNumber, Constants.BOARD_SIZE, tokens.length)
            );
        }

        int[] values = new int[Constants.BOARD_SIZE];

        for (int i = 0; i < tokens.length; i++) {
            try {
                // Trim whitespace and parse
                String token = tokens[i].trim();
                int value = Integer.parseInt(token);

                // Validate range
                if (value < Constants.MIN_VALUE || value > Constants.MAX_VALUE) {
                    throw new IOException(
                            String.format("Line %d, Column %d: Value %d is out of range [%d-%d]",
                                    lineNumber, i + 1, value, Constants.MIN_VALUE, Constants.MAX_VALUE)
                    );
                }

                values[i] = value;

            } catch (NumberFormatException e) {
                throw new IOException(
                        String.format("Line %d, Column %d: Invalid number format '%s'",
                                lineNumber, i + 1, tokens[i].trim())
                );
            }
        }

        return values;
    }

    /**
     * Parse CSV content from a string (useful for testing)
     * @param csvContent CSV content as string
     * @return com.sudoku.model.SudokuBoard object
     * @throws IOException If parsing fails
     */
    public SudokuBoard parseString(String csvContent) throws IOException {
        String[] lines = csvContent.split("\\r?\\n");
        List<int[]> rows = new ArrayList<>();

        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;

            if (line.trim().isEmpty()) {
                continue;
            }

            int[] row = parseLine(line, lineNumber);
            rows.add(row);

            if (rows.size() >= Constants.BOARD_SIZE) {
                break;
            }
        }

        if (rows.size() != Constants.BOARD_SIZE) {
            throw new IOException(
                    String.format("Invalid number of rows: expected %d, found %d",
                            Constants.BOARD_SIZE, rows.size())
            );
        }

        int[][] board = new int[Constants.BOARD_SIZE][];
        for (int i = 0; i < rows.size(); i++) {
            board[i] = rows.get(i);
        }

        if (!FileValidator.isValidBoard(board)) {
            throw new IllegalArgumentException("Invalid board format");
        }

        return new SudokuBoard(board);
    }
}