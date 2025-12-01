package com.sudoku.output;

import com.sudoku.model.ValidationResult;

/**
 * Handles printing of validation results to console.
 * Provides utility methods for different output scenarios.
 *
 * @author Member 2
 */
public class ResultPrinter {

    /**
     * Prints the validation result to standard output.
     * Uses OutputFormatter to format the result.
     *
     * @param result the ValidationResult to print
     */
    public static void print(ValidationResult result) {
        String formattedOutput = OutputFormatter.format(result);
        System.out.println(formattedOutput);
    }

    /**
     * Prints the validation result with a custom prefix message.
     * Useful for debugging or adding context to the output.
     *
     * @param result the ValidationResult to print
     * @param prefix message to print before the result
     */
    public static void printWithPrefix(ValidationResult result, String prefix) {
        System.out.println(prefix);
        print(result);
    }

    /**
     * Prints error message to standard error stream.
     * Used for reporting errors during validation or file processing.
     *
     * @param message the error message to print
     */
    public static void printError(String message) {
        System.err.println("ERROR: " + message);
    }

    /**
     * Prints debug information (can be controlled by a debug flag).
     * Useful for development and troubleshooting.
     *
     * @param message the debug message to print
     * @param debug whether debug mode is enabled
     */
    public static void printDebug(String message, boolean debug) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }
}