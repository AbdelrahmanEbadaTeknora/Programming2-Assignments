package solver;

import java.util.List;
import java.util.NoSuchElementException;
import OptionalHelperClasses.Position;


public class PermutationIterator {
    private final List<Position> emptyPositions;
    private final int numPositions;
    private final int[] currentCombination;
    private boolean hasNext;


    public PermutationIterator(List<Position> emptyPositions) {
        if (emptyPositions == null || emptyPositions.isEmpty()) {
            throw new IllegalArgumentException("Empty positions cannot be null or empty");
        }
        if (emptyPositions.size() > 5) {
            throw new IllegalArgumentException("Cannot have more than 5 empty positions");
        }

        this.emptyPositions = emptyPositions;
        this.numPositions = emptyPositions.size();
        this.currentCombination = new int[numPositions];
        this.hasNext = true;

        // Initialize with all 1s (first valid permutation)
        for (int i = 0; i < numPositions; i++) {
            currentCombination[i] = 1;
        }
    }

    /**
     * Checks if there are more permutations to generate
     */
    public boolean hasNext() {
        return hasNext;
    }

    /**
     * Gets the next permutation
     * @return Array of values for each empty position
     * @throws NoSuchElementException if no more permutations
     */
    public int[] next() {
        if (!hasNext) {
            throw new NoSuchElementException("No more permutations available");
        }

        // Create a copy of current combination to return
        int[] result = currentCombination.clone();

        // Generate next combination (like incrementing a base-9 number)
        incrementCombination();

        return result;
    }


    private void incrementCombination() {
        int position = numPositions - 1;

        while (position >= 0) {
            currentCombination[position]++;

            if (currentCombination[position] <= 9) {
                // Valid next value, done
                return;
            }

            // Reset this position and carry over to previous
            currentCombination[position] = 1;
            position--;
        }

        // If we get here, we've exhausted all combinations
        hasNext = false;
    }


    public void reset() {
        for (int i = 0; i < numPositions; i++) {
            currentCombination[i] = 1;
        }
        hasNext = true;
    }
}