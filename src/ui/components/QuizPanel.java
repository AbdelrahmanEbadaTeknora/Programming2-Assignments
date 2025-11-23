package ui.components;

import models.Quiz;
import models.Question;

import javax.swing.*;
import java.awt.*;

public class QuizPanel extends JPanel {
    private Quiz quiz;
    private boolean isEditable;
    private JButton editButton;
    private JButton deleteButton;

    public QuizPanel(Quiz quiz, boolean isEditable) {
        this.quiz = quiz;
        this.isEditable = isEditable;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setBackground(Color.WHITE);

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel(quiz.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(63, 81, 181));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = displayQuizInfo();
        add(infoPanel, BorderLayout.CENTER);

        // Buttons panel (if editable)
        if (isEditable) {
            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonsPanel.setOpaque(false);

            editButton = new JButton("Edit Quiz");
            editButton.setBackground(new Color(33, 150, 243));
            editButton.setForeground(Color.BLACK);
            editButton.setFocusPainted(false);

            deleteButton = new JButton("Delete Quiz");
            deleteButton.setBackground(new Color(244, 67, 54));
            deleteButton.setForeground(Color.BLACK);
            deleteButton.setFocusPainted(false);

            buttonsPanel.add(editButton);
            buttonsPanel.add(deleteButton);
            add(buttonsPanel, BorderLayout.SOUTH);
        }
    }

    private JPanel displayQuizInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Passing score
        JLabel passingLabel = new JLabel("✓ Passing Score: " + quiz.getPassingScore() + "%");
        passingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passingLabel);
        panel.add(Box.createVerticalStrut(5));

        // Max attempts
        String attemptsText = quiz.getMaxAttempts() > 0
                ? quiz.getMaxAttempts() + " attempts"
                : "Unlimited attempts";
        JLabel attemptsLabel = new JLabel("✓ Max Attempts: " + attemptsText);
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(attemptsLabel);
        panel.add(Box.createVerticalStrut(5));

        // Time limit
        String timeText = quiz.getTimeLimit() > 0
                ? quiz.getTimeLimit() + " minutes"
                : "No time limit";
        JLabel timeLabel = new JLabel("✓ Time Limit: " + timeText);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(timeLabel);
        panel.add(Box.createVerticalStrut(5));

        // Questions count
        JLabel questionsLabel = new JLabel("✓ Questions: " + quiz.getQuestionCount());
        questionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(questionsLabel);
        panel.add(Box.createVerticalStrut(5));

        // Total points
        JLabel pointsLabel = new JLabel("✓ Total Points: " + quiz.getTotalPoints());
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pointsLabel.setForeground(new Color(76, 175, 80));
        panel.add(pointsLabel);

        // Questions list
        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
            panel.add(Box.createVerticalStrut(10));
            panel.add(new JSeparator());
            panel.add(Box.createVerticalStrut(10));

            JLabel questionsTitle = new JLabel("Questions:");
            questionsTitle.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(questionsTitle);
            panel.add(Box.createVerticalStrut(5));

            displayQuestions(panel);
        }

        return panel;
    }

    private void displayQuestions(JPanel panel) {
        int index = 1;
        for (Question q : quiz.getQuestions()) {
            JLabel questionLabel = new JLabel(
                    String.format("%d. %s (%d pts)", index, q.getQuestionText(), q.getPoints())
            );
            questionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            panel.add(questionLabel);
            panel.add(Box.createVerticalStrut(3));
            index++;
        }
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public Quiz getQuiz() {
        return quiz;
    }
}