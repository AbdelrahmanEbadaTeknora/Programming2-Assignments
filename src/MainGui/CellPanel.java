package MainGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Represents a single cell in the Sudoku board GUI
 * Handles user input and displays cell value
 */
public class CellPanel extends JPanel {
    private JTextField textField;
    private int row;
    private int col;
    private boolean isInitial;      // True if part of original puzzle
    private boolean isInvalid;
    private static final int CELL_SIZE = 60;
    private static final int FONT_SIZE = 20;

    public CellPanel(int row, int col, int value, boolean isInitial) {
        this.row = row;
        this.col = col;
        this.isInitial = isInitial;
        this.isInvalid = false;

        setLayout(new GridLayout(1, 1));
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Create text field
        textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        textField.setFocusTraversalKeysEnabled(false);

        // Set value
        if (value != 0) {
            textField.setText(String.valueOf(value));
        }

        // Configure based on whether it's initial value
        if (isInitial) {
            textField.setEditable(false);
            textField.setBackground(new Color(200, 200, 200));  // Gray for initial cells
            textField.setForeground(Color.BLACK);
            textField.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        } else {
            textField.setEditable(true);
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            setupInputValidation();
        }

        add(textField);
        updateAppearance();
    }

    /**
     * Sets up input validation for user entries
     */
    /**
     * Sets up input validation for user entries
     */
    private void setupInputValidation() {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String input = String.valueOf(e.getKeyChar());

                // Allow backspace and delete
                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyChar() == KeyEvent.VK_DELETE) {
                    return;
                }

                // Get current text
                String currentText = textField.getText();

                // Prevent typing if already has 1 digit
                if (currentText.length() >= 1) {
                    e.consume(); // Block the input
                    return;
                }

                // Only allow digits 1-9
                if (!input.matches("[1-9]")) {
                    e.consume(); // Block the input
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // No changes needed here
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No changes needed here
            }
        });

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBackground(new Color(255, 255, 200)); // Light yellow when focused
                textField.selectAll(); // Select text for easy replacement
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Validate the content is a single digit 1-9 or empty
                String text = textField.getText();
                if (!text.isEmpty()) {
                    // If more than 1 character, keep only first
                    if (text.length() > 1) {
                        textField.setText(text.substring(0, 1));
                        text = textField.getText();
                    }

                    // Check if it's a digit 1-9
                    if (!text.matches("[1-9]")) {
                        textField.setText(""); // Clear if not valid
                    }
                }

                // Update background color
                if (isInvalid) {
                    textField.setBackground(new Color(255, 200, 200));
                } else {
                    textField.setBackground(Color.WHITE);
                }
            }
        });
    }


    /**
     * Gets the value in the cell
     */
    public int getValue() {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Sets the value in the cell
     */
    /**
     * Sets the value in the cell
     */
    public void setValue(int value) {
        // Only allow 0 (empty) or digits 1-9
        if (value == 0) {
            textField.setText("");
        } else if (value >= 1 && value <= 9) {
            textField.setText(String.valueOf(value));
        }
        // If value is not 0-9, ignore it (don't set anything)
    }

    /**
     * Gets the row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position
     */
    public int getCol() {
        return col;
    }

    /**
     * Checks if cell is editable (not part of initial puzzle)
     */
    public boolean isEditable() {
        return !isInitial;
    }

    /**
     * Checks if cell is part of initial puzzle
     */
    public boolean isInitialCell() {
        return isInitial;
    }

    /**
     * Marks cell as invalid
     */
    public void setInvalid(boolean invalid) {
        this.isInvalid = invalid;
        updateAppearance();
    }

    /**
     * Updates cell appearance based on state
     */
    private void updateAppearance() {
        if (isInitial) {
            textField.setBackground(new Color(200, 200, 200));  // Gray for initial
            textField.setForeground(Color.BLACK);
        } else if (isInvalid) {
            textField.setBackground(new Color(255, 200, 200)); // Light red for invalid
            textField.setForeground(Color.RED);
        } else {
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
        }
    }

    /**
     * Clears the cell (sets to empty)
     * Only works if cell is editable
     */
    public void clear() {
        if (isEditable()) {
            textField.setText("");
            isInvalid = false;
            updateAppearance();
        }
    }

    /**
     * Focuses the cell
     */
    @Override
    public void requestFocus() {
        textField.requestFocus();
    }

    /**
     * Enables/disables the cell
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!isInitial) {
            textField.setEnabled(enabled);
        }
    }

    /**
     * Gets the text field for advanced operations
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * Checks if cell is empty
     */
    public boolean isEmpty() {
        return getValue() == 0;
    }

    /**
     * Checks if cell has a value
     */
    public boolean isFilled() {
        return getValue() != 0;
    }

    /**
     * Gets string representation
     */
    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ")=" + getValue() +
                (isInitial ? "[INITIAL]" : "[EDITABLE]");
    }
}