package storageAndLogging;

import Models.Game;
import Models.Board;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UndoManager {
    private GameLogger logger;
    private static final Pattern LOG_PATTERN = Pattern.compile("\\((\\d+),(\\d+),(\\d+),(\\d+)\\)");


    public UndoManager(GameLogger logger) {
        this.logger = logger;
    }


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