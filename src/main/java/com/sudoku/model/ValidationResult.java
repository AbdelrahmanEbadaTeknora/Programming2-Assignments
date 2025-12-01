package com.sudoku.model;

import com.sudoku.util.Constants;

import java.util.*;

/**
 * Represents the result of Sudoku validation
 * Contains information about validity and any duplicates found
 */
public class ValidationResult {
    private boolean isValid;
    private final Map<String, List<DuplicateInfo>> duplicates;

    /**
     * Constructor initializes an empty result
     */
    public ValidationResult() {
        this.isValid = true;
        this.duplicates = new LinkedHashMap<>();

        // Initialize empty lists for rows, columns, and boxes
        duplicates.put(Constants.ROW_LABEL, new ArrayList<>());
        duplicates.put(Constants.COL_LABEL, new ArrayList<>());
        duplicates.put(Constants.BOX_LABEL, new ArrayList<>());
    }

    /**
     * Check if the Sudoku solution is valid
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Set the validity status
     * @param valid Validity status
     */
    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    /**
     * Add duplicate information for a row
     * @param rowIndex Row number (1-based)
     * @param duplicateValue The value that is duplicated
     * @param positions List of positions (0-based column indices where duplicate appears)
     */
    public void addRowDuplicate(int rowIndex, int duplicateValue, List<Integer> positions) {
        DuplicateInfo info = new DuplicateInfo(rowIndex, duplicateValue, new ArrayList<>(positions));
        addDuplicate(Constants.ROW_LABEL, info);
    }

    /**
     * Add duplicate information for a column
     * @param colIndex Column number (1-based)
     * @param duplicateValue The value that is duplicated
     * @param positions List of positions (0-based row indices where duplicate appears)
     */
    public void addColumnDuplicate(int colIndex, int duplicateValue, List<Integer> positions) {
        DuplicateInfo info = new DuplicateInfo(colIndex, duplicateValue, new ArrayList<>(positions));
        addDuplicate(Constants.COL_LABEL, info);
    }

    /**
     * Add duplicate information for a box
     * @param boxIndex Box number (1-based, 1-9)
     * @param duplicateValue The value that is duplicated
     * @param positions List of positions (0-based indices within the box, 0-8)
     */
    public void addBoxDuplicate(int boxIndex, int duplicateValue, List<Integer> positions) {
        DuplicateInfo info = new DuplicateInfo(boxIndex, duplicateValue, new ArrayList<>(positions));
        addDuplicate(Constants.BOX_LABEL, info);
    }

    /**
     * Add duplicate information (generic method)
     * @param type Type of validation (ROW, COL, BOX)
     * @param info Duplicate information
     */
    public void addDuplicate(String type, DuplicateInfo info) {
        if (!duplicates.containsKey(type)) {
            duplicates.put(type, new ArrayList<>());
        }
        duplicates.get(type).add(info);
        this.isValid = false;  // Mark as invalid
    }

    /**
     * Get all duplicates
     * @return Map of type to list of duplicate information
     */
    public Map<String, List<DuplicateInfo>> getDuplicates() {
        return duplicates;
    }

    /**
     * Get duplicates of a specific type
     * @param type Type (ROW, COL, BOX)
     * @return List of duplicate information
     */
    public List<DuplicateInfo> getDuplicatesByType(String type) {
        return duplicates.getOrDefault(type, new ArrayList<>());
    }

    /**
     * Get row duplicates in format for OutputFormatter
     * Returns Map<RowNumber, Map<DuplicateValue, List<Positions>>>
     * @return Nested map structure of row duplicates
     */
    public Map<Integer, Map<Integer, List<Integer>>> getRowDuplicates() {
        return convertToNestedMap(Constants.ROW_LABEL);
    }

    /**
     * Get column duplicates in format for OutputFormatter
     * Returns Map<ColNumber, Map<DuplicateValue, List<Positions>>>
     * @return Nested map structure of column duplicates
     */
    public Map<Integer, Map<Integer, List<Integer>>> getColumnDuplicates() {
        return convertToNestedMap(Constants.COL_LABEL);
    }

    /**
     * Get box duplicates in format for OutputFormatter
     * Returns Map<BoxNumber, Map<DuplicateValue, List<Positions>>>
     * @return Nested map structure of box duplicates
     */
    public Map<Integer, Map<Integer, List<Integer>>> getBoxDuplicates() {
        return convertToNestedMap(Constants.BOX_LABEL);
    }

    /**
     * Helper method to convert DuplicateInfo list to nested map structure
     * @param type The type (ROW, COL, or BOX)
     * @return Nested map: Map<Index, Map<Value, List<Positions>>>
     */
    private Map<Integer, Map<Integer, List<Integer>>> convertToNestedMap(String type) {
        Map<Integer, Map<Integer, List<Integer>>> result = new LinkedHashMap<>();

        List<DuplicateInfo> duplicateList = getDuplicatesByType(type);
        for (DuplicateInfo info : duplicateList) {
            int index = info.getIndex();
            int value = info.getDuplicateValue();
            List<Integer> positions = info.getPositions();

            // Create inner map if it doesn't exist
            if (!result.containsKey(index)) {
                result.put(index, new LinkedHashMap<>());
            }

            // Add the duplicate value and its positions
            result.get(index).put(value, new ArrayList<>(positions));
        }

        return result;
    }

