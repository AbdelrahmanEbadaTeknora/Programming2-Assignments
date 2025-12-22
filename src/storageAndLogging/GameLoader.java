package storageAndLogging;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.NotFoundException;
import utils.Constants;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.GAMES_FOLDER;


public class GameLoader {


    public Game loadGame(DifficultyLevel level) throws NotFoundException {
        String folderPath = GAMES_FOLDER + File.separator;

        if (level == null) {
            folderPath += Constants.INCOMPLETE_FOLDER;
        } else {
            folderPath += level.getFolderName();
        }

        // Find first game file in the folder
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new NotFoundException("Folder not found: " + folderPath);
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            throw new NotFoundException("No game found for difficulty: " +
                    (level != null ? level : "INCOMPLETE"));
        }

        // Load the first game file
        try {
            int[][] grid = loadBoardFromFile(files[0]);
            Board board = new Board(grid);
            return new Game(board, level);
        } catch (IOException e) {
            throw new NotFoundException("Failed to load game: " + e.getMessage(), e);
        }
    }


    public Game loadCurrentGame() throws NotFoundException {
        return loadGame(null);
    }

    public void saveCurrentGame(Game game) throws IOException {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        String folderPath = GAMES_FOLDER + File.separator +
                Constants.INCOMPLETE_FOLDER;
        createFolderIfNotExists(folderPath);

        // Use the correct filename
        String filePath = folderPath + File.separator + Constants.GAME_FILE;

        System.out.println("DEBUG [GameLoader.saveCurrentGame]:");
        System.out.println("  Folder: " + folderPath);
        System.out.println("  File: " + filePath);

        saveBoardToFile(game.getBoard(), filePath);

        // Verify file was created
        File savedFile = new File(filePath);
        if (savedFile.exists()) {
            System.out.println("  SUCCESS: File saved (" + savedFile.length() + " bytes)");
        } else {
            System.err.println("  ERROR: File not created!");
        }
    }


    public void saveGame(Game game, DifficultyLevel difficulty) throws IOException {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        String folderPath = GAMES_FOLDER + File.separator +
                difficulty.getFolderName();
        createFolderIfNotExists(folderPath);

        // Create unique filename with timestamp
        String filename = "game_" + System.currentTimeMillis() + ".txt";
        String filePath = folderPath + File.separator + filename;

        saveBoardToFile(game.getBoard(), filePath);
    }


    public void deleteGame(DifficultyLevel difficulty) throws IOException {
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        String folderPath = GAMES_FOLDER + File.separator +
                difficulty.getFolderName();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    public void deleteCurrentGame() throws IOException {
        String folderPath = GAMES_FOLDER + File.separator +
                Constants.INCOMPLETE_FOLDER;
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


    private int[][] loadBoardFromFile(File file) throws IOException {
        int[][] grid = new int[9][9];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;

            while ((line = reader.readLine()) != null && row < 9) {
                String[] values = line.trim().split("\\s+");
                if (values.length != 9) {
                    throw new IOException("Invalid line format: expected 9 values, got " +
                            values.length);
                }

                for (int col = 0; col < 9; col++) {
                    try {
                        grid[row][col] = Integer.parseInt(values[col]);
                        if (grid[row][col] < 0 || grid[row][col] > 9) {
                            throw new IOException("Invalid value: " + grid[row][col] +
                                    " at (" + row + "," + col + ")");
                        }
                    } catch (NumberFormatException e) {
                        throw new IOException("Invalid number format: " + values[col]);
                    }
                }
                row++;
            }

            if (row < 9) {
                throw new IOException("File has fewer than 9 rows");
            }
        }

        return grid;
    }


    private void saveBoardToFile(Board board, String filePath) throws IOException {
        int[][] grid = board.getGrid();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(String.valueOf(grid[i][j]));
                    if (j < 8) {
                        writer.write(" ");
                    }
                }
                writer.write("\n");
            }
        }
    }


    private void createFolderIfNotExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                System.err.println("Failed to create folder: " + folderPath);
            }
        }
    }


    public List<Game> getAllGames(DifficultyLevel difficulty) throws IOException {
        List<Game> games = new ArrayList<>();
        String folderPath = GAMES_FOLDER + File.separator +
                difficulty.getFolderName();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files != null) {
                for (File file : files) {
                    try {
                        int[][] grid = loadBoardFromFile(file);
                        games.add(new Game(grid, difficulty));
                    } catch (IOException e) {
                        System.err.println("Failed to load game from: " +
                                file.getAbsolutePath());
                    }
                }
            }
        }

        return games;
    }
}