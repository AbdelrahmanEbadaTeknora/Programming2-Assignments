package verification;

import OptionalHelperClasses.Position;
import Models.enums.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the result of a Sudoku board verification
 * Stores the state (VALID, INVALID, INCOMPLETE) and any invalid positions
 */
public class VerificationResult {
    private final GameState state;
    private final List<Position> invalidPositions;
    private final String message;

    /**
     * Constructor with all parameters
     * @param state The state of the board (VALID, INVALID, or INCOMPLETE)
     * @param invalidPositions List of positions that have conflicts
     * @param message Additional message describing the result
     */
    public VerificationResult(GameState state, List<Position> invalidPositions, String message) {
        this.state = state;
        this.invalidPositions = invalidPositions != null ? invalidPositions : new ArrayList<>();
        this.message = message != null ? message : "";
    }

    /**
     * Constructor with only state
     * @param state The state of the board
     */
    public VerificationResult(GameState state) {
        this(state, new ArrayList<>(), "");
    }

    /**
     * Gets the state of the board
     */
    public GameState getState() {
        return state;
    }

    /**
     * Gets the list of invalid positions
     */
    public List<Position> getInvalidPositions() {
        return new ArrayList<>(invalidPositions);
    }

    /**
     * Gets the message describing the result
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks if board is valid
     */
    public boolean isValid() {
        return state == GameState.VALID;
    }

    /**
     * Checks if board is invalid
     */
    public boolean isInvalid() {
        return state == GameState.INVALID;
    }

    /**
     * Checks if board is incomplete
     */
    public boolean isIncomplete() {
        return state == GameState.INCOMPLETE;
    }

    @Override
    public String toString() {
        return "VerificationResult{" +
                "state=" + state +
                ", invalidPositions=" + invalidPositions.size() +
                ", message='" + message + '\'' +
                '}';
    }
}