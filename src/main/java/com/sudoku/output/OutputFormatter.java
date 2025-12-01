package com.sudoku.output;

import com.sudoku.model.ValidationResult;
import java.util.List;
import java.util.Map;

/**
 * Formats validation results for output according to project specification.
 *
 * Output format:
 * - Valid case: "VALID"
 * - Invalid case: "INVALID" followed by details of all duplicates
 *
 * @author Member 2
 */
public class OutputFormatter {

    /**
     * Formats the validation result as a string
     *
     * @param result the ValidationResult to format
     * @return formatted string ready for output
     */
    public static String format(ValidationResult result) {
        if (result.isValid()) {
            return "VALID";
        } else {
            return formatInvalidResult(result);
        }
    }

    /**
     * Formats an invalid result with all duplicate information.
     *
     * Format:
     * INVALID
     * ROW N, #V, [positions]
     * ...
     * ------------------------------------------
     * COL N, #V, [positions]
     * ...
     * ------------------------------------------
     * BOX N, #V, [positions]
     * ...
     *
     * @param result the ValidationResult containing duplicates
     * @return formatted string with all duplicate details
     */
    private static String formatInvalidResult(ValidationResult result) {
        StringBuilder output = new StringBuilder();
        output.append("INVALID\n");

        // Format row duplicates
        Map<Integer, Map<Integer, List<Integer>>> rowDuplicates = result.getRowDuplicates();
        for (int rowNum = 1; rowNum <= 9; rowNum++) {
            if (rowDuplicates.containsKey(rowNum)) {
                Map<Integer, List<Integer>> duplicatesInRow = rowDuplicates.get(rowNum);
                for (Map.Entry<Integer, List<Integer>> entry : duplicatesInRow.entrySet()) {
                    int duplicateValue = entry.getKey();
                    List<Integer> positions = entry.getValue();
                    output.append(String.format("ROW %d, #%d, %s\n",
                            rowNum, duplicateValue, formatPositions(positions)));
                }
            }
        }

        output.append("------------------------------------------\n");

        // Format column duplicates
        Map<Integer, Map<Integer, List<Integer>>> colDuplicates = result.getColumnDuplicates();
        for (int colNum = 1; colNum <= 9; colNum++) {
            if (colDuplicates.containsKey(colNum)) {
                Map<Integer, List<Integer>> duplicatesInCol = colDuplicates.get(colNum);
                for (Map.Entry<Integer, List<Integer>> entry : duplicatesInCol.entrySet()) {
                    int duplicateValue = entry.getKey();
                    List<Integer> positions = entry.getValue();
                    output.append(String.format("COL %d, #%d, %s\n",
                            colNum, duplicateValue, formatPositions(positions)));
                }
            }
        }

        output.append("------------------------------------------\n");

        // Format box duplicates
        Map<Integer, Map<Integer, List<Integer>>> boxDuplicates = result.getBoxDuplicates();
        for (int boxNum = 1; boxNum <= 9; boxNum++) {
            if (boxDuplicates.containsKey(boxNum)) {
                Map<Integer, List<Integer>> duplicatesInBox = boxDuplicates.get(boxNum);
                for (Map.Entry<Integer, List<Integer>> entry : duplicatesInBox.entrySet()) {
                    int duplicateValue = entry.getKey();
                    List<Integer> positions = entry.getValue();
                    output.append(String.format("BOX %d, #%d, %s\n",
                            boxNum, duplicateValue, formatPositions(positions)));
                }
            }
        }

        return output.toString().trim();
    }

    /**
     * Formats a list of positions as [1, 2, 3, ...]
     * Converts from 0-based to 1-based indexing
     *
     * @param positions list of 0-based positions
     * @return formatted string like [1, 2, 3]
     */
    private static String formatPositions(List<Integer> positions) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < positions.size(); i++) {
            sb.append(positions.get(i) + 1); // Convert to 1-based indexing
            if (i < positions.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}