package ui.components;

import models.Lesson;
import models.Quiz;
import services.QuizService;
import database.JsonDatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LessonListDialog extends JDialog {
    private String courseId;
    private DefaultListModel<String> lessonListModel;
    private JList<String> lessonList;
    private JsonDatabaseManager dbManager;
    private QuizService quizService;

    public LessonListDialog(JFrame parent, String courseId, String courseTitle) {
        super(parent, "Manage Lessons: " + courseTitle, true);
        this.courseId = courseId;
        this.dbManager = JsonDatabaseManager.getInstance();
        this.quizService = new QuizService();

        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
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
        JButton manageQuizButton = new JButton("Manage Quiz");
        JButton closeButton = new JButton("Close");

        addButton.addActionListener(e -> addLesson());
        editButton.addActionListener(e -> editLesson());
        deleteButton.addActionListener(e -> deleteLesson());
        manageQuizButton.addActionListener(e -> manageQuiz());
        closeButton.addActionListener(e -> dispose());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(manageQuizButton);
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadLessons() {
        lessonListModel.clear();
        List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
        for (Lesson lesson : lessons) {
            String quizInfo = "";
            if (lesson.getQuizId() != null && !lesson.getQuizId().isEmpty()) {
                Quiz quiz = quizService.getQuizById(lesson.getQuizId());
                if (quiz != null) {
                    quizInfo = " [Quiz: " + quiz.getTitle() + "]";
                }
            }
            lessonListModel.addElement(lesson.getLessonId() + " - " + lesson.getTitle() + quizInfo);
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

    private void manageQuiz() {
        int selectedIndex = lessonList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to manage its quiz.");
            return;
        }

        List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
        Lesson selectedLesson = lessons.get(selectedIndex);

        // Check if lesson already has a quiz
        Quiz existingQuiz = null;
        if (selectedLesson.getQuizId() != null && !selectedLesson.getQuizId().isEmpty()) {
            existingQuiz = quizService.getQuizById(selectedLesson.getQuizId());
        }

        QuizEditorDialog dialog = new QuizEditorDialog(
                (JFrame) getOwner(),
                selectedLesson.getLessonId(),
                existingQuiz
        );
        dialog.setVisible(true);
        loadLessons();
    }
}