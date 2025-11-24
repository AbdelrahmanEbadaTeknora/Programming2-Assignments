package ui.components;

import database.JsonDatabaseManager;
import java.awt.*;
import java.util.ArrayList; //
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import models.*;
import services.CertificateService;

public class StudentDashboardFrame extends JFrame {
    private Student currentStudent;
    private JsonDatabaseManager dbManager;
    private JTabbedPane tabbedPane;

    private static final Color PRIMARY_COLOR = new Color(59, 89, 182);
    private static final Color SECONDARY_COLOR = new Color(240, 242, 245);
    private static final Color SUCCESS_COLOR = new Color(67, 160, 71);
    private static final Color ACCENT_COLOR = new Color(255, 87, 34);

    public StudentDashboardFrame(Student student) {
        this.currentStudent = student;
        this.dbManager = JsonDatabaseManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SkillForge - Student Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        add(createTopPanel(), BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.addTab("📚 Browse Courses", createBrowsePanel());
        tabbedPane.addTab("📖 My Courses", createMyCoursesPanel());
        tabbedPane.addTab("🏆 Certificates", createCertificatesPanel()); // ✅ ADDED: Certificates tab

        add(tabbedPane, BorderLayout.CENTER);

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    }
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PRIMARY_COLOR);

        JLabel welcome = new JLabel("Welcome, " + currentStudent.getUsername() + "! 👋");
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(Color.BLACK);

        int enrolledCount = currentStudent.getEnrolledCourses() != null ?
                currentStudent.getEnrolledCourses().size() : 0;
        JLabel stats = new JLabel("Enrolled in " + enrolledCount + " courses");
        stats.setFont(new Font("Arial", Font.PLAIN, 12));
        stats.setForeground(new Color(220, 220, 220));

