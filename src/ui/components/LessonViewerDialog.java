package ui.components;

import models.Lesson;
import database.JsonDatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LessonViewerDialog extends JDialog {
    private String courseId;
    private String studentId;
    private JsonDatabaseManager dbManager;
    private DefaultListModel<String> lessonListModel;
    private JList<String> lessonList;
    private JPanel lessonDisplayPanel;
    private List<Lesson> lessons;

    public LessonViewerDialog(JFrame parent, String courseId, String courseTitle, String studentId) {
        super(parent, "Lessons: " + courseTitle, true);
        this.courseId = courseId;
        this.studentId = studentId;
        this.dbManager = JsonDatabaseManager.getInstance();

        setLayout(new BorderLayout(10, 10));
        setSize(900, 600);
        setLocationRelativeTo(parent);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left panel - Lesson list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Lessons"));

        lessonListModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonListModel);
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedLesson();
            }
        });

        JScrollPane listScroll = new JScrollPane(lessonList);
        leftPanel.add(listScroll, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // Right panel - Lesson display
        lessonDisplayPanel = new JPanel(new BorderLayout());
        lessonDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel noSelectionLabel = new JLabel("Select a lesson to view", SwingConstants.CENTER);
        noSelectionLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        lessonDisplayPanel.add(noSelectionLabel, BorderLayout.CENTER);

        splitPane.setRightComponent(lessonDisplayPanel);
        splitPane.setDividerLocation(250);

        add(splitPane, BorderLayout.CENTER);

        // Close button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load lessons
        loadLessons();
    }

    private void loadLessons() {
        lessonListModel.clear();
        lessons = dbManager.getLessonsByCourse(courseId);
        List<String> completedLessons = dbManager.getCompletedLessons(studentId, courseId);

        for (Lesson lesson : lessons) {
            String status = completedLessons.contains(lesson.getLessonId()) ? " ✓" : "";
            lessonListModel.addElement(lesson.getTitle() + status);
        }

        if (!lessons.isEmpty()) {
            lessonList.setSelectedIndex(0);
        }
    }

    private void displaySelectedLesson() {
        int selectedIndex = lessonList.getSelectedIndex();
        if (selectedIndex == -1) return;

        Lesson lesson = lessons.get(selectedIndex);
        List<String> completedLessons = dbManager.getCompletedLessons(studentId, courseId);
        boolean isCompleted = completedLessons.contains(lesson.getLessonId());

        // Clear previous display
        lessonDisplayPanel.removeAll();

        // Create lesson panel
        LessonPanel panel = new LessonPanel(lesson, true);

        // Configure complete button
        JButton completeButton = panel.getCompleteButton();
        if (completeButton != null) {
            if (isCompleted) {
                completeButton.setText("Completed ✓");
                completeButton.setEnabled(false);
                completeButton.setBackground(new Color(200, 200, 200));
            } else {
                completeButton.addActionListener(e -> markLessonCompleted(lesson.getLessonId()));
            }
        }

        lessonDisplayPanel.add(panel, BorderLayout.CENTER);
        lessonDisplayPanel.revalidate();
        lessonDisplayPanel.repaint();
    }

    private void markLessonCompleted(String lessonId) {
        if (dbManager.markLessonCompleted(studentId, courseId, lessonId)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lesson marked as completed!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadLessons(); // Refresh list to show checkmark
            displaySelectedLesson(); // Refresh display to disable button
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to mark lesson as completed.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}