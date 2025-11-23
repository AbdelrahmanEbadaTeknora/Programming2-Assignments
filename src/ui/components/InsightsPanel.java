package ui.components;

import services.AnalyticsService;
import models.Student;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class InsightsPanel extends JPanel {

    private String courseId;
    private AnalyticsService analyticsService;

    private JPanel summaryPanel;
    private ChartPanel performanceChart;
    private ChartPanel completionChart;
    private ChartPanel quizAveragesChart;
    private JPanel studentListPanel;

    public InsightsPanel(String courseId) {
        this.courseId = courseId;
        this.analyticsService = new AnalyticsService();

        initializeUI();
        loadAnalytics();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Course Analytics & Insights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 245, 250));

        summaryPanel = createSummaryPanel();
        mainContent.add(summaryPanel);
        mainContent.add(Box.createVerticalStrut(15));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        chartsPanel.setBackground(new Color(245, 245, 250));
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        performanceChart = createPerformanceChart();
        JPanel perfWrapper = createChartWrapper(performanceChart);
        chartsPanel.add(perfWrapper);

        completionChart = createCompletionChart();
        JPanel compWrapper = createChartWrapper(completionChart);
        chartsPanel.add(compWrapper);

        quizAveragesChart = createQuizAveragesChart();
        JPanel quizWrapper = createChartWrapper(quizAveragesChart);
        chartsPanel.add(quizWrapper);

        mainContent.add(chartsPanel);
        mainContent.add(Box.createVerticalStrut(15));

        studentListPanel = createStudentListPanel();
        mainContent.add(studentListPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refresh());

        JButton exportBtn = new JButton("Export Report");
        exportBtn.addActionListener(e -> exportReport());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(exportBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createChartWrapper(ChartPanel chart) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        wrapper.add(chart, BorderLayout.CENTER);
        return wrapper;
    }

    public JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 15, 0));
        panel.setBackground(new Color(245, 245, 250));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setBorder(new EmptyBorder(0, 0, 10, 0));

        Map<String, Object> stats = analyticsService.getCourseStatsSummary(courseId);

        panel.add(createStatCard("Total Students",
                String.valueOf(stats.getOrDefault("totalStudents", 0)),
                new Color(65, 105, 225)));

        panel.add(createStatCard("Total Lessons",
                String.valueOf(stats.getOrDefault("totalLessons", 0)),
                new Color(50, 205, 50)));

        panel.add(createStatCard("Completion Rate",
                String.format("%.1f%%", stats.getOrDefault("completionRate", 0.0)),
                new Color(255, 140, 0)));

        panel.add(createStatCard("Avg Quiz Score",
                String.format("%.1f%%", stats.getOrDefault("averageQuizScore", 0.0)),
                new Color(138, 43, 226)));

        int strugglingCount = analyticsService.getStrugglingStudents(courseId, 50.0).size();
        panel.add(createStatCard("Struggling Students",
                String.valueOf(strugglingCount),
                strugglingCount > 0 ? new Color(220, 20, 60) : new Color(50, 205, 50)));

        return panel;
    }

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
        card.add(Box.createVerticalStrut(5));
        card.add(titleLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    public ChartPanel createPerformanceChart() {
        Map<String, Double> data = analyticsService.getStudentPerformanceWithNames(courseId);
        return new ChartPanel("Student Progress (%)", data, ChartPanel.ChartType.BAR);
    }

    public ChartPanel createCompletionChart() {
        Map<String, Double> data = analyticsService.getLessonCompletionRates(courseId);
        return new ChartPanel("Lesson Completion (%)", data, ChartPanel.ChartType.BAR);
    }

    public ChartPanel createQuizAveragesChart() {
        Map<String, Double> data = analyticsService.getQuizAverages(courseId);
        return new ChartPanel("Quiz Scores", data, ChartPanel.ChartType.LINE);
    }

    public JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel title = new JLabel("Student Overview");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Top performers tab
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        List<Student> topStudents = analyticsService.getTopPerformingStudents(courseId, 5);
        if (topStudents.isEmpty()) {
            topPanel.add(new JLabel("No students enrolled yet"));
        } else {
            for (int i = 0; i < topStudents.size(); i++) {
                Student s = topStudents.get(i);
                double progress = analyticsService.getStudentProgress(s.getUserId(), courseId);
                topPanel.add(createStudentRow(i + 1, s.getUsername(), progress, new Color(50, 205, 50)));
            }
        }
        tabbedPane.addTab("Top Performers", new JScrollPane(topPanel));

        // Struggling students tab
        JPanel strugglingPanel = new JPanel();
        strugglingPanel.setLayout(new BoxLayout(strugglingPanel, BoxLayout.Y_AXIS));
        strugglingPanel.setBackground(Color.WHITE);

        List<Student> strugglingStudents = analyticsService.getStrugglingStudents(courseId, 50.0);
        if (strugglingStudents.isEmpty()) {
            strugglingPanel.add(new JLabel("No struggling students!"));
        } else {
            for (int i = 0; i < strugglingStudents.size(); i++) {
                Student s = strugglingStudents.get(i);
                double progress = analyticsService.getStudentProgress(s.getUserId(), courseId);
                strugglingPanel.add(createStudentRow(i + 1, s.getUsername(), progress, new Color(220, 20, 60)));
            }
        }
        tabbedPane.addTab("Need Help", new JScrollPane(strugglingPanel));

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentRow(int rank, String name, double progress, Color progressColor) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(8, 10, 8, 10));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JLabel nameLabel = new JLabel(rank + ". " + name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        row.add(nameLabel, BorderLayout.WEST);

        JPanel progressPanel = new JPanel(new BorderLayout(5, 0));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setPreferredSize(new Dimension(200, 20));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue((int) progress);
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("%.1f%%", progress));
        progressBar.setForeground(progressColor);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        row.add(progressPanel, BorderLayout.EAST);
        return row;
    }

    public void loadAnalytics() {
        // Data loaded in create methods
    }

    public void refresh() {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
        JOptionPane.showMessageDialog(this, "Analytics refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    public void exportReport() {
        Map<String, Object> stats = analyticsService.getCourseStatsSummary(courseId);

        StringBuilder report = new StringBuilder();
        report.append("=== COURSE ANALYTICS REPORT ===\n\n");
        report.append("Course: ").append(stats.getOrDefault("courseTitle", "Unknown")).append("\n\n");
        report.append("SUMMARY:\n");
        report.append("- Total Students: ").append(stats.getOrDefault("totalStudents", 0)).append("\n");
        report.append("- Total Lessons: ").append(stats.getOrDefault("totalLessons", 0)).append("\n");
        report.append("- Completion Rate: ").append(String.format("%.1f%%", stats.getOrDefault("completionRate", 0.0))).append("\n");
        report.append("- Average Quiz Score: ").append(String.format("%.1f%%", stats.getOrDefault("averageQuizScore", 0.0))).append("\n\n");

        report.append("LESSON COMPLETION RATES:\n");
        Map<String, Double> completion = analyticsService.getLessonCompletionRates(courseId);
        for (Map.Entry<String, Double> entry : completion.entrySet()) {
            report.append("- ").append(entry.getKey()).append(": ").append(String.format("%.1f%%", entry.getValue())).append("\n");
        }

        report.append("\nSTUDENT PERFORMANCE:\n");
        Map<String, Double> performance = analyticsService.getStudentPerformanceWithNames(courseId);
        for (Map.Entry<String, Double> entry : performance.entrySet()) {
            report.append("- ").append(entry.getKey()).append(": ").append(String.format("%.1f%%", entry.getValue())).append("\n");
        }

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Analytics Report", JOptionPane.INFORMATION_MESSAGE);
    }
}