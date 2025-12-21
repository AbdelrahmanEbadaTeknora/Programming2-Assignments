package main.java.view;

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
    private boolean isInitial;
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
            textField.setBackground(new Color(200, 200, 200));
            textField.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        } else {
            textField.setEditable(true);
            textField.setBackground(Color.WHITE);
            setupInputValidation();
        }

        add(textField);
        updateAppearance();
    }

    /**
     * Sets up input validation for user entries
     */
    private void setupInputValidation() {
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String input = String.valueOf(e.getKeyChar());

                // Allow backspace and delete
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_DELETE) {
                    return;
                }

                // Only allow digits 1-9 or empty
                if (!input.matches("[1-9]")) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBackground(new Color(255, 255, 200)); // Light yellow
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (isInvalid) {
                    textField.setBackground(new Color(255, 200, 200)); // Light red
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
    public void setValue(int value) {
        if (value == 0) {
            textField.setText("");
        } else {
            textField.setText(String.valueOf(value));
        }
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
     * Checks if cell is editable
     */
    public boolean isEditable() {
        return !isInitial;
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
            textField.setBackground(new Color(200, 200, 200));
            textField.setForeground(Color.BLACK);
        } else if (isInvalid) {
            textField.setBackground(new Color(255, 200, 200)); // Light red
            textField.setForeground(Color.RED);
        } else {
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
        }
    }

    /**
     * Clears the cell
     */
    public void clear() {
        if (!isInitial) {
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
}