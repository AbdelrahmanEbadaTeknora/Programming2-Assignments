package MainGui;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the 9x9 Sudoku board with 3x3 boxes
 * Manages CellPanel components arranged in grid
 */
public class GameBoardPanel extends JPanel {
    private CellPanel[][] cells;
    private static final int BOARD_SIZE = 9;
    private static final int BOX_SIZE = 3;
    private int[][] originalBoard; // Store initial values

    public GameBoardPanel(int[][] boardGrid, int[][] originalGrid) {
        this.cells = new CellPanel[BOARD_SIZE][BOARD_SIZE];
        this.originalBoard = new int[BOARD_SIZE][BOARD_SIZE];

        // Copy original board for reference
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(originalGrid[i], 0, originalBoard[i], 0, BOARD_SIZE);
        }

        setLayout(new GridLayout(BOX_SIZE, BOX_SIZE, 2, 2));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(550, 550));

        // Create 3x3 boxes
        for (int boxRow = 0; boxRow < BOX_SIZE; boxRow++) {
            for (int boxCol = 0; boxCol < BOX_SIZE; boxCol++) {
                JPanel box = createBox(boardGrid, boxRow, boxCol);
                add(box);
            }
        }
    }

    /**
     * Creates a 3x3 box panel
     */
    private JPanel createBox(int[][] boardGrid, int boxRow, int boxCol) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayout(BOX_SIZE, BOX_SIZE, 1, 1));
        boxPanel.setBackground(Color.BLACK);
        boxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        int startRow = boxRow * BOX_SIZE;
        int startCol = boxCol * BOX_SIZE;

        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                int row = startRow + i;
                int col = startCol + j;
                int value = boardGrid[row][col];

                // Cell is initial if it has a non-zero value in the original board
                boolean isInitial = originalBoard[row][col] != 0;

                CellPanel cell = new CellPanel(row, col, value, isInitial);
                cells[row][col] = cell;
                boxPanel.add(cell);
            }
        }

        return boxPanel;
    }

    /**
     * Gets the current board state as a 2D array
     */
    public int[][] getBoardState() {
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = cells[i][j].getValue();
            }
        }
        return board;
    }

    /**
     * Sets a cell value
     */
    public void setCellValue(int row, int col, int value) {
        if (isValidPosition(row, col)) {
            cells[row][col].setValue(value);
        }
    }

    /**
     * Gets a cell value
     */
    public int getCellValue(int row, int col) {
        if (isValidPosition(row, col)) {
            return cells[row][col].getValue();
        }
        return 0;
    }

    /**
     * Marks invalid cells
     */
    public void markInvalidCells(boolean[][] invalidCells) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (invalidCells[i][j]) {
                    cells[i][j].setInvalid(true);
                } else {
                    cells[i][j].setInvalid(false);
                }
            }
        }
    }

    /**
     * Clears all invalid markings
     */
    public void clearInvalidMarkings() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].setInvalid(false);
            }
        }
    }

    /**
     * Clears all user-entered values (keeps initial values)
     * Only clears cells that are editable (not part of original puzzle)
     */
    public void clearUserEntries() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Only clear if cell is editable (not initial)
                if (cells[i][j].isEditable()) {
                    cells[i][j].clear();
                }
            }
        }
    }

    /**
     * Fills the board with solution
     */
    public void fillSolution(int[][] solution) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].setValue(solution[i][j]);
            }
        }
    }

    /**
     * Enables/disables all cells
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].setEnabled(enabled);
            }
        }
    }

    /**
     * Gets a specific cell panel
     */
    public CellPanel getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return cells[row][col];
        }
        return null;
    }

    /**
     * Counts empty cells in current board
     */
    public int countEmptyCells() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cells[i][j].getValue() == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if board is completely filled
     */
    public boolean isComplete() {
        return countEmptyCells() == 0;
    }

    /**
     * Validates position
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Focuses a specific cell
     */
    public void focusCell(int row, int col) {
        if (isValidPosition(row, col)) {
            cells[row][col].requestFocus();
        }
    }

    /**
     * Gets the original board
     */
    public int[][] getOriginalBoard() {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(originalBoard[i], 0, copy[i], 0, BOARD_SIZE);
        }
        return copy;
    }

    /**
     * Resets board to original state
     */
    public void resetToOriginal() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Reset to original value and mark as initial
                cells[i][j].setValue(originalBoard[i][j]);
            }
        }
    }

    /**
     * Gets count of initial (non-editable) cells
     */
    public int getInitialCellCount() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!cells[i][j].isEditable()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Gets count of user-filled cells
     */
    public int getUserFilledCellCount() {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cells[i][j].isEditable() && cells[i][j].getValue() != 0) {
                    count++;
                }
            }
        }
        return count;
    }
}