package ui.components;

import models.Lesson;
import models.Quiz;
import services.QuizService;
import database.JsonDatabaseManager;

import javax.swing.*;
import java.awt.*;

public class LessonPanel extends JPanel {
    private Lesson lesson;
    private JTextArea contentArea;
    private JList<String> resourcesList;
    private JButton completeButton;
    private JButton takeQuizButton;
    private QuizService quizService;
    private JsonDatabaseManager dbManager;

    public LessonPanel(Lesson lesson, boolean isStudent, String studentId, String courseId) {
        this.lesson = lesson;
        this.quizService = new QuizService();
        this.dbManager = JsonDatabaseManager.getInstance();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Content
        contentArea = new JTextArea(lesson.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setPreferredSize(new Dimension(600, 300));
        add(contentScroll, BorderLayout.CENTER);

        // Bottom panel with resources and actions
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Resources
        if (lesson.getResources() != null && lesson.getResources().length > 0) {
            JPanel resourcesPanel = new JPanel(new BorderLayout());
            resourcesPanel.setBorder(BorderFactory.createTitledBorder("Resources"));

            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String resource : lesson.getResources()) {
                listModel.addElement(resource);
            }
            resourcesList = new JList<>(listModel);
            JScrollPane resourcesScroll = new JScrollPane(resourcesList);
            resourcesScroll.setPreferredSize(new Dimension(600, 80));
            resourcesPanel.add(resourcesScroll);

            bottomPanel.add(resourcesPanel, BorderLayout.CENTER);
        }

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        if (isStudent) {
            // Complete button
            completeButton = new JButton("Mark as Completed");
            completeButton.setBackground(new Color(76, 175, 80));
            completeButton.setForeground(Color.WHITE);
            completeButton.setFocusPainted(false);
            actionPanel.add(completeButton);

            // Take Quiz button (if quiz exists)
            if (lesson.getQuizId() != null && !lesson.getQuizId().isEmpty()) {
                Quiz quiz = quizService.getQuizById(lesson.getQuizId());
                if (quiz != null) {
                    takeQuizButton = new JButton("Take Quiz");
                    takeQuizButton.setBackground(new Color(33, 150, 243));
                    takeQuizButton.setForeground(Color.WHITE);
                    takeQuizButton.setFocusPainted(false);

                    // Check if student has already passed this quiz
                    if (quizService.hasPassedQuiz(studentId, lesson.getQuizId())) {
                        takeQuizButton.setText("Quiz Passed ✓");
                        takeQuizButton.setEnabled(false);
                        takeQuizButton.setBackground(new Color(200, 200, 200));
                    }

                    actionPanel.add(takeQuizButton);
                }
            }
        }

        bottomPanel.add(actionPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Constructor overload for backward compatibility
    public LessonPanel(Lesson lesson, boolean isStudent) {
        this(lesson, isStudent, null, null);
    }

    public JButton getCompleteButton() {
        return completeButton;
    }

    public JButton getTakeQuizButton() {
        return takeQuizButton;
    }

    public Lesson getLesson() {
        return lesson;
    }
}