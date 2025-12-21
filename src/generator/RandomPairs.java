package generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Generates distinct random pairs (x, y) for cell removal in Sudoku
 * Range: 0..8 for both x and y coordinates
 * Each instance uses a new seed based on current time
 */
public class RandomPairs {
    private static final int MAX_COORD = 8;
    private static final int MAX_UNIQUE_PAIRS = (MAX_COORD + 1) * (MAX_COORD + 1); // 81 pairs

    private final Random random;

    /**
     * Constructor initializes Random with current system time as seed
     * This ensures different random sequences for each new instance
     */
    public RandomPairs() {
        // Add a small delay to ensure unique seeds for rapid consecutive calls
        long seed = System.nanoTime();
        this.random = new Random(seed);
    }

    /**
     * Constructor with custom seed (for testing)
     */
    public RandomPairs(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Generates n distinct random pairs (x, y) where 0 <= x <= 8 and 0 <= y <= 8
     *
     * @param n Number of distinct pairs to generate
     * @return List of int[] pairs {x, y}
     * @throws IllegalArgumentException if n is outside valid range [0, 81]
     */
    public List<int[]> generateDistinctPairs(int n) {
        if (n < 0 || n > MAX_UNIQUE_PAIRS) {
            throw new IllegalArgumentException(
                    "n must be between 0 and " + MAX_UNIQUE_PAIRS + " (inclusive)");
        }

        Set<Integer> used = new HashSet<>();
        List<int[]> result = new ArrayList<>(n);

        while (result.size() < n) {
            int x = random.nextInt(MAX_COORD + 1); // 0..8
            int y = random.nextInt(MAX_COORD + 1); // 0..8

            // Encode pair (x, y) as a single int to track uniqueness
            // Formula: x * 9 + y gives unique value for each pair
            int key = x * (MAX_COORD + 1) + y;

            if (used.add(key)) {
                result.add(new int[]{x, y});
            }
        }

        return result;
    }

    /**
     * Gets the maximum number of unique pairs possible
     */
    public static int getMaxUniquePairs() {
        return MAX_UNIQUE_PAIRS;
    }

    /**
     * Generates multiple sets of distinct pairs
     * Useful for generating different difficulty levels
     */
    public List<List<int[]>> generateMultipleSets(int... pairCounts) {
        List<List<int[]>> allSets = new ArrayList<>();

        for (int count : pairCounts) {
            allSets.add(generateDistinctPairs(count));
        }

        return allSets;
    }
}