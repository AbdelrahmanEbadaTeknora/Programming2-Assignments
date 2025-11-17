package ui.components;

import models.Lesson;
import database.JsonDatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LessonListDialog extends JDialog {
    private String courseId;
    private DefaultListModel<String> lessonListModel;
    private JList<String> lessonList;
    private JsonDatabaseManager dbManager;

    public LessonListDialog(JFrame parent, String courseId, String courseTitle) {
        super(parent, "Manage Lessons: " + courseTitle, true);
        this.courseId = courseId;
        this.dbManager = JsonDatabaseManager.getInstance();

        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Lesson list
        lessonListModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonListModel);
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadLessons();

        JScrollPane scrollPane = new JScrollPane(lessonList);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add Lesson");
        JButton editButton = new JButton("Edit Lesson");
        JButton deleteButton = new JButton("Delete Lesson");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> addLesson());
        editButton.addActionListener(e -> editLesson());
        deleteButton.addActionListener(e -> deleteLesson());
        closeButton.addActionListener(e -> dispose());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadLessons() {
        lessonListModel.clear();
        List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
        for (Lesson lesson : lessons) {
            lessonListModel.addElement(lesson.getLessonId() + " - " + lesson.getTitle());
        }
    }

    private void addLesson() {
        LessonEditorDialog dialog = new LessonEditorDialog(
                (JFrame) getOwner(),
                courseId,
                null
        );
        dialog.setVisible(true);
        loadLessons();
    }

    private void editLesson() {
        int selectedIndex = lessonList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to edit.");
            return;
        }

        List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
        Lesson selectedLesson = lessons.get(selectedIndex);

        LessonEditorDialog dialog = new LessonEditorDialog(
                (JFrame) getOwner(),
                courseId,
                selectedLesson
        );
        dialog.setVisible(true);
        loadLessons();
    }

    private void deleteLesson() {
        int selectedIndex = lessonList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this lesson?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
            Lesson selectedLesson = lessons.get(selectedIndex);

            if (dbManager.deleteLessonFromCourse(courseId, selectedLesson.getLessonId())) {
                JOptionPane.showMessageDialog(this, "Lesson deleted successfully!");
                loadLessons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete lesson.");
            }
        }
    }
}