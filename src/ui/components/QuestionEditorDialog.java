package ui.components;

import java.awt.*;
import javax.swing.*;
import models.Question;

public class QuestionEditorDialog extends JDialog {
    private Question question;
    private boolean isNewQuestion;

    private JTextArea questionTextArea;
    private JTextField[] optionFields;
    private JComboBox<String> correctAnswerCombo;
    private JSpinner pointsSpinner;

    private boolean saved = false;

    public QuestionEditorDialog(JDialog parent, Question existingQuestion) {
        super(parent, existingQuestion == null ? "Add Question" : "Edit Question", true);
        this.question = existingQuestion;
        this.isNewQuestion = (existingQuestion == null);
        this.optionFields = new JTextField[4];

        initializeUI();

        if (!isNewQuestion) {
            loadQuestionData();
        }

        setSize(600, 500);
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Question panel
        mainPanel.add(createQuestionPanel(), BorderLayout.NORTH);

        // Options panel
        mainPanel.add(createOptionsPanel(), BorderLayout.CENTER);

        // Settings panel
        mainPanel.add(createSettingsPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Buttons panel
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createQuestionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Question Text"));

        questionTextArea = new JTextArea(4, 40);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(questionTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Answer Options"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Option A:", "Option B:", "Option C:", "Option D:"};

        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            optionFields[i] = new JTextField(30);
            optionFields[i].setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(optionFields[i], gbc);
        }

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Settings"));

        // Correct answer
        panel.add(new JLabel("Correct Answer:"));
        correctAnswerCombo = new JComboBox<>(new String[]{"Option A", "Option B", "Option C", "Option D"});
        panel.add(correctAnswerCombo);

        panel.add(Box.createHorizontalStrut(20));

        // Points
        panel.add(new JLabel("Points:"));
        pointsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        pointsSpinner.setPreferredSize(new Dimension(60, 25));
        panel.add(pointsSpinner);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveQuestion());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private void loadQuestionData() {
        if (question == null) return;

        questionTextArea.setText(question.getQuestionText());

        String[] options = question.getOptions();
        if (options != null) {
            for (int i = 0; i < Math.min(4, options.length); i++) {
                optionFields[i].setText(options[i]);
            }
        }

        correctAnswerCombo.setSelectedIndex(question.getCorrectOptionIndex());
        pointsSpinner.setValue(question.getPoints());
    }

    private boolean validateQuestion() {
        // Check question text
        if (questionTextArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the question text.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check at least 2 options are filled
        int filledOptions = 0;
        for (JTextField field : optionFields) {
            if (!field.getText().trim().isEmpty()) {
                filledOptions++;
            }
        }

        if (filledOptions < 2) {
            JOptionPane.showMessageDialog(this,
                    "Please provide at least 2 answer options.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check correct answer option is filled
        int correctIndex = correctAnswerCombo.getSelectedIndex();
        if (optionFields[correctIndex].getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "The correct answer option cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveQuestion() {
        if (!validateQuestion()) {
            return;
        }

        // Create or update question
        if (isNewQuestion) {
            question = new Question();
        }

        question.setQuestionText(questionTextArea.getText().trim());

        String[] options = new String[4];
        for (int i = 0; i < 4; i++) {
            options[i] = optionFields[i].getText().trim();
        }
        question.setOptions(options);

        question.setCorrectOptionIndex(correctAnswerCombo.getSelectedIndex());
        question.setPoints((Integer) pointsSpinner.getValue());

        saved = true;
        dispose();
    }

    public Question getQuestion() {
        return saved ? question : null;
    }
}