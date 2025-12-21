package storageAndLogging;

import main.java.Models.Game;
import java.io.IOException;

/**
 * MEMBER 4: Implement this class
 * Manages undo operations
 */
public class UndoManager {
    private GameLogger logger;

    public UndoManager(GameLogger logger) {
        this.logger = logger;
    }

    /**
     * Performs undo operation
     * @return true if undo successful, false otherwise
     */
    public boolean undo(Game game) throws IOException {
        // TODO: Member 4 - Implement undo
        throw new UnsupportedOperationException("Member 4: Implement this method");
    }
}