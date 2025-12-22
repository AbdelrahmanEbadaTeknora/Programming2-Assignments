package MainGui;

import Models.enums.DifficultyLevel;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for selecting game difficulty level
 * Returns selected difficulty or null if cancelled
 */
public class DifficultySelectionDialog extends JDialog {
    private DifficultyLevel selectedDifficulty;
    private boolean cancelled;

    public DifficultySelectionDialog(JFrame parent) {
        super(parent, "Select Difficulty", true);
        this.selectedDifficulty = null;
        this.cancelled = true;

        setupUI();
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Sets up the dialog UI
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Choose Difficulty Level");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton easyButton = createDifficultyButton("Easy", DifficultyLevel.EASY);
        JButton mediumButton = createDifficultyButton("Medium", DifficultyLevel.MEDIUM);
        JButton hardButton = createDifficultyButton("Hard", DifficultyLevel.HARD);

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Cancel button
        JPanel cancelPanel = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> {
            selectedDifficulty = null;
            cancelled = true;
            dispose();
        });
        cancelPanel.add(cancelButton);
        add(cancelPanel, BorderLayout.SOUTH);
    }

    private JButton createDifficultyButton(String text, DifficultyLevel difficulty) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);

        // Color based on difficulty
        switch (difficulty) {
            case EASY:
                button.setBackground(new Color(100, 200, 100));
                break;
            case MEDIUM:
                button.setBackground(new Color(200, 200, 100));
                break;
            case HARD:
                button.setBackground(new Color(200, 100, 100));
                break;
        }
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            selectedDifficulty = difficulty;
            cancelled = false;
            dispose();
        });

        return button;
    }

    public static DifficultyLevel showDialog(JFrame parent) {
        DifficultySelectionDialog dialog = new DifficultySelectionDialog(parent);
        dialog.setVisible(true);
        return dialog.selectedDifficulty;
    }

}