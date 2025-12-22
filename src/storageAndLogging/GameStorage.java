package storageAndLogging;

import Models.Game;
import Models.Board;
import Models.enums.DifficultyLevel;
import exceptions.StorageException;
import utils.Constants;
import utils.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages persistent storage of Sudoku games
 * Handles folder structure, file operations, and game persistence
 */
public class GameStorage {

    /**
     * Initializes the folder structure for game storage
     * Creates: games/easy, games/medium, games/hard, games/incomplete
     */
    public static void initializeFolderStructure() {
        String[] folders = {
                Constants.GAMES_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.EASY_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.MEDIUM_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.HARD_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER
        };

        FileManager.createFolderStructure(folders);
    }

    /**
     * Gets the folder path for a specific difficulty
     */
    public static String getDifficultyFolderPath(DifficultyLevel difficulty) {
        if (difficulty == null) {
            return Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER;
        }
        return Constants.GAMES_FOLDER + File.separator + difficulty.getFolderName();
    }

    /**
     * Gets the path for a game file
     */
    public static String getGameFilePath(DifficultyLevel difficulty, String filename) {
        String folderPath = getDifficultyFolderPath(difficulty);
        return folderPath + File.separator + filename;
    }

    /**
     * Gets the incomplete game folder path
     */
    public static String getIncompleteGamePath() {
        return Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER;
    }

    /**
     * Checks if a difficulty folder exists and contains games
     */
    public static boolean hasDifficultyGames(DifficultyLevel difficulty) {
        String folderPath = getDifficultyFolderPath(difficulty);
        return FileManager.folderExists(folderPath) &&
                FileManager.countFiles(folderPath) > 0;
    }

    /**
     * Checks if incomplete game exists
     */
    public static boolean hasIncompleteGame() {
        String folderPath = getIncompleteGamePath();
        File gameFile = new File(folderPath + File.separator + Constants.GAME_FILE);
        return gameFile.exists() && gameFile.isFile();
    }

    /**
     * Gets count of games in a difficulty folder
     */
    public static int getGameCountByDifficulty(DifficultyLevel difficulty) {
        String folderPath = getDifficultyFolderPath(difficulty);
        return FileManager.countFiles(folderPath);
    }


    public static List<File> getGameFilesByDifficulty(DifficultyLevel difficulty) {
        String folderPath = getDifficultyFolderPath(difficulty);
        return FileManager.getAllFiles(folderPath);
    }


    public static List<File> getTextGameFiles(DifficultyLevel difficulty) {
        String folderPath = getDifficultyFolderPath(difficulty);
        return FileManager.getFilesWithExtension(folderPath, ".txt");
    }


    public static File getFirstGameFile(DifficultyLevel difficulty) {
        List<File> files = getTextGameFiles(difficulty);
        if (files.isEmpty()) {
            return null;
        }
        return files.get(0);
    }


    public static String generateGameFilename() {
        return "game_" + System.currentTimeMillis() + ".txt";
    }


    public static String generateBackupFilename(String originalFilename) {
        String timestamp = "_backup_" + System.currentTimeMillis();
        int dotIndex = originalFilename.lastIndexOf('.');

        if (dotIndex > 0) {
            return originalFilename.substring(0, dotIndex) + timestamp +
                    originalFilename.substring(dotIndex);
        }
        return originalFilename + timestamp;
    }


    public static long getStorageSizeInBytes() {
        long totalSize = 0;
        String[] folders = {
                Constants.GAMES_FOLDER + File.separator + Constants.EASY_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.MEDIUM_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.HARD_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER
        };

        for (String folder : folders) {
            List<File> files = FileManager.getAllFiles(folder);
            for (File file : files) {
                totalSize += FileManager.getFileSize(file.getAbsolutePath());
            }
        }

        return totalSize;
    }


    public static boolean hasStorageSpace() {
        File storageFolder = new File(Constants.GAMES_FOLDER);
        return storageFolder.getUsableSpace() > 1024 * 1024; // At least 1MB free
    }


    public static StorageStats getStorageStats() {
        StorageStats stats = new StorageStats();

        stats.easyGames = getGameCountByDifficulty(DifficultyLevel.EASY);
        stats.mediumGames = getGameCountByDifficulty(DifficultyLevel.MEDIUM);
        stats.hardGames = getGameCountByDifficulty(DifficultyLevel.HARD);
        stats.hasIncompleteGame = hasIncompleteGame();
        stats.totalSizeBytes = getStorageSizeInBytes();

        return stats;
    }


    public static boolean clearDifficultyGames(DifficultyLevel difficulty)
            throws StorageException {
        try {
            String folderPath = getDifficultyFolderPath(difficulty);
            return FileManager.deleteAllFilesInFolder(folderPath);
        } catch (Exception e) {
            throw new StorageException("Failed to clear games: " + e.getMessage(), e);
        }
    }


    public static boolean clearIncompleteGame() throws StorageException {
        try {
            String folderPath = getIncompleteGamePath();
            return FileManager.deleteAllFilesInFolder(folderPath);
        } catch (Exception e) {
            throw new StorageException("Failed to clear incomplete game: " +
                    e.getMessage(), e);
        }
    }


    public static void backupIncompleteGame() throws StorageException {
        try {
            String folderPath = getIncompleteGamePath();
            File gameFile = new File(folderPath + File.separator + Constants.GAME_FILE);

            if (gameFile.exists()) {
                String backupFilename = generateBackupFilename(Constants.GAME_FILE);
                String backupPath = folderPath + File.separator + backupFilename;
                FileManager.copyFile(gameFile.getAbsolutePath(), backupPath);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to backup incomplete game: " +
                    e.getMessage(), e);
        }
    }


    public static class StorageStats {
        public int easyGames;
        public int mediumGames;
        public int hardGames;
        public boolean hasIncompleteGame;
        public long totalSizeBytes;


        public double getSizeInMB() {
            return totalSizeBytes / (1024.0 * 1024.0);
        }

        @Override
        public String toString() {
            return "StorageStats{" +
                    "easy=" + easyGames +
                    ", medium=" + mediumGames +
                    ", hard=" + hardGames +
                    ", incomplete=" + (hasIncompleteGame ? 1 : 0) +
                    ", totalSize=" + String.format("%.2f MB", getSizeInMB()) +
                    '}';
        }
    }

}