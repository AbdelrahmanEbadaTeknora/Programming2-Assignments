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
     * Add duplicate information
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

    /**
     * Inner class to store duplicate information
     */
    public static class DuplicateInfo {
        private final int index;              // Row/Col/Box number (1-based)
        private final int duplicateValue;     // The duplicate value
        private final List<Integer> positions; // Positions where it appears

        /**
         * Constructor for DuplicateInfo
         * @param index Row/Column/Box index (1-based)
         * @param duplicateValue The value that is duplicated
         * @param positions List of positions (1-based column/row indices or cell positions)
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
         * Get the positions where duplicate appears
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