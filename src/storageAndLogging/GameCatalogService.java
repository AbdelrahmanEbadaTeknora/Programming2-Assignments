package storageAndLogging;

import Models.Catalog;
import Models.enums.DifficultyLevel;
import utils.Constants;

import java.io.File;


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

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        return files != null && files.length > 0;
    }
}