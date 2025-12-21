package verification;
import main.java.Models.enums.GameState;
import java.util.ArrayList;
import java.util.List;

public class VerificationResult {
    private final GameState state;
    private final List<Position> invalidPositions;
    private final String message;

    public VerificationResult(GameState state, List<Position> invalidPositions, String message) {
        this.state = state;
        this.invalidPositions = invalidPositions != null ? invalidPositions : new ArrayList<>();
        this.message = message;
    }

    public VerificationResult(GameState state) {
        this(state, new ArrayList<>(), "");
    }

    public GameState getState() {
        return state;
    }

    public List<Position> getInvalidPositions() {
        return new ArrayList<>(invalidPositions);
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return state == GameState.VALID;
    }

    public boolean isInvalid() {
        return state == GameState.INVALID;
    }

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