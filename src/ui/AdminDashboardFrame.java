package ui;

import models.Admin;
import models.Course;
import models.User;
import database.JsonDatabaseManager;
import services.CourseApprovalService;
import ui.components.CourseApprovalPanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {
    private Admin currentAdmin;
    private JsonDatabaseManager dbManager;
    private CourseApprovalService approvalService;
    private JTabbedPane tabbedPane;

    private static final Color PRIMARY_COLOR = new Color(59, 89, 182);
    private static final Color SECONDARY_COLOR = new Color(240, 242, 245);
    private static final Color SUCCESS_COLOR = new Color(67, 160, 71);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);

    // Constructor
    public AdminDashboardFrame(Admin admin) {
        this.currentAdmin = admin;
        this.dbManager = JsonDatabaseManager.getInstance();
        this.approvalService = new CourseApprovalService();
        initializeUI();
    }

    // Initialize the UI
    private void initializeUI() {
        setTitle("SkillForge - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Add header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("📋 Pending Courses", createPendingCoursesPanel());
        tabbedPane.addTab("📚 All Courses", createAllCoursesPanel());
        tabbedPane.addTab("👥 All Users", createAllUsersPanel());

        add(tabbedPane, BorderLayout.CENTER);

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    // Create header panel
    public JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PRIMARY_COLOR);

        JLabel welcome = new JLabel("Admin Dashboard 👨‍💼");
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(Color.WHITE);

        JLabel adminName = new JLabel("Welcome, " + currentAdmin.getUsername() + "!");
        adminName.setFont(new Font("Arial", Font.PLAIN, 12));
        adminName.setForeground(new Color(220, 220, 220));

        leftPanel.add(welcome);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(adminName);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(DANGER_COLOR);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> logout());

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    // Create pending courses panel
    public JPanel createPendingCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        List<Course> pendingCourses = approvalService.getPendingCourses();
        JLabel title = new JLabel("Pending Course Approvals (" + pendingCourses.size() + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBackground(SECONDARY_COLOR);

        if (pendingCourses.isEmpty()) {
            JPanel emptyState = createEmptyState(
                    "✅",
                    "No Pending Courses",
                    "All courses have been reviewed! Check back later for new submissions."
            );
            courseListPanel.add(emptyState);
        } else {
            for (Course course : pendingCourses) {
                CourseApprovalPanel approvalPanel = new CourseApprovalPanel(
                        course,
                        e -> approveCourse(course.getCourseId()),
                        e -> rejectCourse(course.getCourseId())
                );
                courseListPanel.add(approvalPanel);
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(courseListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create all courses panel
    public JPanel createAllCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        List<Course> approvedCourses = approvalService.getApprovedCourses();
        List<Course> rejectedCourses = approvalService.getRejectedCourses();
        List<Course> pendingCourses = approvalService.getPendingCourses();

        int totalCourses = approvedCourses.size() + rejectedCourses.size() + pendingCourses.size();

        JLabel title = new JLabel("All Courses (" + totalCourses + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        // Create statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(SECONDARY_COLOR);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        statsPanel.add(createStatCard("Approved", String.valueOf(approvedCourses.size()), SUCCESS_COLOR));
        statsPanel.add(createStatCard("Pending", String.valueOf(pendingCourses.size()), new Color(255, 152, 0)));
        statsPanel.add(createStatCard("Rejected", String.valueOf(rejectedCourses.size()), DANGER_COLOR));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Create courses list
        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBackground(SECONDARY_COLOR);

        // Add approved courses
        if (!approvedCourses.isEmpty()) {
            JLabel approvedLabel = new JLabel("✓ Approved Courses");
            approvedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            approvedLabel.setForeground(SUCCESS_COLOR);
            approvedLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            courseListPanel.add(approvedLabel);

            for (Course course : approvedCourses) {
                courseListPanel.add(createCourseCard(course, SUCCESS_COLOR));
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Add pending courses
        if (!pendingCourses.isEmpty()) {
            JLabel pendingLabel = new JLabel("⏳ Pending Courses");
            pendingLabel.setFont(new Font("Arial", Font.BOLD, 14));
            pendingLabel.setForeground(new Color(255, 152, 0));
            pendingLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            courseListPanel.add(pendingLabel);

            for (Course course : pendingCourses) {
                courseListPanel.add(createCourseCard(course, new Color(255, 152, 0)));
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Add rejected courses
        if (!rejectedCourses.isEmpty()) {
            JLabel rejectedLabel = new JLabel("✗ Rejected Courses");
            rejectedLabel.setFont(new Font("Arial", Font.BOLD, 14));
            rejectedLabel.setForeground(DANGER_COLOR);
            rejectedLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            courseListPanel.add(rejectedLabel);

            for (Course course : rejectedCourses) {
                courseListPanel.add(createCourseCard(course, DANGER_COLOR));
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(courseListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create all users panel
    public JPanel createAllUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        List<User> allUsers = dbManager.users().getAllUsers();

        JLabel title = new JLabel("All Users (" + allUsers.size() + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        // Count users by role
        int studentCount = 0;
        int instructorCount = 0;
        int adminCount = 0;

        for (User user : allUsers) {
            switch (user.getRole()) {
                case "student": studentCount++; break;
                case "instructor": instructorCount++; break;
                case "admin": adminCount++; break;
            }
        }

        // Create statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(SECONDARY_COLOR);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        statsPanel.add(createStatCard("Students", String.valueOf(studentCount), new Color(33, 150, 243)));
        statsPanel.add(createStatCard("Instructors", String.valueOf(instructorCount), new Color(156, 39, 176)));
        statsPanel.add(createStatCard("Admins", String.valueOf(adminCount), PRIMARY_COLOR));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Create users list
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(SECONDARY_COLOR);

        if (allUsers.isEmpty()) {
            JPanel emptyState = createEmptyState(
                    "👥",
                    "No Users",
                    "No users have registered yet."
            );
            userListPanel.add(emptyState);
        } else {
            for (User user : allUsers) {
                userListPanel.add(createUserCard(user));
                userListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(userListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create empty state panel
    private JPanel createEmptyState(String emoji, String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(new EmptyBorder(50, 20, 50, 20));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(emojiLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(descLabel);

        return panel;
    }

    // Create stat card
    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // Create course card
    private JPanel createCourseCard(Course course, Color statusColor) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(statusColor, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel statusLabel = new JLabel("Status: " + course.getApprovalStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(statusColor);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String instructorName = getInstructorName(course.getInstructorId());
        JLabel instructorLabel = new JLabel("Instructor: " + instructorName);
        instructorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        instructorLabel.setForeground(Color.GRAY);
        instructorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = new JLabel("ID: " + course.getCourseId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(instructorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(idLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    // Create user card
    private JPanel createUserCard(User user) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(PRIMARY_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("📧 " + user.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.DARK_GRAY);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color roleColor;
        switch (user.getRole()) {
            case "student": roleColor = new Color(33, 150, 243); break;
            case "instructor": roleColor = new Color(156, 39, 176); break;
            case "admin": roleColor = PRIMARY_COLOR; break;
            default: roleColor = Color.GRAY;
        }

        JLabel roleLabel = new JLabel("Role: " + user.getRole().toUpperCase());
        roleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        roleLabel.setForeground(roleColor);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = new JLabel("ID: " + user.getUserId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(roleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(idLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    // Get instructor name from ID
    private String getInstructorName(String instructorId) {
        User user = dbManager.users().getUserById(instructorId);
        if (user != null) {
            return user.getUsername();
        }
        return instructorId;
    }

    // Load pending courses
    public void loadPendingCourses() {
        // This is called automatically when creating the panel
        // Just refresh the panels
        refreshPanels();
    }

    // Approve a course
    public void approveCourse(String courseId) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to approve this course?",
                "Confirm Approval",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = approvalService.approveCourse(courseId);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Course approved successfully! ✓\nThe course is now visible to all students.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshPanels();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to approve course. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Reject a course
    public void rejectCourse(String courseId) {
        String reason = JOptionPane.showInputDialog(
                this,
                "Please provide a reason for rejecting this course:",
                "Reject Course",
                JOptionPane.QUESTION_MESSAGE
        );

        if (reason != null && !reason.trim().isEmpty()) {
            boolean success = approvalService.rejectCourse(courseId);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Course rejected successfully.\nReason: " + reason,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshPanels();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to reject course. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (reason != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please provide a reason for rejection.",
                    "Reason Required",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // Refresh all panels
    public void refreshPanels() {
        tabbedPane.removeAll();
        tabbedPane.addTab("📋 Pending Courses", createPendingCoursesPanel());
        tabbedPane.addTab("📚 All Courses", createAllCoursesPanel());
        tabbedPane.addTab("👥 All Users", createAllUsersPanel());
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    // Logout
    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }
}