package main.java.storage;

import main.java.Models.Game;
import main.java.Models.enums.DifficultyLevel;
import main.java.exceptions.NotFoundException;

import java.io.IOException;

/**
 * MEMBER 2: Implement this class
 * Loads games from main.javaa.storage
 */
public class GameLoader {

    /**
     * Loads a game of specified difficulty
     */
    public Game loadGame(DifficultyLevel level) throws NotFoundException {
        // TODO: Member 2 - Implement game loading
        throw new UnsupportedOperationException("Member 2: Implement this method");
    }

    /**
     * Saves current game state
     */
    public void saveCurrentGame(Game game) throws IOException {
        // TODO: Member 2 - Implement save current game
        throw new UnsupportedOperationException("Member 2: Implement this method");
    }

    /**
     * Deletes a completed game
     */
    public void deleteGame(DifficultyLevel level) throws IOException {
        // TODO: Member 2 - Implement delete game
        throw new UnsupportedOperationException("Member 2: Implement this method");
    }
}