        leftPanel.add(welcome);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(stats);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(ACCENT_COLOR);
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBorderPainted(false);
        logoutButton.addActionListener(e -> logout());

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        List<Course> availableCourses = getAvailableCourses();
        JLabel title = new JLabel("Available Courses (" + availableCourses.size() + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBackground(SECONDARY_COLOR);

        if (availableCourses.isEmpty()) {
            JPanel emptyState = createEmptyState(
                    "🎓",
                    "No Available Courses",
                    "All courses have been enrolled! Check back later for new courses."
            );
            courseListPanel.add(emptyState);
        } else {
            for (Course course : availableCourses) {
                courseListPanel.add(createCourseCard(course, true));
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(courseListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        List<Course> enrolledCourses = getEnrolledCourses();
        JLabel title = new JLabel("My Enrolled Courses (" + enrolledCourses.size() + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        JPanel courseListPanel = new JPanel();
        courseListPanel.setLayout(new BoxLayout(courseListPanel, BoxLayout.Y_AXIS));
        courseListPanel.setBackground(SECONDARY_COLOR);

        if (enrolledCourses.isEmpty()) {
            JPanel emptyState = createEmptyState(
                    "📚",
                    "No Enrolled Courses",
                    "Start learning by enrolling in courses from the Browse tab!"
            );
            courseListPanel.add(emptyState);
        } else {
            for (Course course : enrolledCourses) {
                courseListPanel.add(createEnrolledCourseCard(course));
                courseListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(courseListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

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

    private JPanel createCourseCard(Course course, boolean showEnrollButton) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Course info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel descLabel = new JLabel("<html><body style='width: 500px'>" +
                course.getDescription() + "</body></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.DARK_GRAY);

        String instructorName = getInstructorName(course.getInstructorId());
        JLabel instructorLabel = new JLabel("👤 Instructor: " + instructorName);
        instructorLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructorLabel.setForeground(Color.GRAY);
        int lessonCount = course.getLessons() != null ? course.getLessons().length : 0;
        JLabel lessonCountLabel = new JLabel("📝 " + lessonCount + " lessons");
        lessonCountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lessonCountLabel.setForeground(Color.GRAY);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(instructorLabel);
        infoPanel.add(lessonCountLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        if (showEnrollButton) {
            JButton enrollButton = new JButton("Enroll Now");
            enrollButton.setFont(new Font("Arial", Font.BOLD, 12));
            enrollButton.setBackground(SUCCESS_COLOR);
            enrollButton.setForeground(Color.BLACK);
            enrollButton.setFocusPainted(false);
            enrollButton.setBorderPainted(false);
            enrollButton.setPreferredSize(new Dimension(120, 35));
            enrollButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            enrollButton.addActionListener(e -> handleEnrollment(course));

            card.add(enrollButton, BorderLayout.EAST);
        }

        return card;
    }

    private JPanel createEnrolledCourseCard(Course course) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY_COLOR, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(course.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);

        String instructorName = getInstructorName(course.getInstructorId());
        JLabel instructorLabel = new JLabel("👤 Instructor: " + instructorName);
        instructorLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructorLabel.setForeground(Color.GRAY);

        double progress = getCourseProgress(course.getCourseId());
        int completedLessons = getCompletedLessonCount(course.getCourseId());
        int totalLessons = course.getLessons() != null ? course.getLessons().length : 0;

        JLabel progressLabel = new JLabel(String.format("📊 Progress: %d/%d lessons completed",
                completedLessons, totalLessons));
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        progressLabel.setForeground(Color.DARK_GRAY);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) progress);
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.0f%%", progress));
        progressBar.setForeground(SUCCESS_COLOR);
        progressBar.setPreferredSize(new Dimension(200, 25));
        progressBar.setBorder(BorderFactory.createLineBorder(SUCCESS_COLOR, 1));

        if (progress == 100.0) {
            JLabel completedBadge = new JLabel(" Completed!");
            completedBadge.setFont(new Font("Arial", Font.BOLD, 11));
            completedBadge.setForeground(SUCCESS_COLOR);
            infoPanel.add(completedBadge);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        }

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(instructorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(progressLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(progressBar);

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new JButton("View Lessons");
        viewButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewButton.setBackground(PRIMARY_COLOR);
        viewButton.setForeground(Color.BLACK);
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewButton.setMaximumSize(new Dimension(120, 35));
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> viewCourseLessons(course));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createVerticalGlue());

        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    private List<Course> getAvailableCourses() {
        List<Course> allCourses = dbManager.getApprovedCourses();
        List<String> enrolledCourseIds = currentStudent.getEnrolledCourses();

        if (enrolledCourseIds == null || enrolledCourseIds.isEmpty()) {
            return allCourses;
        }

        Set<String> enrolledSet = new HashSet<>(enrolledCourseIds);
        return allCourses.stream()
                .filter(course -> !enrolledSet.contains(course.getCourseId()))
                .collect(Collectors.toList());
    }

    private List<Course> getEnrolledCourses() {
        List<String> enrolledCourseIds = currentStudent.getEnrolledCourses();

        if (enrolledCourseIds == null || enrolledCourseIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Course> enrolledCourses = new ArrayList<>();
        for (String courseId : enrolledCourseIds) {
            Course course = dbManager.getCourseById(courseId);
            if (course != null) {
                enrolledCourses.add(course);
            }
        }

        return enrolledCourses;
    }

    private double getCourseProgress(String courseId) {
        List<Lesson> lessons = dbManager.getLessonsByCourse(courseId);
        if (lessons.isEmpty()) {
            return 0.0;
        }
        List<String> completedLessonIds = dbManager.getCompletedLessons(currentStudent.getUserId(), courseId);
        int totalLessons = lessons.size();
        int completedLessons = completedLessonIds.size();
        return (completedLessons * 100.0) / totalLessons;
    }

    private int getCompletedLessonCount(String courseId) {
        List<String> completedLessons = dbManager.getCompletedLessons(currentStudent.getUserId(), courseId);
        return completedLessons.size();
    }

    private String getInstructorName(String instructorId) {
        User user = dbManager.getUserById(instructorId);
        if (user != null) {
            return user.getUsername();
        }
        return instructorId;
    }

    private void handleEnrollment(Course course) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html><body style='width: 300px'>" +
                        "<b>Enroll in " + course.getTitle() + "?</b><br><br>" +
                        course.getDescription() +
                        "</body></html>",
                "Confirm Enrollment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dbManager.enrollStudent(currentStudent.getUserId(), course.getCourseId());

            if (success) {
                // Reload student data to get updated enrolled courses
                User updatedUser = dbManager.getUserById(currentStudent.getUserId());
                if (updatedUser instanceof Student) {
                    currentStudent = (Student) updatedUser;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "<html><body style='width: 250px'>" +
                                "<b>Success! 🎉</b><br><br>" +
                                "You have successfully enrolled in <b>" + course.getTitle() + "</b>.<br>" +
                                "Start learning by viewing the lessons!" +
                                "</body></html>",
                        "Enrollment Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshPanels();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "<html><body style='width: 250px'>" +
                                "<b>Enrollment Failed</b><br><br>" +
                                "You may already be enrolled in this course, or an error occurred." +
                                "</body></html>",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void viewCourseLessons(Course course) {
    LessonViewerDialog dialog = new LessonViewerDialog(
            this,
            course.getCourseId(),
            course.getTitle(),
            currentStudent.getUserId()
    );
    dialog.setVisible(true);

    // Reload student data
    User updatedUser = dbManager.getUserById(currentStudent.getUserId());
    if (updatedUser instanceof Student) {
        currentStudent = (Student) updatedUser;
    }
    
    // ✅ ADD: Auto-check for certificate after completing lessons
    CertificateService certService = new CertificateService();
    if (certService.isEligibleForCertificate(currentStudent.getUserId(), course.getCourseId())) {
        int generate = JOptionPane.showConfirmDialog(this,
            "<html><body style='width: 300px'>" +
            "🎉 <b>Congratulations!</b><br><br>" +
            "You've completed all requirements for <b>" + course.getTitle() + "</b>!<br><br>" +
            "Would you like to generate your certificate now?" +
            "</body></html>",
            "Certificate Available!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (generate == JOptionPane.YES_OPTION) {
            Certificate cert = certService.generateCertificate(
                currentStudent.getUserId(), 
                course.getCourseId()
            );
            
            if (cert != null) {
                JOptionPane.showMessageDialog(this,
                    "<html><body style='width: 300px'>" +
                    "✅ <b>Certificate Generated!</b><br><br>" +
                    "Your certificate for <b>" + course.getTitle() + "</b> is ready!<br><br>" +
                    "Final Score: <b>" + cert.getFormattedScore() + "</b><br><br>" +
                    "Check the Certificates tab to view it." +
                    "</body></html>",
                    "Success!",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    refreshPanels();
}

    private void refreshPanels() {
        tabbedPane.removeAll();
        tabbedPane.addTab("📚 Browse Courses", createBrowsePanel());
        tabbedPane.addTab("📖 My Courses", createMyCoursesPanel());
        tabbedPane.addTab("🏆 Certificates", createCertificatesPanel());
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    private void logout() {
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
                try {
                    Class<?> loginClass = Class.forName("ui.LoginFrame");
                    JFrame loginFrame = (JFrame) loginClass.getDeclaredConstructor().newInstance();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    System.out.println("LoginFrame not available. Application closing.");
                    System.exit(0);
                }
            });
        }
    }

    private JPanel createCertificatesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(SECONDARY_COLOR);

        CertificateService certService = new CertificateService();
        List<Certificate> certificates = certService.getStudentCertificates(currentStudent.getUserId());

        JLabel title = new JLabel("My Certificates (" + certificates.size() + ")");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(title, BorderLayout.NORTH);

        JPanel certListPanel = new JPanel();
        certListPanel.setLayout(new BoxLayout(certListPanel, BoxLayout.Y_AXIS));
        certListPanel.setBackground(SECONDARY_COLOR);

        if (certificates.isEmpty()) {
            JPanel emptyState = createEmptyState(
                    "🏆",
                    "No Certificates Yet",
                    "Complete courses to earn certificates! Certificates will appear here once you've successfully finished a course."
            );
            certListPanel.add(emptyState);
        } else {
            for (Certificate cert : certificates) {
                certListPanel.add(createCertificateCard(cert));
                certListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(certListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCertificateCard(Certificate cert) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(SUCCESS_COLOR, 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel courseLabel = new JLabel(cert.getCourseName());
        courseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        courseLabel.setForeground(PRIMARY_COLOR);

        JLabel scoreLabel = new JLabel("Final Score: " + cert.getFormattedScore());
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreLabel.setForeground(Color.DARK_GRAY);

        JLabel dateLabel = new JLabel("Issued: " + cert.getFormattedIssueDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLabel.setForeground(Color.GRAY);

        JLabel certIdLabel = new JLabel("ID: " + cert.getCertificateId());
        certIdLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        certIdLabel.setForeground(Color.LIGHT_GRAY);

        infoPanel.add(courseLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(certIdLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new JButton("View");
        viewButton.setFont(new Font("Arial", Font.BOLD, 12));
        viewButton.setBackground(PRIMARY_COLOR);
        viewButton.setForeground(Color.BLACK);
        viewButton.setFocusPainted(false);
        viewButton.setBorderPainted(false);
        viewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewButton.setMaximumSize(new Dimension(100, 30));
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> viewCertificate(cert));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(viewButton);
        buttonPanel.add(Box.createVerticalGlue());

        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void viewCertificate(Certificate cert) {
        try {
            Class<?> certViewerClass = Class.forName("ui.components.CertificateViewerDialog");
            JDialog certDialog = (JDialog) certViewerClass.getDeclaredConstructor(
                    JFrame.class, Certificate.class
            ).newInstance(this, cert);
            certDialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Certificate viewer not available.\n" +
                            "Certificate Details:\n" +
                            "Course: " + cert.getCourseName() + "\n" +
                            "Score: " + cert.getFormattedScore() + "\n" +
                            "Issued: " + cert.getFormattedIssueDate(),
                    "Certificate: " + cert.getCourseName(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JsonDatabaseManager db = JsonDatabaseManager.getInstance();
            Student testStudent = null; // (Student) db.getUserById("S001");

            if (testStudent == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "<html><body style='width: 300px'>" +
                                "<b>No Student Found</b><br><br>" +
                                "No student with ID 'S001' exists in the database.<br><br>" +
                                "Please create a student account first using the signup feature." +
                                "</body></html>",
                        "Student Not Found",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            StudentDashboardFrame frame = new StudentDashboardFrame(testStudent);
            frame.setVisible(true);
        });
    }
}