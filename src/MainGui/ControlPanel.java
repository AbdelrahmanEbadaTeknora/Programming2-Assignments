package MainGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Control panel with game action buttons
 * Provides Verify, Solve, Undo, Clear, and New Game buttons
 */
public class ControlPanel extends JPanel {
    private JButton verifyButton;
    private JButton solveButton;
    private JButton undoButton;
    private JButton clearButton;
    private JButton newGameButton;
    private JLabel statusLabel;

    public ControlPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 240, 240));

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        // Create status label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.SOUTH);
    }

    /**
     * Creates the button panel with all control buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBackground(new Color(240, 240, 240));

        // Verify Button
        verifyButton = new JButton("Verify");
        verifyButton.setPreferredSize(new Dimension(100, 40));
        verifyButton.setFont(new Font("Arial", Font.BOLD, 12));
        verifyButton.setFocusPainted(false);
        verifyButton.setBackground(new Color(100, 150, 200));
        verifyButton.setForeground(Color.WHITE);
        panel.add(verifyButton);

        // Solve Button (initially disabled)
        solveButton = new JButton("Solve");
        solveButton.setPreferredSize(new Dimension(100, 40));
        solveButton.setFont(new Font("Arial", Font.BOLD, 12));
        solveButton.setFocusPainted(false);
        solveButton.setBackground(new Color(100, 150, 200));
        solveButton.setForeground(Color.WHITE);
        solveButton.setEnabled(false);
        panel.add(solveButton);

        // Undo Button
        undoButton = new JButton("Undo");
        undoButton.setPreferredSize(new Dimension(100, 40));
        undoButton.setFont(new Font("Arial", Font.BOLD, 12));
        undoButton.setFocusPainted(false);
        undoButton.setBackground(new Color(150, 150, 150));
        undoButton.setForeground(Color.WHITE);
        panel.add(undoButton);

        // Clear Button
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(100, 40));
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setFocusPainted(false);
        clearButton.setBackground(new Color(200, 100, 100));
        clearButton.setForeground(Color.WHITE);
        panel.add(clearButton);

        // New Game Button
        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100, 40));
        newGameButton.setFont(new Font("Arial", Font.BOLD, 12));
        newGameButton.setFocusPainted(false);
        newGameButton.setBackground(new Color(100, 180, 100));
        newGameButton.setForeground(Color.WHITE);
        panel.add(newGameButton);

        return panel;
    }

    /**
     * Gets Verify button
     */
    public JButton getVerifyButton() {
        return verifyButton;
    }

    /**
     * Gets Solve button
     */
    public JButton getSolveButton() {
        return solveButton;
    }

    /**
     * Gets Undo button
     */
    public JButton getUndoButton() {
        return undoButton;
    }

    /**
     * Gets Clear button
     */
    public JButton getClearButton() {
        return clearButton;
    }

    /**
     * Gets New Game button
     */
    public JButton getNewGameButton() {
        return newGameButton;
    }

    /**
     * Sets status message
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Gets status label
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    /**
     * Enables/disables Solve button
     */
    public void setSolveButtonEnabled(boolean enabled) {
        solveButton.setEnabled(enabled);
    }

    /**
     * Enables/disables Undo button
     */
    public void setUndoButtonEnabled(boolean enabled) {
        undoButton.setEnabled(enabled);
    }

    /**
     * Adds action listener to Verify button
     */
    public void addVerifyListener(ActionListener listener) {
        verifyButton.addActionListener(listener);
    }

    /**
     * Adds action listener to Solve button
     */
    public void addSolveListener(ActionListener listener) {
        solveButton.addActionListener(listener);
    }

    /**
     * Adds action listener to Undo button
     */
    public void addUndoListener(ActionListener listener) {
        undoButton.addActionListener(listener);
    }

    /**
     * Adds action listener to Clear button
     */
    public void addClearListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    /**
     * Adds action listener to New Game button
     */
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Disables all buttons
     */
    public void disableAllButtons() {
        verifyButton.setEnabled(false);
        solveButton.setEnabled(false);
        undoButton.setEnabled(false);
        clearButton.setEnabled(false);
        newGameButton.setEnabled(false);
    }

    /**
     * Enables all buttons
     */
    public void enableAllButtons() {
        verifyButton.setEnabled(true);
        clearButton.setEnabled(true);
        newGameButton.setEnabled(true);
    }

    /**
     * Resets button states to initial
     */
    public void resetButtonStates() {
        verifyButton.setEnabled(true);
        solveButton.setEnabled(false);
        undoButton.setEnabled(false);
        clearButton.setEnabled(true);
        newGameButton.setEnabled(true);
        statusLabel.setText("Ready");
    }

    /**
     * Gets status message
     */
    public String getStatus() {
        return statusLabel.getText();
    }
}