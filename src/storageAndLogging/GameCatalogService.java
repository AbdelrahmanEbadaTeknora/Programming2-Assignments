package storageAndLogging;

import Models.Catalog;
import Models.enums.DifficultyLevel;
import utils.Constants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


public class GameCatalogService {


    public Catalog checkGames() {
        boolean hasUnfinished = checkUnfinishedGame();
        boolean hasAllDifficulties = checkAllDifficulties();

        System.out.println("DEBUG [GameCatalogService]:");
        System.out.println("  hasUnfinished: " + hasUnfinished);
        System.out.println("  hasAllDifficulties: " + hasAllDifficulties);
        System.out.println("  incomplete folder: " +
                Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER);

        return new Catalog(hasUnfinished, hasAllDifficulties);
    }


    private boolean checkUnfinishedGame() {
        String incompletePath = Constants.GAMES_FOLDER + File.separator +
                Constants.INCOMPLETE_FOLDER;
        File incompleteFolder = new File(incompletePath);

        if (!incompleteFolder.exists() || !incompleteFolder.isDirectory()) {
            return false;
        }

        // Check if game file exists in incomplete folder
        File gameFile = new File(incompletePath + File.separator + Constants.GAME_FILE);
        return gameFile.exists() && gameFile.isFile();
    }


    private boolean checkAllDifficulties() {
        return checkDifficultyExists(DifficultyLevel.EASY) &&
                checkDifficultyExists(DifficultyLevel.MEDIUM) &&
                checkDifficultyExists(DifficultyLevel.HARD);
    }


    private boolean checkDifficultyExists(DifficultyLevel difficulty) {
        String folderPath = Constants.GAMES_FOLDER + File.separator +
                difficulty.getFolderName();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        // Check if folder contains at least one game file
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        return files != null && files.length > 0;
    }


    public void initializeFolderStructure() {
        String[] folderNames = {
                Constants.GAMES_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.EASY_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.MEDIUM_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.HARD_FOLDER,
                Constants.GAMES_FOLDER + File.separator + Constants.INCOMPLETE_FOLDER
        };

        for (String folderName : folderNames) {
            File folder = new File(folderName);
            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                    System.err.println("Failed to create folder: " + folderName);
                }
            }
        }
    }
}