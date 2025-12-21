package MainGui;

import controller.Controllable;
import controller.GameController;
import Models.Catalog;
import Models.enums.DifficultyLevel;
import exceptions.InvalidGameException;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import OptionalHelperClasses.UserAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Main GUI window for Sudoku game
 * Manages view layer and coordinates with controller
 */
public class SudokuGUI extends JFrame {
    private Controllable controller;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MainMenuPanel mainMenuPanel;
    private GameBoardPanel gameBoardPanel;
    private ControlPanel controlPanel;
    private int[][] currentBoardGrid;
    private int[][] originalBoardGrid;

    public SudokuGUI(Controllable controller) {
        this.controller = controller;
        setupGUI();
        initializeGameStructure();
    }

    /**
     * Sets up the main GUI
     */
    private void setupGUI() {
        setTitle("Sudoku Game");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Card layout for switching between menu and game
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Main menu
        mainMenuPanel = new MainMenuPanel();
        setupMainMenuListeners();
        mainPanel.add(mainMenuPanel, "MENU");

        // Game board panel will be added when game starts
        setContentPane(mainPanel);
        setVisible(true);

        // Show main menu
        showMainMenu();
    }

    /**
     * Initializes the game structure and creates sample games if needed
     */
    private void initializeGameStructure() {
        System.out.println("\n=== Initializing Game Structure ===");
        try {
            // Initialize folder structure
            storageAndLogging.GameStorage.initializeFolderStructure();
            System.out.println("Folder structure initialized");

            // Check if we need to generate games
            Models.Catalog catalog = controller.getCatalog();
            System.out.println("Catalog check - hasAllModes: " + catalog.hasAllModes());

            if (!catalog.hasAllModes()) {
                System.out.println("Not all game modes exist, generating default games...");
                generateDefaultGames();
            } else {
                System.out.println("All game modes already exist!");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize game structure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates default games from a solved puzzle
     */
    private void generateDefaultGames() {
        try {
            System.out.println("\n=== Generating Default Games ===");

            // Create a solved Sudoku board (valid 9x9)
            int[][] solvedBoard = {
                    {1, 2, 3, 4, 5, 6, 7, 8, 9},
                    {4, 5, 6, 7, 8, 9, 1, 2, 3},
                    {7, 8, 9, 1, 2, 3, 4, 5, 6},
                    {2, 3, 4, 5, 6, 7, 8, 9, 1},
                    {5, 6, 7, 8, 9, 1, 2, 3, 4},
                    {8, 9, 1, 2, 3, 4, 5, 6, 7},
                    {3, 4, 5, 6, 7, 8, 9, 1, 2},
                    {6, 7, 8, 9, 1, 2, 3, 4, 5},
                    {9, 1, 2, 3, 4, 5, 6, 7, 8}
            };

            System.out.println("Calling controller.driveGames() with default solved board...");

            // Generate games from this solved board
            controller.driveGames(solvedBoard);

            System.out.println("Default games generated successfully!\n");
        } catch (Exception e) {
            System.err.println("Failed to generate default games: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up listeners for main menu buttons
     */
    private void setupMainMenuListeners() {
        mainMenuPanel.addContinueListener(e -> continuePreviousGame());
        mainMenuPanel.addEasyListener(e -> startNewGame(DifficultyLevel.EASY));
        mainMenuPanel.addMediumListener(e -> startNewGame(DifficultyLevel.MEDIUM));
        mainMenuPanel.addHardListener(e -> startNewGame(DifficultyLevel.HARD));
        mainMenuPanel.addLoadFileListener(e -> loadFromFile());
    }

    /**
     * Shows the main menu
     */
    private void showMainMenu() {
        Catalog catalog = controller.getCatalog();
        mainMenuPanel.setContinueButtonEnabled(catalog.hasCurrent());
        cardLayout.show(mainPanel, "MENU");
    }

    /**
     * Continues previous unfinished game
     */
    private void continuePreviousGame() {
        try {
            int[][] board = controller.getGame((char) 0); // 0 for incomplete
            showGameBoard(board);
        } catch (NotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "No unfinished game found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Starts a new game with specified difficulty
     */
    private void startNewGame(DifficultyLevel difficulty) {
        try {
            System.out.println("Loading " + difficulty + " game...");
            char diffChar = difficulty.toString().charAt(0);
            int[][] board = controller.getGame(diffChar);

            // Verify board was loaded correctly
            int emptyCells = countEmptyCells(board);
            System.out.println("Board loaded - Empty cells: " + emptyCells);

            showGameBoard(board);
        } catch (NotFoundException e) {
            System.err.println("Game not found: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Game not found for difficulty: " + difficulty + "\n\nPlease load a puzzle file first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Counts empty cells in a board
     */
    private int countEmptyCells(int[][] board) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Loads puzzle from file
     */
    private void loadFromFile() {
        System.out.println("\nDEBUG: loadFromFile() called");

        String filePath = FileInputDialog.showDialog(this);
        System.out.println("DEBUG: Selected file: " + filePath);

        if (filePath != null && !filePath.isEmpty()) {
            try {
                // Read board from file
                System.out.println("DEBUG: Reading board from file...");
                int[][] board = readBoardFromFile(filePath);
                System.out.println("DEBUG: Board read successfully");

                // Print first few cells
                System.out.println("DEBUG: Board sample (first 3x3):");
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        System.out.print(board[i][j] + " ");
                    }
                    System.out.println();
                }

                System.out.println("DEBUG: Calling controller.driveGames()...");
                controller.driveGames(board);

                JOptionPane.showMessageDialog(this,
                        "Puzzle loaded and games generated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                showMainMenu();
            } catch (SolutionInvalidException e) {
                System.err.println("DEBUG: SolutionInvalidException: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Invalid puzzle: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                System.err.println("DEBUG: General Exception: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Failed to load file: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("DEBUG: No file selected or dialog cancelled");
        }
    }

    /**
     * Reads board from file (simple format: 9 lines of 9 space-separated numbers)
     */
    private int[][] readBoardFromFile(String filePath) throws IOException {
        int[][] board = new int[9][9];
        try (java.io.BufferedReader reader =
                     new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 9) {
                String[] values = line.trim().split("\\s+");
                if (values.length != 9) {
                    throw new IOException("Invalid line: expected 9 values");
                }
                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }
            if (row < 9) {
                throw new IOException("File has fewer than 9 rows");
            }
        }
        return board;
    }

    /**
     * Shows the game board
     */
    private void showGameBoard(int[][] board) {
        currentBoardGrid = board;
        originalBoardGrid = copyBoard(board);

        // Remove previous game panel if exists
        java.awt.Component[] components = mainPanel.getComponents();
        for (java.awt.Component comp : components) {
            if (comp instanceof JPanel && !(comp instanceof MainMenuPanel)) {
                mainPanel.remove(comp);
            }
        }

        // Create game panel
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout(10, 10));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Board
        gameBoardPanel = new GameBoardPanel(currentBoardGrid, originalBoardGrid);
        gamePanel.add(gameBoardPanel, BorderLayout.CENTER);

        // Control panel
        controlPanel = new ControlPanel();
        setupGameControls();
        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel, "GAME");
    }

    /**
     * Sets up game control listeners
     */
    private void setupGameControls() {
        controlPanel.addVerifyListener(e -> verifyGame());
        controlPanel.addSolveListener(e -> solveGame());
        controlPanel.addUndoListener(e -> undoMove());
        controlPanel.addClearListener(e -> clearBoard());
        controlPanel.addNewGameListener(e -> showMainMenu());
    }

    /**
     * Verifies the current game state
     */
    private void verifyGame() {
        int[][] board = gameBoardPanel.getBoardState();
        boolean[][] invalidCells = controller.verifyGame(board);

        gameBoardPanel.markInvalidCells(invalidCells);

        // Count invalid cells
        int invalidCount = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (invalidCells[i][j]) {
                    invalidCount++;
                }
            }
        }

        if (invalidCount == 0) {
            if (gameBoardPanel.isComplete()) {
                controlPanel.setStatus("✓ Puzzle Solved! Congratulations!");
            } else {
                controlPanel.setStatus("✓ Current entries are valid");
            }
        } else {
            controlPanel.setStatus("✗ " + invalidCount + " invalid cells found");
        }
    }

    /**
     * Solves the game
     */
    private void solveGame() {
        try {
            int[][] board = gameBoardPanel.getBoardState();
            int[][] solution = controller.solveGame(board);
            gameBoardPanel.fillSolution(solution);
            controlPanel.setStatus("✓ Puzzle solved!");
        } catch (InvalidGameException e) {
            JOptionPane.showMessageDialog(this,
                    "Cannot solve: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Undoes last move
     */
    private void undoMove() {
        // This would require access to GameController's undo method
        controlPanel.setStatus("Undo functionality to be implemented");
    }

    /**
     * Clears all user entries
     */
    private void clearBoard() {
        int option = JOptionPane.showConfirmDialog(this,
                "Clear all entries?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            gameBoardPanel.clearUserEntries();
            gameBoardPanel.clearInvalidMarkings();
            controlPanel.setStatus("Board cleared");
        }
    }

    /**
     * Copies a 2D array
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

    /**
     * Updates solve button based on empty cell count
     */
    public void updateSolveButtonState() {
        if (controlPanel != null) {
            int emptyCells = gameBoardPanel.countEmptyCells();
            controlPanel.setSolveButtonEnabled(emptyCells == 5);
        }
    }

    /**
     * Main entry point - Creates and starts the Sudoku GUI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create the game controller
                GameController controller = new GameController();

                // Create and show the main GUI window
                SudokuGUI gui = new SudokuGUI(controller);

                System.out.println("Sudoku GUI started successfully!");
            } catch (Exception e) {
                System.err.println("Failed to start Sudoku GUI: " + e.getMessage());
                e.printStackTrace();

                // Show error dialog
                JOptionPane.showMessageDialog(null,
                        "Failed to start application: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}