    /**
     * Check if there are row errors
     * @return true if row duplicates exist
     */
    public boolean hasRowErrors() {
        return !getDuplicatesByType(Constants.ROW_LABEL).isEmpty();
    }

    /**
     * Check if there are column errors
     * @return true if column duplicates exist
     */
    public boolean hasColumnErrors() {
        return !getDuplicatesByType(Constants.COL_LABEL).isEmpty();
    }

    /**
     * Check if there are box errors
     * @return true if box duplicates exist
     */
    public boolean hasBoxErrors() {
        return !getDuplicatesByType(Constants.BOX_LABEL).isEmpty();
    }

    /**
     * Get total error count (number of duplicate entries across all types)
     * @return Total number of duplicate entries
     */
    public int getErrorCount() {
        int count = 0;
        for (List<DuplicateInfo> list : duplicates.values()) {
            count += list.size();
        }
        return count;
    }

    /**
     * Merge another validation result into this one
     * @param other Another validation result
     */
    public void merge(ValidationResult other) {
        if (!other.isValid()) {
            this.isValid = false;
        }

        for (Map.Entry<String, List<DuplicateInfo>> entry : other.getDuplicates().entrySet()) {
            String type = entry.getKey();
            List<DuplicateInfo> otherDuplicates = entry.getValue();

            if (!duplicates.containsKey(type)) {
                duplicates.put(type, new ArrayList<>());
            }
            duplicates.get(type).addAll(otherDuplicates);
        }
    }

    /**
     * Check if there are any duplicates
     * @return true if duplicates exist
     */
    public boolean hasDuplicates() {
        return !isValid;
    }

    @Override
    public String toString() {
        if (isValid) {
            return "VALID";
        }

        StringBuilder sb = new StringBuilder("INVALID\n");

        // Print row errors
        List<DuplicateInfo> rowErrors = getDuplicatesByType(Constants.ROW_LABEL);
        for (DuplicateInfo info : rowErrors) {
            sb.append(String.format("ROW %d, #%d, %s\n",
                    info.getIndex(),
                    info.getDuplicateValue(),
                    info.getPositions()));
        }

        if (!rowErrors.isEmpty()) {
            sb.append("------------------------------------------\n");
        }

        // Print column errors
        List<DuplicateInfo> colErrors = getDuplicatesByType(Constants.COL_LABEL);
        for (DuplicateInfo info : colErrors) {
            sb.append(String.format("COL %d, #%d, %s\n",
                    info.getIndex(),
                    info.getDuplicateValue(),
                    info.getPositions()));
        }

        if (!colErrors.isEmpty()) {
            sb.append("------------------------------------------\n");
        }

        // Print box errors
        List<DuplicateInfo> boxErrors = getDuplicatesByType(Constants.BOX_LABEL);
        for (DuplicateInfo info : boxErrors) {
            sb.append(String.format("BOX %d, #%d, %s\n",
                    info.getIndex(),
                    info.getDuplicateValue(),
                    info.getPositions()));
        }

        return sb.toString();
    }

    /**
     * Inner class to store duplicate information
     */
    public static class DuplicateInfo {
        private final int index;              // Row/Col/Box number (1-based)
        private final int duplicateValue;     // The duplicate value
        private final List<Integer> positions; // Positions where it appears (0-based)

        /**
         * Constructor for DuplicateInfo
         * @param index Row/Column/Box index (1-based)
         * @param duplicateValue The value that is duplicated
         * @param positions List of positions (0-based indices)
         */
        public DuplicateInfo(int index, int duplicateValue, List<Integer> positions) {
            this.index = index;
            this.duplicateValue = duplicateValue;
            this.positions = new ArrayList<>(positions);
        }

        /**
         * Get the index (row/column/box number)
         * @return Index (1-based)
         */
        public int getIndex() {
            return index;
        }

        /**
         * Get the duplicate value
         * @return Duplicate value
         */
        public int getDuplicateValue() {
            return duplicateValue;
        }

        /**
         * Get the positions where duplicate appears (0-based)
         * @return List of positions
         */
        public List<Integer> getPositions() {
            return new ArrayList<>(positions);
        }

        @Override
        public String toString() {
            return String.format("DuplicateInfo[index=%d, value=%d, positions=%s]",
                    index, duplicateValue, positions);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DuplicateInfo that = (DuplicateInfo) o;
            return index == that.index &&
                    duplicateValue == that.duplicateValue &&
                    Objects.equals(positions, that.positions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, duplicateValue, positions);
        }
    }
}