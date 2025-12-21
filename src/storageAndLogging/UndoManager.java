package storageAndLogging;

import Models.Game;
import Models.Board;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages undo operations using log file
 * Reverses last move by reading log file and applying inverse operation
 */
public class UndoManager {
    private GameLogger logger;
    private static final Pattern LOG_PATTERN = Pattern.compile("\\((\\d+),(\\d+),(\\d+),(\\d+)\\)");

    /**
     * No-argument constructor
     */
    public UndoManager() {
        this.logger = null;
    }

    /**
     * Constructor with GameLogger
     */
    public UndoManager(GameLogger logger) {
        this.logger = logger;
    }

    /**
     * Sets the GameLogger instance
     */
    public void setGameLogger(GameLogger logger) {
        this.logger = logger;
    }

    /**
     * Gets the GameLogger instance
     */
    public GameLogger getGameLogger() {
        return logger;
    }

    /**
     * Performs undo operation on the game
     * Reads last log entry, removes it, and applies inverse change to board
     *
     * @param game Game to undo move in
     * @return true if undo successful, false if no moves to undo
     * @throws IOException if file operations fail
     */
    public boolean undo(Game game) throws IOException {
        if (game == null) {
            return false;
        }

        if (logger == null) {
            System.err.println("GameLogger not set in UndoManager");
            return false;
        }

        // Get the last log entry
        String lastEntry = logger.getLastEntry();

        if (lastEntry == null || lastEntry.isEmpty()) {
            return false; // No moves to undo
        }

        // Parse the log entry
        LogMove move = parseLogEntry(lastEntry);

        if (move == null) {
            return false;
        }

        // Apply inverse operation: set cell back to previous value
        Board board = game.getBoard();
        board.setValue(move.row, move.col, move.previousValue);

        // Remove the entry from log file
        logger.removeLastEntry();

        return true;
    }

    /**
     * Performs multiple undo operations
     */
    public boolean undoMultiple(Game game, int count) throws IOException {
        if (logger == null) {
            System.err.println("GameLogger not set in UndoManager");
            return false;
        }

        for (int i = 0; i < count; i++) {
            if (!undo(game)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses a log entry in format (x,y,val,prev)
     */
    private LogMove parseLogEntry(String entry) {
        Matcher matcher = LOG_PATTERN.matcher(entry);

        if (!matcher.find()) {
            return null;
        }

        try {
            int row = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            int newValue = Integer.parseInt(matcher.group(3));
            int previousValue = Integer.parseInt(matcher.group(4));

            return new LogMove(row, col, newValue, previousValue);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse log entry: " + entry);
            return null;
        }
    }

    /**
     * Gets number of undoable moves
     */
    public int getUndoableMovesCount() throws IOException {
        if (logger == null) {
            return 0;
        }
        return logger.getAllEntries().size();
    }

    /**
     * Checks if undo is possible
     */
    public boolean canUndo() throws IOException {
        if (logger == null) {
            return false;
        }
        return logger.getLastEntry() != null;
    }

    /**
     * Clears all undo history
     */
    public void clearHistory() throws IOException {
        if (logger != null) {
            logger.clearLog();
        }
    }

    /**
     * Inner class to represent a logged move
     */
    private static class LogMove {
        int row;
        int col;
        int newValue;
        int previousValue;

        LogMove(int row, int col, int newValue, int previousValue) {
            this.row = row;
            this.col = col;
            this.newValue = newValue;
            this.previousValue = previousValue;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d,%d)", row, col, newValue, previousValue);
        }
    }
}