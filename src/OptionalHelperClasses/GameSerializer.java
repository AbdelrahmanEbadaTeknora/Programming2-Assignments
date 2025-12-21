package OptionalHelperClasses;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Serializes and deserializes Game objects to/from custom format
 * Used for saving game state with metadata
 */
public class GameSerializer {
    private static final String DELIMITER = ",";
    private static final String BOARD_START = "BOARD_START";
    private static final String BOARD_END = "BOARD_END";

    /**
     * Serializes a game to a file in custom format
     * Format:
     * DIFFICULTY=EASY
     * TIMESTAMP=1234567890
     * ID=game_1234567890
     * BOARD_START
     * 1 2 3 ...
     * ...
     * BOARD_END
     */
    public static void serializeGame(Game game, String filePath) throws IOException {
        if (game == null || filePath == null) {
            throw new IllegalArgumentException("Game and file path cannot be null");
        }

        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write metadata
            writer.write("DIFFICULTY=" + game.getDifficulty().toString());
            writer.newLine();
            writer.write("TIMESTAMP=" + game.getTimestamp());
            writer.newLine();
            writer.write("ID=" + game.getId());
            writer.newLine();

            // Write board
            writer.write(BOARD_START);
            writer.newLine();

            int[][] grid = game.getBoard().getGrid();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(String.valueOf(grid[i][j]));
                    if (j < 8) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }

            writer.write(BOARD_END);
            writer.newLine();
        }
    }

    /**
     * Deserializes a game from a file
     * @param filePath Path to game file
     * @return Deserialized Game object
     * @throws IOException if file operations fail
     */
    public static Game deserializeGame(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            DifficultyLevel difficulty = null;
            long timestamp = 0;
            String id = null;
            int[][] board = null;
            boolean readingBoard = false;
            int boardRow = 0;

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("DIFFICULTY=")) {
                    difficulty = DifficultyLevel.valueOf(
                            line.substring("DIFFICULTY=".length()));
                } else if (line.startsWith("TIMESTAMP=")) {
                    timestamp = Long.parseLong(
                            line.substring("TIMESTAMP=".length()));
                } else if (line.startsWith("ID=")) {
                    id = line.substring("ID=".length());
                } else if (line.equals(BOARD_START)) {
                    readingBoard = true;
                    board = new int[9][9];
                    boardRow = 0;
                } else if (line.equals(BOARD_END)) {
                    readingBoard = false;
                } else if (readingBoard) {
                    String[] values = line.split("\\s+");
                    if (values.length == 9) {
                        for (int j = 0; j < 9; j++) {
                            board[boardRow][j] = Integer.parseInt(values[j]);
                        }
                        boardRow++;
                    }
                }
            }

            if (board == null || difficulty == null) {
                throw new IOException("Invalid file format - missing board or difficulty");
            }

            Game game = new Game(board, difficulty);
            return game;
        }
    }

    /**
     * Serializes to JSON format (alternative)
     */
    public static String toJsonString(Game game) {
        if (game == null) {
            return "{}";
        }

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"difficulty\":\"").append(game.getDifficulty()).append("\",");
        json.append("\"timestamp\":").append(game.getTimestamp()).append(",");
        json.append("\"id\":\"").append(game.getId()).append("\",");
        json.append("\"emptyCells\":").append(game.getEmptyCellCount()).append(",");
        json.append("\"board\":[");

        int[][] grid = game.getBoard().getGrid();
        for (int i = 0; i < 9; i++) {
            json.append("[");
            for (int j = 0; j < 9; j++) {
                json.append(grid[i][j]);
                if (j < 8) {
                    json.append(",");
                }
            }
            json.append("]");
            if (i < 8) {
                json.append(",");
            }
        }
        json.append("]");
        json.append("}");

        return json.toString();
    }

    /**
     * Deserializes from JSON string (basic implementation)
     */
    public static Game fromJsonString(String json) throws IOException {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }

        // Basic JSON parsing - for production use a proper JSON library
        try {
            // Extract difficulty
            String diffStr = extractJsonValue(json, "difficulty");
            DifficultyLevel difficulty = DifficultyLevel.valueOf(diffStr);

            // Extract board
            String boardStr = json.substring(json.indexOf("\"board\":[") + 9);
            boardStr = boardStr.substring(0, boardStr.indexOf("]"));

            // Parse board - simplified parsing
            int[][] board = new int[9][9];
            String[] rows = boardStr.split("\\],\\[");
            for (int i = 0; i < Math.min(9, rows.length); i++) {
                String row = rows[i].replaceAll("[\\[\\]]", "").trim();
                String[] values = row.split(",");
                for (int j = 0; j < Math.min(9, values.length); j++) {
                    board[i][j] = Integer.parseInt(values[j].trim());
                }
            }

            return new Game(board, difficulty);
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to extract JSON value
     */
    private static String extractJsonValue(String json, String key) {
        String searchStr = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchStr);
        if (startIndex == -1) {
            searchStr = "\"" + key + "\":";
            startIndex = json.indexOf(searchStr);
            if (startIndex == -1) {
                return "";
            }
            startIndex += searchStr.length();
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
            return json.substring(startIndex, endIndex).trim();
        }
        startIndex += searchStr.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    /**
     * Creates a backup copy of a game file
     */
    public static void backupGame(String originalPath, String backupPath)
            throws IOException {
        File original = new File(originalPath);
        if (!original.exists()) {
            throw new IOException("Original file not found: " + originalPath);
        }

        File backup = new File(backupPath);
        File parentDir = backup.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(original));
             BufferedWriter writer = new BufferedWriter(new FileWriter(backup))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Compresses game data to string representation
     */
    public static String compressGame(Game game) {
        if (game == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(game.getDifficulty().toString()).append("|");
        sb.append(game.getId()).append("|");

        int[][] grid = game.getBoard().getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
            }
        }

        return sb.toString();
    }

    /**
     * Gets file size in bytes
     */
    public static long getFileSizeInBytes(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    /**
     * Validates game file format
     */
    public static boolean isValidGameFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                return false;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean hasDifficulty = false;
                boolean hasTimestamp = false;
                boolean hasBoard = false;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("DIFFICULTY=")) hasDifficulty = true;
                    if (line.startsWith("TIMESTAMP=")) hasTimestamp = true;
                    if (line.equals(BOARD_START)) hasBoard = true;
                }

                return hasDifficulty && hasTimestamp && hasBoard;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets information about serializer
     */
    public static String getSerializerInfo() {
        return "GameSerializer - Sudoku Game Serialization\n" +
                "Formats Supported:\n" +
                "  - Custom text format (recommended)\n" +
                "  - JSON format (alternative)\n" +
                "  - Compressed format\n" +
                "Features:\n" +
                "  - Metadata preservation (difficulty, ID, timestamp)\n" +
                "  - File validation\n" +
                "  - Backup creation\n" +
                "  - Size estimation";
    }
}