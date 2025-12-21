package main.java.view;

import main.java.Models.enums.DifficultyLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main menu panel for game startup
 * Shows options to continue unfinished game or start new game
 */
public class MainMenuPanel extends JPanel {
    private JButton continueButton;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private JButton loadFileButton;
    private JLabel messageLabel;

    public MainMenuPanel() {
        setupUI();
    }

    /**
     * Sets up the main menu UI
     */
    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Sudoku Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(titleLabel);
        add(Box.createVerticalStrut(40));

        // Message label
        messageLabel = new JLabel("Welcome! Choose an option:");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(messageLabel);
        add(Box.createVerticalStrut(30));

        // Continue button
        continueButton = createMenuButton("Continue Unfinished Game",
                new Color(100, 150, 200));
        add(continueButton);
        add(Box.createVerticalStrut(15));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 2));
        separator.setForeground(new Color(100, 100, 100));
        add(separator);
        add(Box.createVerticalStrut(15));

        // Difficulty selection label
        JLabel diffLabel = new JLabel("Start New Game:");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 14));
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(diffLabel);
        add(Box.createVerticalStrut(10));

        // Difficulty buttons
        easyButton = createMenuButton("Easy", new Color(100, 200, 100));
        add(easyButton);
        add(Box.createVerticalStrut(10));

        mediumButton = createMenuButton("Medium", new Color(200, 200, 100));
        add(mediumButton);
        add(Box.createVerticalStrut(10));

        hardButton = createMenuButton("Hard", new Color(200, 100, 100));
        add(hardButton);
        add(Box.createVerticalStrut(20));

        // Separator
        separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 2));
        separator.setForeground(new Color(100, 100, 100));
        add(separator);
        add(Box.createVerticalStrut(15));

        // Load file button
        loadFileButton = createMenuButton("Load Solved Puzzle File",
                new Color(150, 150, 150));
        add(loadFileButton);

        add(Box.createVerticalGlue());
    }

    /**
     * Creates a styled menu button
     */
    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(
                        Math.min(bgColor.getRed() + 30, 255),
                        Math.min(bgColor.getGreen() + 30, 255),
                        Math.min(bgColor.getBlue() + 30, 255)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Gets continue button
     */
    public JButton getContinueButton() {
        return continueButton;
    }

    /**
     * Gets easy button
     */
    public JButton getEasyButton() {
        return easyButton;
    }

    /**
     * Gets medium button
     */
    public JButton getMediumButton() {
        return mediumButton;
    }

    /**
     * Gets hard button
     */
    public JButton getHardButton() {
        return hardButton;
    }

    /**
     * Gets load file button
     */
    public JButton getLoadFileButton() {
        return loadFileButton;
    }

    /**
     * Sets message label text
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Enables/disables continue button
     */
    public void setContinueButtonEnabled(boolean enabled) {
        continueButton.setEnabled(enabled);
    }

    /**
     * Adds listener to continue button
     */
    public void addContinueListener(ActionListener listener) {
        continueButton.addActionListener(listener);
    }

    /**
     * Adds listener to easy button
     */
    public void addEasyListener(ActionListener listener) {
        easyButton.addActionListener(listener);
    }

    /**
     * Adds listener to medium button
     */
    public void addMediumListener(ActionListener listener) {
        mediumButton.addActionListener(listener);
    }

    /**
     * Adds listener to hard button
     */
    public void addHardListener(ActionListener listener) {
        hardButton.addActionListener(listener);
    }

    /**
     * Adds listener to load file button
     */
    public void addLoadFileListener(ActionListener listener) {
        loadFileButton.addActionListener(listener);
    }
}