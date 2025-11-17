package ui.components;

import models.Lesson;
import javax.swing.*;
import java.awt.*;

public class LessonPanel extends JPanel {
    private Lesson lesson;
    private JTextArea contentArea;
    private JList<String> resourcesList;
    private JButton completeButton;

    public LessonPanel(Lesson lesson, boolean isStudent) {
        this.lesson = lesson;
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

        // Complete button (only for students)
        if (isStudent) {
            completeButton = new JButton("Mark as Completed");
            completeButton.setBackground(new Color(76, 175, 80));
            completeButton.setForeground(Color.WHITE);
            completeButton.setFocusPainted(false);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(completeButton);
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JButton getCompleteButton() {
        return completeButton;
    }

    public Lesson getLesson() {
        return lesson;
    }
}