package ui.components;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import models.*;
import services.CertificateService;
import services.QuizService;
// Remove duplicate import: import javax.swing.*;

public class QuizResultPanel extends JPanel {
    private final QuizAttempt attempt;
    private final Quiz quiz;
    private final String studentId;

    private final QuizService quizService;
    private final CertificateService certificateService;

    public QuizResultPanel(QuizAttempt attempt, Quiz quiz, String studentId) {
        this.attempt = attempt;
        this.quiz = quiz;
        this.studentId = studentId;

        this.quizService = new QuizService();
        this.certificateService = new CertificateService();

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createScorePanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createFeedbackPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createAnswersReviewPanel());
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createRetryPanel());

        if (attempt.isPassed() && attempt.getCourseId() != null) {
            checkCertificateEligibility();
        }
    }

    private JPanel createScorePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(attempt.isPassed() ? new Color(67, 160, 71) : new Color(244, 67, 54));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel statusLabel = new JLabel(attempt.isPassed() ? "✅ PASSED!" : "❌ FAILED");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 32));
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel(String.format("%d / %d", attempt.getScore(), attempt.getMaxScore()));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel percentageLabel = new JLabel(attempt.getFormattedPercentage());
        percentageLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        percentageLabel.setForeground(new Color(255, 255, 255, 200));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(statusLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(percentageLabel);

        return panel;
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Quiz Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 89, 182));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passingLabel = new JLabel(String.format("Passing Score: %d%%", quiz.getPassingScore()));
        passingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel yourScoreLabel = new JLabel(String.format("Your Score: %.1f%%", attempt.getPercentage()));
        yourScoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        yourScoreLabel.setForeground(attempt.isPassed() ? new Color(67, 160, 71) : new Color(244, 67, 54));
        yourScoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("Date: " + attempt.getAttemptDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String feedbackText = getFeedbackMessage();
        JLabel feedbackLabel = new JLabel("<html><body style='width: 600px'>" + feedbackText + "</body></html>");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(yourScoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(dateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(feedbackLabel);

        return panel;
    }

    private JPanel createAnswersReviewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Review Your Answers");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 89, 182));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        java.util.List<Question> questions = quiz.getQuestions();
        Map<String, Integer> studentAnswers = attempt.getAnswers();

        if (questions != null) {
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                Integer selectedIndex = studentAnswers != null ? studentAnswers.get(question.getQuestionId()) : null;
                panel.add(createQuestionReviewCard(question, i + 1, selectedIndex));
                panel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createQuestionReviewCard(Question question, int questionNumber, Integer selectedIndex) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Check if correct
        boolean isCorrect = selectedIndex != null && selectedIndex == question.getCorrectOptionIndex();

        // Question number and status
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel numberLabel = new JLabel("Question " + questionNumber);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        numberLabel.setForeground(new Color(59, 89, 182));

        JLabel statusIcon = new JLabel(isCorrect ? "✅" : "❌");
        statusIcon.setFont(new Font("Arial", Font.PLAIN, 16));

        headerPanel.add(numberLabel);
        headerPanel.add(statusIcon);

        // Question text
        JLabel questionLabel = new JLabel("<html><body style='width: 600px'>" + question.getQuestionText() + "</body></html>");
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Your answer
        JLabel yourAnswerLabel;
        if (selectedIndex != null && selectedIndex >= 0 && question.getOptions() != null && selectedIndex < question.getOptions().length) {
            String yourAnswer = question.getOptions()[selectedIndex];
            yourAnswerLabel = new JLabel("Your answer: " + yourAnswer);
            yourAnswerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            yourAnswerLabel.setForeground(isCorrect ? new Color(67, 160, 71) : new Color(244, 67, 54));
        } else {
            yourAnswerLabel = new JLabel("Your answer: (Not answered)");
            yourAnswerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            yourAnswerLabel.setForeground(Color.GRAY);
        }
        yourAnswerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Correct answer (if wrong)
        JLabel correctAnswerLabel = null;
        if (!isCorrect) {
            String correctAnswer = question.getCorrectAnswer();
            correctAnswerLabel = new JLabel("Correct answer: " + correctAnswer);
            correctAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            correctAnswerLabel.setForeground(new Color(67, 160, 71));
            correctAnswerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(questionLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(yourAnswerLabel);
        if (correctAnswerLabel != null) {
            card.add(Box.createRigidArea(new Dimension(0, 5)));
            card.add(correctAnswerLabel);
        }

        return card;
    }

    private JPanel createRetryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(Color.WHITE);
        int remainingAttempts = quizService.getRemainingAttempts(studentId, quiz.getQuizId());

        if (!attempt.isPassed() && (remainingAttempts > 0 || remainingAttempts == -1)) {
            JLabel attemptsLabel = new JLabel(
                    remainingAttempts == -1 ? "Unlimited attempts remaining" : String.format("%d attempt(s) remaining", remainingAttempts)
            );
            attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            attemptsLabel.setForeground(Color.GRAY);
            panel.add(attemptsLabel);
        } else if (!attempt.isPassed() && remainingAttempts == 0) {
            JLabel noAttemptsLabel = new JLabel("No attempts remaining");
            noAttemptsLabel.setFont(new Font("Arial", Font.BOLD, 12));
            noAttemptsLabel.setForeground(new Color(244, 67, 54));
            panel.add(noAttemptsLabel);
        }

        return panel;
    }

    private String getFeedbackMessage() {
        double percentage = attempt.getPercentage();

        if (attempt.isPassed()) {
            if (percentage >= 90) {
                return " <b>Excellent work!</b> You've mastered this material. Your understanding is outstanding!";
            } else if (percentage >= 80) {
                return "<b>Great job!</b> You have a strong grasp of the material. Keep up the good work!";
            } else {
                return " <b>Well done!</b> You passed the quiz. Review the questions you missed to strengthen your knowledge.";
            }
        } else {
            int remainingAttempts = quizService.getRemainingAttempts(studentId, quiz.getQuizId());
            if (remainingAttempts > 0 || remainingAttempts == -1) {
                return " <b>Keep trying!</b> Review the material and the questions you missed. You can retake this quiz to improve your score.";
            } else {
                return " <b>No attempts remaining.</b> Please review the lesson content and contact your instructor if you need assistance.";
            }
        }
    }

    private void checkCertificateEligibility() {
        if (attempt.getCourseId() != null) {
            boolean eligible = certificateService.isEligibleForCertificate(studentId, attempt.getCourseId());
            if (eligible) {
                JOptionPane.showMessageDialog(this,
                        "Congratulations! You are now eligible for a course certificate!\n" +
                                "Complete all course requirements to generate your certificate.",
                        "Certificate Eligibility", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public JButton getRetryButton() {
        int remainingAttempts = quizService.getRemainingAttempts(studentId, quiz.getQuizId());

        if (!attempt.isPassed() && (remainingAttempts > 0 || remainingAttempts == -1)) {
            JButton retryButton = new JButton("Retry Quiz");
            retryButton.setFont(new Font("Arial", Font.BOLD, 12));
            retryButton.setBackground(new Color(255, 152, 0));
            retryButton.setForeground(Color.BLACK);
            retryButton.setFocusPainted(false);
            retryButton.setBorderPainted(false);
            retryButton.setPreferredSize(new Dimension(120, 35));
            retryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return retryButton;
        }
        return null;
    }
}