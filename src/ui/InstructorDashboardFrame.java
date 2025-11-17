package ui;

import models.Course;
import models.Instructor;
import models.Lesson;
import database.JsonDatabaseManager;
import ui.components.LessonListDialog; // ADD THIS IMPORT

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InstructorDashboardFrame extends JFrame {
    private final Instructor instructor; // Made final
    private final JsonDatabaseManager dbManager; // Made final

    private JTable coursesTable;
    private DefaultTableModel coursesTableModel;
    private JTextArea courseDetailsArea;

    public InstructorDashboardFrame(Instructor instructor) {
        this.instructor = instructor;
        this.dbManager = JsonDatabaseManager.getInstance();

        initializeUI();
        loadInstructorCourses();
    }

    private void initializeUI() {
        setTitle("SkillForge - Instructor Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Welcome and Logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + instructor.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Split between courses list and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);

        // Left - Courses Table
        JPanel coursesPanel = createCoursesPanel();
        splitPane.setLeftComponent(coursesPanel);

        JPanel detailsPanel = createDetailsPanel();
        splitPane.setRightComponent(detailsPanel);

        add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = createActionPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("My Courses"));

        // Table for courses
        String[] columnNames = {"Course ID", "Title", "Students", "Lessons"};
        coursesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        coursesTable = new JTable(coursesTableModel);
        coursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayCourseDetails();
            }
        });

        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Course Details"));

        courseDetailsArea = new JTextArea();
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        courseDetailsArea.setLineWrap(true);
        courseDetailsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(courseDetailsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createCourseButton = new JButton("Create New Course");
        createCourseButton.addActionListener(e -> createNewCourse());

        JButton editCourseButton = new JButton("Edit Course");
        editCourseButton.addActionListener(e -> editSelectedCourse());

        JButton deleteCourseButton = new JButton("Delete Course");
        deleteCourseButton.addActionListener(e -> deleteSelectedCourse());

        JButton viewStudentsButton = new JButton("View Enrolled Students");
        viewStudentsButton.addActionListener(e -> viewEnrolledStudents());

        JButton manageLessonsButton = new JButton("Manage Lessons");
        manageLessonsButton.addActionListener(e -> manageLessons());

        panel.add(createCourseButton);
        panel.add(editCourseButton);
        panel.add(deleteCourseButton);
        panel.add(viewStudentsButton);
        panel.add(manageLessonsButton);

        return panel;
    }

    private void loadInstructorCourses() {
        coursesTableModel.setRowCount(0);

        List<Course> allCourses = dbManager.getAllCourses();
        for (Course course : allCourses) {
            if (course.getInstructorId().equals(instructor.getUserId())) {
                // FIXED: Use correct method names
                int studentCount = course.getStudents() != null ? course.getStudents().size() : 0;
                int lessonCount = course.getLessons() != null ? course.getLessons().length : 0;

                Object[] rowData = {
                        course.getCourseId(),
                        course.getTitle(),
                        studentCount, // FIXED: Use getStudents().size()
                        lessonCount   // FIXED: Use getLessons().length
                };
                coursesTableModel.addRow(rowData);
            }
        }
    }

    private void displayCourseDetails() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            courseDetailsArea.setText("");
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        Course course = dbManager.getCourseById(courseId);

        if (course != null) {
            StringBuilder details = new StringBuilder();
            details.append("Course ID: ").append(course.getCourseId()).append("\n");
            details.append("Title: ").append(course.getTitle()).append("\n");
            details.append("Description: ").append(course.getDescription()).append("\n\n");

            // FIXED: Use correct method names
            int studentCount = course.getStudents() != null ? course.getStudents().size() : 0;
            int lessonCount = course.getLessons() != null ? course.getLessons().length : 0;

            details.append("Enrolled Students: ").append(studentCount).append("\n");
            details.append("Total Lessons: ").append(lessonCount).append("\n\n");

            if (course.getLessons() != null && course.getLessons().length > 0) {
                details.append("Lessons:\n");
                for (int i = 0; i < course.getLessons().length; i++) {
                    Lesson lesson = course.getLessons()[i]; // FIXED: Array access
                    details.append((i + 1)).append(". ").append(lesson.getTitle()).append("\n");
                }
            }

            courseDetailsArea.setText(details.toString());
        }
    }

    private void createNewCourse() {
        JTextField courseIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        Object[] message = {
                "Course ID:", courseIdField,
                "Title:", titleField,
                "Description:", descScroll
        };

        int option = JOptionPane.showConfirmDialog(
                this, message, "Create New Course", JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            String courseId = courseIdField.getText().trim();
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (validateCourseInput(courseId, title, description)) {
                // Check for duplicate course ID
                if (dbManager.getCourseById(courseId) != null) {
                    JOptionPane.showMessageDialog(
                            this, "Course ID already exists!", "Error", JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                Course newCourse = new Course(courseId, title, description, instructor.getUserId());
                boolean success = dbManager.saveCourse(newCourse);

                if (success) {
                    JOptionPane.showMessageDialog(
                            this, "Course created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE
                    );
                    loadInstructorCourses();
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Failed to create course!", "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private void editSelectedCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a course to edit.", "No Selection", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        Course course = dbManager.getCourseById(courseId);

        if (course != null) {
            JTextField titleField = new JTextField(course.getTitle());
            JTextArea descriptionArea = new JTextArea(course.getDescription(), 5, 20);
            descriptionArea.setLineWrap(true);
            descriptionArea.setWrapStyleWord(true);
            JScrollPane descScroll = new JScrollPane(descriptionArea);

            Object[] message = {
                    "Course ID: " + courseId + " (cannot be changed)",
                    "Title:", titleField,
                    "Description:", descScroll
            };

            int option = JOptionPane.showConfirmDialog(
                    this, message, "Edit Course", JOptionPane.OK_CANCEL_OPTION
            );

            if (option == JOptionPane.OK_OPTION) {
                String newTitle = titleField.getText().trim();
                String newDescription = descriptionArea.getText().trim();

                if (validateCourseInput(courseId, newTitle, newDescription)) {
                    course.setTitle(newTitle);
                    course.setDescription(newDescription);
                    boolean success = dbManager.updateCourse(course);

                    if (success) {
                        JOptionPane.showMessageDialog(
                                this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE
                        );
                        loadInstructorCourses();
                        displayCourseDetails();
                    } else {
                        JOptionPane.showMessageDialog(
                                this, "Failed to update course!", "Error", JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
    }

    private void deleteSelectedCourse() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a course to delete.", "No Selection", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        String title = (String) coursesTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the course '" + title + "'?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dbManager.deleteCourse(courseId);
            if (success) {
                JOptionPane.showMessageDialog(
                        this, "Course deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE
                );
                loadInstructorCourses();
                courseDetailsArea.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this, "Failed to delete course!", "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void viewEnrolledStudents() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a course.", "No Selection", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        Course course = dbManager.getCourseById(courseId);

        if (course != null) {
            // FIXED: Use getStudents() instead of getEnrolledStudentIds()
            List<String> studentIds = course.getStudents();

            if (studentIds.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "No students enrolled in this course yet.", "No Students", JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            StringBuilder studentList = new StringBuilder();
            studentList.append("Enrolled Students (").append(studentIds.size()).append("):\n\n");

            for (String studentId : studentIds) {
                studentList.append("• Student ID: ").append(studentId).append("\n");
            }

            JTextArea textArea = new JTextArea(studentList.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(
                    this, scrollPane, "Enrolled Students - " + course.getTitle(), JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void manageLessons() {
        int selectedRow = coursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a course.", "No Selection", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String courseId = (String) coursesTableModel.getValueAt(selectedRow, 0);
        String courseTitle = (String) coursesTableModel.getValueAt(selectedRow, 1);

        // FIXED: Use Member 4's LessonListDialog component
        LessonListDialog dialog = new LessonListDialog(this, courseId, courseTitle);
        dialog.setVisible(true);

        // Refresh course data after lesson management
        loadInstructorCourses();
        displayCourseDetails();
    }

    private boolean validateCourseInput(String courseId, String title, String description) {
        if (courseId.isEmpty() || title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (courseId.length() < 3) {
            JOptionPane.showMessageDialog(
                    this, "Course ID must be at least 3 characters!", "Validation Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (title.length() < 3) {
            JOptionPane.showMessageDialog(
                    this, "Title must be at least 3 characters!", "Validation Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}