package ui.components;

import models.*;
import services.QuizService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class QuizTakingDialog extends JDialog {
    private Quiz quiz;
    private String studentId;
    private String courseId;
    private String lessonId;
    private int currentQuestionIndex = 0;
    private Map<String, Integer> selectedAnswers = new HashMap<>();

    // UI Components
    private JPanel questionPanel;
    private JLabel questionNumberLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton previousButton;
    private JButton nextButton;
    private JButton submitButton;

    private QuizService quizService;

    public QuizTakingDialog(JFrame parent, Quiz quiz, String studentId, String courseId, String lessonId) {
        super(parent, "Taking Quiz: " + quiz.getTitle(), true);
        this.quiz = quiz;
        this.studentId = studentId;
        this.courseId = courseId;
        this.lessonId = lessonId;

        // ✅ INTEGRATION: Use Member 4's QuizService
        this.quizService = new QuizService();

        int remainingAttempts = quizService.getRemainingAttempts(studentId, quiz.getQuizId());
        if (remainingAttempts == 0) {
            JOptionPane.showMessageDialog(this,
                    "You have no remaining attempts for this quiz.",
                    "No Attempts Left", JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }

        if (quizService.hasPassedQuiz(studentId, quiz.getQuizId())) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "You have already passed this quiz.\nDo you want to retake it anyway?",
                    "Already Passed", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                dispose();
                return;
            }
        }

        initializeUI();
    }

    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createQuizInfoPanel(), BorderLayout.NORTH);
        add(createQuestionPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        loadQuestion(0);
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private JPanel createQuizInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(59, 89, 182));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(59, 89, 182));

        JLabel titleLabel = new JLabel(quiz.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        int remainingAttempts = quizService.getRemainingAttempts(studentId, quiz.getQuizId());
        String attemptsText = remainingAttempts == -1 ? "Unlimited" : String.valueOf(remainingAttempts);

        JLabel infoLabel = new JLabel(String.format(
                "Total Questions: %d | Pass Mark: %d%% | Attempts Left: %s",
                quiz.getQuestionCount(), quiz.getPassingScore(), attemptsText
        ));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(220, 220, 220));

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(infoLabel);
        panel.add(leftPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createQuestionPanel() {
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Question number label
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionNumberLabel.setForeground(new Color(59, 89, 182));
        questionNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Question text label
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create 4 radio buttons for options
        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        questionPanel.add(questionNumberLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        questionPanel.add(questionLabel);
        questionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add option radio buttons
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionButtons[i].setBackground(Color.WHITE);
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            optionsGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
            questionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(questionPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(240, 242, 245));
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Left side: Previous button
        previousButton = new JButton("← Previous");
        previousButton.setFont(new Font("Arial", Font.BOLD, 12));
        previousButton.setFocusPainted(false);
        previousButton.setEnabled(false);
        previousButton.addActionListener(e -> previousQuestion());
        styleButton(previousButton, new Color(100, 100, 100), Color.WHITE);

        // Right side: Next and Submit buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(240, 242, 245));

        nextButton = new JButton("Next →");
        nextButton.setFont(new Font("Arial", Font.BOLD, 12));
        nextButton.setFocusPainted(false);
        nextButton.addActionListener(e -> nextQuestion());
        styleButton(nextButton, new Color(59, 89, 182), Color.WHITE);

        submitButton = new JButton("Submit Quiz");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setFocusPainted(false);
        submitButton.setVisible(false);
        submitButton.addActionListener(e -> submitQuiz());
        styleButton(submitButton, new Color(67, 160, 71), Color.WHITE);

        rightPanel.add(nextButton);
        rightPanel.add(submitButton);

        panel.add(previousButton, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void loadQuestion(int index) {
        java.util.List<Question> questions = quiz.getQuestions();
        if (questions == null || index < 0 || index >= questions.size()) return;

        currentQuestionIndex = index;
        Question question = questions.get(index);

        // Update question number
        questionNumberLabel.setText(String.format("Question %d of %d", index + 1, questions.size()));

        // Update question text
        questionLabel.setText("<html><body style='width: 650px'>" + question.getQuestionText() + "</body></html>");

        // Update options
        String[] options = question.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            if (options != null && i < options.length && options[i] != null) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setVisible(true);
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        // Restore previously selected answer
        if (selectedAnswers.containsKey(question.getQuestionId())) {
            int selectedIndex = selectedAnswers.get(question.getQuestionId());
            if (selectedIndex >= 0 && selectedIndex < optionButtons.length) {
                optionButtons[selectedIndex].setSelected(true);
            }
        } else {
            optionsGroup.clearSelection();
        }

        updateNavigationButtons();
    }

    private void saveCurrentAnswer() {
        java.util.List<Question> questions = quiz.getQuestions();
        if (questions == null || currentQuestionIndex >= questions.size()) return;

        Question question = questions.get(currentQuestionIndex);
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selectedAnswers.put(question.getQuestionId(), i);
                return;
            }
        }
    }

    private void nextQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex < quiz.getQuestionCount() - 1) {
            loadQuestion(currentQuestionIndex + 1);
        }
    }

    private void previousQuestion() {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            loadQuestion(currentQuestionIndex - 1);
        }
    }

    private void updateNavigationButtons() {
        int totalQuestions = quiz.getQuestionCount();
        previousButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setVisible(currentQuestionIndex < totalQuestions - 1);
        submitButton.setVisible(currentQuestionIndex == totalQuestions - 1);
    }

    private void submitQuiz() {
        saveCurrentAnswer();

        // Check if all questions are answered
        if (!canSubmit()) {
            int unanswered = quiz.getQuestionCount() - selectedAnswers.size();
            int confirm = JOptionPane.showConfirmDialog(this,
                    String.format("You have %d unanswered question(s).\nSubmit anyway?", unanswered),
                    "Unanswered Questions", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to submit this quiz?\nYou cannot change your answers after submission.",
                "Confirm Submission", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // ✅ INTEGRATION: Submit using Member 4's QuizService
            QuizAttempt attempt = quizService.submitQuiz(studentId, quiz.getQuizId(), selectedAnswers);

            if (attempt == null) {
                JOptionPane.showMessageDialog(this,
                        "Error submitting quiz. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            showResults(attempt);
            dispose();
        }
    }

    private void showResults(QuizAttempt attempt) {
        String message = String.format(
                "<html><body style='width: 300px'>" +
                        "<h2>Quiz Results</h2>" +
                        "<p><b>Score:</b> %d/%d (%s)</p>" +
                        "<p><b>Status:</b> %s</p>" +
                        "<p><b>Passing Score:</b> %d%%</p>" +
                        "</body></html>",
                attempt.getScore(), attempt.getMaxScore(), attempt.getFormattedPercentage(),
                attempt.isPassed() ? "✅ PASSED" : "❌ FAILED", quiz.getPassingScore()
        );

        int messageType = attempt.isPassed() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
        JOptionPane.showMessageDialog(this, message, "Quiz Completed", messageType);
    }

    private boolean canSubmit() {
        return selectedAnswers.size() == quiz.getQuestionCount();
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}