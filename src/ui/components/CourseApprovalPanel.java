package ui.components;

import models.Course;
import models.User;
import database.JsonDatabaseManager;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CourseApprovalPanel extends JPanel {
    private Course course;
    private JButton approveButton;
    private JButton rejectButton;
    private JsonDatabaseManager dbManager;

    // Constructor
    public CourseApprovalPanel(Course course, ActionListener approveAction, ActionListener rejectAction) {
        this.course = course;
        this.dbManager = JsonDatabaseManager.getInstance();

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Add course info panel
        add(createCourseInfoPanel(), BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        approveButton = new JButton("✓ Approve");
        approveButton.setFont(new Font("Arial", Font.BOLD, 12));
        approveButton.setBackground(new Color(67, 160, 71));
        approveButton.setForeground(Color.WHITE);
        approveButton.setFocusPainted(false);
        approveButton.setBorderPainted(false);
        approveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        approveButton.setMaximumSize(new Dimension(100, 30));
        approveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        approveButton.addActionListener(approveAction);

        rejectButton = new JButton("✗ Reject");
        rejectButton.setFont(new Font("Arial", Font.BOLD, 12));
        rejectButton.setBackground(new Color(244, 67, 54));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFocusPainted(false);
        rejectButton.setBorderPainted(false);
        rejectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rejectButton.setMaximumSize(new Dimension(100, 30));
        rejectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectButton.addActionListener(rejectAction);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(approveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(rejectButton);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.EAST);
    }

    // Create the course information panel
    public JPanel createCourseInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(59, 89, 182));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><body style='width: 400px'>" +
                course.getDescription() + "</body></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String instructorName = getInstructorName(course.getInstructorId());
        JLabel instructorLabel = new JLabel("👤 Instructor: " + instructorName);
        instructorLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructorLabel.setForeground(Color.GRAY);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        int lessonCount = course.getLessons() != null ? course.getLessons().length : 0;
        JLabel lessonCountLabel = new JLabel("📚 " + lessonCount + " lessons");
        lessonCountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lessonCountLabel.setForeground(Color.GRAY);
        lessonCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel courseIdLabel = new JLabel("ID: " + course.getCourseId());
        courseIdLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        courseIdLabel.setForeground(Color.LIGHT_GRAY);
        courseIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(instructorLabel);
        infoPanel.add(lessonCountLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(courseIdLabel);

        return infoPanel;
    }

    // Get instructor name from ID
    private String getInstructorName(String instructorId) {
        User user = dbManager.getUserById(instructorId);
        if (user != null) {
            return user.getUsername();
        }
        return instructorId;
    }

    // Getter for approve button
    public JButton getApproveButton() {
        return approveButton;
    }

    // Getter for reject button
    public JButton getRejectButton() {
        return rejectButton;
    }

    // Getter for course
    public Course getCourse() {
        return course;
    }
}