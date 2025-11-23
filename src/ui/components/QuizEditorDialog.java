package ui.components;

import models.Quiz;
import models.Question;
import services.QuizService;
import database.JsonDatabaseManager;
import models.Course;
import models.Lesson;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizEditorDialog extends JDialog {
    private String lessonId;
    private Quiz quiz;
    private boolean isNewQuiz;

    private JTextField titleField;
    private JSpinner passingScoreSpinner;
    private JSpinner maxAttemptsSpinner;
    private JSpinner timeLimitSpinner;

    private DefaultListModel<String> questionsListModel;
    private JList<String> questionsList;
    private List<Question> questions;

    private JLabel totalQuestionsLabel;
    private JLabel totalPointsLabel;

    private QuizService quizService;
    private JsonDatabaseManager db;

    public QuizEditorDialog(JFrame parent, String lessonId, Quiz existingQuiz) {
        super(parent, existingQuiz == null ? "Create Quiz" : "Edit Quiz", true);
        this.lessonId = lessonId;
        this.quiz = existingQuiz;
        this.isNewQuiz = (existingQuiz == null);
        this.questions = new ArrayList<>();
        this.quizService = new QuizService();
        this.db = JsonDatabaseManager.getInstance();

        if (!isNewQuiz && quiz.getQuestions() != null) {
            questions.addAll(quiz.getQuestions());
        }

        initializeUI();

        if (!isNewQuiz) {
            loadQuizData();
        }

        setSize(700, 600);
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Settings panel
        mainPanel.add(createQuizSettingsPanel(), BorderLayout.NORTH);

        // Questions panel
        mainPanel.add(createQuestionsPanel(), BorderLayout.CENTER);

        // Summary panel
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Buttons
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createQuizSettingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Quiz Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Quiz Title:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        titleField = new JTextField(30);
        panel.add(titleField, gbc);

        gbc.gridwidth = 1;

        // Passing score
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Passing Score (%):"), gbc);

        gbc.gridx = 1;
        passingScoreSpinner = new JSpinner(new SpinnerNumberModel(70, 0, 100, 5));
        passingScoreSpinner.setPreferredSize(new Dimension(70, 25));
        panel.add(passingScoreSpinner, gbc);

        // Max attempts
        gbc.gridx = 2;
        panel.add(new JLabel("Max Attempts:"), gbc);

        gbc.gridx = 3;
        maxAttemptsSpinner = new JSpinner(new SpinnerNumberModel(3, -1, 10, 1));
        maxAttemptsSpinner.setPreferredSize(new Dimension(70, 25));
        panel.add(maxAttemptsSpinner, gbc);

        // Time limit
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Time Limit (min):"), gbc);

        gbc.gridx = 1;
        timeLimitSpinner = new JSpinner(new SpinnerNumberModel(-1, -1, 180, 5));
        timeLimitSpinner.setPreferredSize(new Dimension(70, 25));
        panel.add(timeLimitSpinner, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        JLabel helpLabel = new JLabel("(-1 for unlimited)");
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        helpLabel.setForeground(Color.GRAY);
        panel.add(helpLabel, gbc);

        return panel;
    }

    private JPanel createQuestionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Questions"));

        // Questions list
        questionsListModel = new DefaultListModel<>();
        questionsList = new JList<>(questionsListModel);
        questionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionsList.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(questionsList);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add Question");
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> addQuestion());

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editQuestion());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteQuestion());

        JButton moveUpButton = new JButton("Move Up");
        moveUpButton.addActionListener(e -> moveQuestionUp());

        JButton moveDownButton = new JButton("Move Down");
        moveDownButton.addActionListener(e -> moveQuestionDown());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        buttonsPanel.add(moveUpButton);
        buttonsPanel.add(moveDownButton);

        panel.add(buttonsPanel, BorderLayout.SOUTH);

        updateQuestionsList();

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Summary"));

        totalQuestionsLabel = new JLabel("Total Questions: 0");
        totalQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 13));

        totalPointsLabel = new JLabel("Total Points: 0");
        totalPointsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        totalPointsLabel.setForeground(new Color(76, 175, 80));

        panel.add(totalQuestionsLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(totalPointsLabel);

        updateSummary();

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton saveButton = new JButton("Save Quiz");
        saveButton.setBackground(new Color(33, 150, 243));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.addActionListener(e -> saveQuiz());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private void loadQuizData() {
        if (quiz == null) return;

        titleField.setText(quiz.getTitle());
        passingScoreSpinner.setValue(quiz.getPassingScore());
        maxAttemptsSpinner.setValue(quiz.getMaxAttempts());
        timeLimitSpinner.setValue(quiz.getTimeLimit());

        updateQuestionsList();
    }

    private void addQuestion() {
        QuestionEditorDialog dialog = new QuestionEditorDialog(this, null);
        dialog.setVisible(true);

        Question newQuestion = dialog.getQuestion();
        if (newQuestion != null) {
            questions.add(newQuestion);
            updateQuestionsList();
            updateSummary();
        }
    }

    private void editQuestion() {
        int selectedIndex = questionsList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a question to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Question selectedQuestion = questions.get(selectedIndex);
        QuestionEditorDialog dialog = new QuestionEditorDialog(this, selectedQuestion);
        dialog.setVisible(true);

        Question editedQuestion = dialog.getQuestion();
        if (editedQuestion != null) {
            questions.set(selectedIndex, editedQuestion);
            updateQuestionsList();
            updateSummary();
        }
    }

    private void deleteQuestion() {
        int selectedIndex = questionsList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a question to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            questions.remove(selectedIndex);
            updateQuestionsList();
            updateSummary();
        }
    }

    private void moveQuestionUp() {
        int selectedIndex = questionsList.getSelectedIndex();
        if (selectedIndex <= 0) return;

        Question temp = questions.get(selectedIndex);
        questions.set(selectedIndex, questions.get(selectedIndex - 1));
        questions.set(selectedIndex - 1, temp);

        updateQuestionsList();
        questionsList.setSelectedIndex(selectedIndex - 1);
    }

    private void moveQuestionDown() {
        int selectedIndex = questionsList.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= questions.size() - 1) return;

        Question temp = questions.get(selectedIndex);
        questions.set(selectedIndex, questions.get(selectedIndex + 1));
        questions.set(selectedIndex + 1, temp);

        updateQuestionsList();
        questionsList.setSelectedIndex(selectedIndex + 1);
    }

    private void updateQuestionsList() {
        questionsListModel.clear();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            String display = String.format("%d. %s (%d pts)",
                    i + 1,
                    q.getQuestionText(),
                    q.getPoints());
            questionsListModel.addElement(display);
        }
    }

    private void updateSummary() {
        int totalQuestions = questions.size();
        int totalPoints = 0;

        for (Question q : questions) {
            totalPoints += q.getPoints();
        }

        totalQuestionsLabel.setText("Total Questions: " + totalQuestions);
        totalPointsLabel.setText("Total Points: " + totalPoints);
    }

    private boolean validateQuiz() {
        // Check title
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a quiz title.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check at least one question
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add at least one question to the quiz.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveQuiz() {
        if (!validateQuiz()) {
            return;
        }

        try {
            // Create or update quiz
            if (isNewQuiz) {
                quiz = new Quiz();
            }

            quiz.setLessonId(lessonId);
            quiz.setTitle(titleField.getText().trim());
            quiz.setPassingScore((Integer) passingScoreSpinner.getValue());
            quiz.setMaxAttempts((Integer) maxAttemptsSpinner.getValue());
            quiz.setTimeLimit((Integer) timeLimitSpinner.getValue());
            quiz.setQuestions(questions);

            boolean success;
            if (isNewQuiz) {
                success = quizService.createQuiz(lessonId, quiz);
            } else {
                success = quizService.updateQuiz(quiz);

                // Also update lesson's quiz reference
                updateLessonQuizReference();
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Quiz saved successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save quiz.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "An error occurred: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLessonQuizReference() {
        // Find and update the lesson
        List<Course> allCourses = db.getAllCourses();
        for (Course course : allCourses) {
            if (course.getLessons() != null) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        lesson.setQuizId(quiz.getQuizId());
                        db.updateCourse(course);
                        return;
                    }
                }
            }
        }
    }
}