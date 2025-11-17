package ui.components;

import models.Lesson;
import database.JsonDatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class LessonEditorDialog extends JDialog {
    private String courseId;
    private Lesson lesson;
    private JsonDatabaseManager dbManager;

    private JTextField lessonIdField;
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextArea resourcesArea;

    public LessonEditorDialog(JFrame parent, String courseId, Lesson lesson) {
        super(parent, lesson == null ? "Add Lesson" : "Edit Lesson", true);
        this.courseId = courseId;
        this.lesson = lesson;
        this.dbManager = JsonDatabaseManager.getInstance();

        setLayout(new BorderLayout(10, 10));
        setSize(500, 500);
        setLocationRelativeTo(parent);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Lesson ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Lesson ID:"), gbc);

        gbc.gridx = 1;
        lessonIdField = new JTextField(20);
        if (lesson != null) {
            lessonIdField.setText(lesson.getLessonId());
            lessonIdField.setEditable(false);
        } else {
            lessonIdField.setText("L" + UUID.randomUUID().toString().substring(0, 8));
        }
        formPanel.add(lessonIdField, gbc);

        // Title
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        if (lesson != null) {
            titleField.setText(lesson.getTitle());
        }
        formPanel.add(titleField, gbc);

        // Content
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Content:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        contentArea = new JTextArea(10, 20);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        if (lesson != null) {
            contentArea.setText(lesson.getContent());
        }
        JScrollPane contentScroll = new JScrollPane(contentArea);
        formPanel.add(contentScroll, gbc);

        // Resources
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weighty = 0.5;
        formPanel.add(new JLabel("Resources (one per line):"), gbc);

        gbc.gridx = 1;
        resourcesArea = new JTextArea(5, 20);
        resourcesArea.setLineWrap(true);
        if (lesson != null && lesson.getResources() != null) {
            resourcesArea.setText(String.join("\n", lesson.getResources()));
        }
        JScrollPane resourcesScroll = new JScrollPane(resourcesArea);
        formPanel.add(resourcesScroll, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveLesson());
        cancelButton.addActionListener(e -> dispose());

        buttonsPanel.add(saveButton);
        buttonsPanel.add(cancelButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void saveLesson() {
        // Validate inputs
        String lessonId = lessonIdField.getText().trim();
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (lessonId.isEmpty() || title.isEmpty() || content.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Parse resources
        String resourcesText = resourcesArea.getText().trim();
        String[] resources = resourcesText.isEmpty()
                ? new String[0]
                : resourcesText.split("\n");

        // Create or update lesson
        Lesson newLesson = new Lesson(lessonId, title, content, resources);

        boolean success;
        if (lesson == null) {
            // Adding new lesson
            success = dbManager.addLessonToCourse(courseId, newLesson);
        } else {
            // Updating existing lesson
            success = dbManager.updateLesson(courseId, newLesson);
        }

        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lesson saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save lesson. Lesson ID may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}