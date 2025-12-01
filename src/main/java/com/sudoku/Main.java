package com.sudoku;

import com.sudoku.model.SudokuBoard;
import com.sudoku.model.ValidationResult;
import com.sudoku.parser.CSVParser;
import com.sudoku.parser.FileValidator;
import com.sudoku.factory.ValidatorFactory;
import com.sudoku.output.ResultPrinter;

/**
 * Main entry point for Sudoku Validator CLI application
 * Usage: java -jar sudoku.jar <filepath> <mode>
 * Modes: 0 (sequential), 3 (three threads), 27 (twenty-seven threads)
 */
public class Main {

    public static void main(String[] args) {
        // Check command line arguments
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }

        String filepath = args[0];
        int mode;

        // Parse mode argument
        try {
            mode = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error: Mode must be a number (0, 3, or 27)");
            printUsage();
            System.exit(1);
            return;
        }

        // Validate mode
        if (mode != 0 && mode != 3 && mode != 27) {
            System.err.println("Error: Invalid mode. Must be 0, 3, or 27.");
            printUsage();
            System.exit(1);
        }

        try {
            // Validate file exists and is readable
            if (!FileValidator.isValidFile(filepath)) {
                System.err.println("Error: Invalid file - " + filepath);
                System.err.println("Please ensure the file exists and is readable.");
                System.exit(1);
            }

            // Parse CSV file
            CSVParser parser = new CSVParser();
            SudokuBoard board = parser.parseFile(filepath);

            // Display processing information
            System.out.println("========================================");
            System.out.println("Sudoku Solution Verifier");
            System.out.println("========================================");
            System.out.println("File: " + filepath);
            System.out.println("Mode: " + getModeDescription(mode));
            System.out.println("========================================\n");

            // Create appropriate validator using Factory Pattern
            long startTime = System.currentTimeMillis();
            ValidationResult result = ValidatorFactory.createValidator(board, mode).validate();
            long endTime = System.currentTimeMillis();

            // Print validation results
            ResultPrinter.print(result);

            // Print execution time
            System.out.println("\n========================================");
            System.out.println("Execution Time: " + (endTime - startTime) + " ms");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("Error during validation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Prints usage information
     */
    private static void printUsage() {
        System.out.println("\n========================================");
        System.out.println("Sudoku Solution Verifier - Usage");
        System.out.println("========================================");
        System.out.println("Usage: java -jar sudoku.jar <filepath> <mode>");
        System.out.println("\nArguments:");
        System.out.println("  <filepath>  Path to CSV file containing 9x9 Sudoku board");
        System.out.println("  <mode>      Validation mode:");
        System.out.println("              0  - Sequential (single thread)");
        System.out.println("              3  - Parallel with 3 threads");
        System.out.println("              27 - Parallel with 27 threads");
        System.out.println("\nExample:");
        System.out.println("  java -jar sudoku.jar board.csv 3");
        System.out.println("========================================\n");
    }

    /**
     * Returns a description of the validation mode
     */
    private static String getModeDescription(int mode) {
        switch (mode) {
            case 0:
                return "Sequential (1 thread)";
            case 3:
                return "Parallel (4 threads: 1 main + 3 workers)";
            case 27:
                return "Parallel (28 threads: 1 main + 27 workers)";
            default:
                return "Unknown";
        }
    }
}