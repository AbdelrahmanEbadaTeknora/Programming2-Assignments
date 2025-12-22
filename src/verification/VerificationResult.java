package verification;

import OptionalHelperClasses.Position;
import Models.enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class VerificationResult {
    private final GameState state;
    private final List<Position> invalidPositions;
    private final String message;

    public VerificationResult(GameState state, List<Position> invalidPositions, String message) {
        this.state = state;
        this.invalidPositions = invalidPositions != null ? invalidPositions : new ArrayList<>();
        this.message = message != null ? message : "";
    }

    public GameState getState() {
        return state;
    }

    /**
     * Gets the list of invalid positions
     */
    public List<Position> getInvalidPositions() {
        return new ArrayList<>(invalidPositions);
    }


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