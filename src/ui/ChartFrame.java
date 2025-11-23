package ui;

import services.AnalyticsService;
import ui.components.ChartPanel;
import ui.components.InsightsPanel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ChartFrame extends JFrame {

    private String courseId;
    private String courseTitle;
    private AnalyticsService analyticsService;

    private JTabbedPane chartTabs;
    private ChartPanel performanceChart;
    private ChartPanel completionChart;
    private ChartPanel quizScoresChart;

    public ChartFrame(String courseId, String courseTitle) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.analyticsService = new AnalyticsService();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Analytics - " + courseTitle);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        chartTabs = createChartTabs();
        mainPanel.add(chartTabs, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Course Analytics: " + courseTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        statsPanel.setBackground(new Color(245, 245, 250));

        int studentCount = analyticsService.getEnrolledStudentCount(courseId);
        double completionRate = analyticsService.getCourseCompletionRate(courseId);
        double avgQuizScore = analyticsService.getAverageQuizScore(courseId);

        statsPanel.add(createStatLabel("Students", String.valueOf(studentCount), new Color(65, 105, 225)));
        statsPanel.add(createStatLabel("Completion", String.format("%.1f%%", completionRate), new Color(50, 205, 50)));
        statsPanel.add(createStatLabel("Avg Score", String.format("%.1f%%", avgQuizScore), new Color(255, 140, 0)));

        panel.add(statsPanel, BorderLayout.EAST);
        return panel;
    }

    private JLabel createStatLabel(String label, String value, Color color) {
        JLabel lbl = new JLabel("<html><b style='color:" + toHex(color) + ";font-size:14px;'>" + value +
                "</b><br><span style='color:gray;font-size:10px;'>" + label + "</span></html>");
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public JTabbedPane createChartTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        InsightsPanel insightsPanel = new InsightsPanel(courseId);
        tabbedPane.addTab("Overview", insightsPanel);

        JPanel performanceTab = createPerformanceTab();
        tabbedPane.addTab("Student Performance", performanceTab);

        JPanel completionTab = createCompletionTab();
        tabbedPane.addTab("Lesson Completion", completionTab);

        JPanel quizTab = createQuizScoresTab();
        tabbedPane.addTab("Quiz Scores", quizTab);

        return tabbedPane;
    }

    public JPanel createPerformanceTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel descLabel = new JLabel("<html><b>Student Performance</b><br>" +
                "This chart shows each student's progress based on completed lessons.</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(descLabel, BorderLayout.NORTH);

        Map<String, Double> data = analyticsService.getStudentPerformanceWithNames(courseId);
        performanceChart = new ChartPanel("Student Progress (%)", data, ChartPanel.ChartType.BAR);
        performanceChart.setPreferredSize(new Dimension(800, 450));
        panel.add(performanceChart, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Chart Type:"));

        JComboBox<String> chartTypeCombo = new JComboBox<>(new String[]{"Bar Chart", "Line Chart", "Pie Chart"});
        chartTypeCombo.addActionListener(e -> {
            int index = chartTypeCombo.getSelectedIndex();
            ChartPanel.ChartType type = ChartPanel.ChartType.values()[index];
            performanceChart.setChartType(type);
        });
        controlPanel.add(chartTypeCombo);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel createCompletionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel descLabel = new JLabel("<html><b>Lesson Completion Rates</b><br>" +
                "Percentage of students who completed each lesson.</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(descLabel, BorderLayout.NORTH);

        Map<String, Double> data = analyticsService.getLessonCompletionRates(courseId);
        completionChart = new ChartPanel("Lesson Completion (%)", data, ChartPanel.ChartType.BAR);
        completionChart.setPreferredSize(new Dimension(800, 450));
        panel.add(completionChart, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Chart Type:"));

        JComboBox<String> chartTypeCombo = new JComboBox<>(new String[]{"Bar Chart", "Line Chart", "Pie Chart"});
        chartTypeCombo.addActionListener(e -> {
            int index = chartTypeCombo.getSelectedIndex();
            ChartPanel.ChartType type = ChartPanel.ChartType.values()[index];
            completionChart.setChartType(type);
        });
        controlPanel.add(chartTypeCombo);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    public JPanel createQuizScoresTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel descLabel = new JLabel("<html><b>Quiz Score Averages</b><br>" +
                "Average quiz scores for each lesson.</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(descLabel, BorderLayout.NORTH);

        Map<String, Double> data = analyticsService.getQuizAverages(courseId);
        quizScoresChart = new ChartPanel("Average Quiz Scores", data, ChartPanel.ChartType.LINE);
        quizScoresChart.setPreferredSize(new Dimension(800, 450));
        panel.add(quizScoresChart, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Chart Type:"));

        JComboBox<String> chartTypeCombo = new JComboBox<>(new String[]{"Bar Chart", "Line Chart", "Pie Chart"});
        chartTypeCombo.setSelectedIndex(1);
        chartTypeCombo.addActionListener(e -> {
            int index = chartTypeCombo.getSelectedIndex();
            ChartPanel.ChartType type = ChartPanel.ChartType.values()[index];
            quizScoresChart.setChartType(type);
        });
        controlPanel.add(chartTypeCombo);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(245, 245, 250));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshCharts());

        JButton exportBtn = new JButton("Export Data");
        exportBtn.addActionListener(e -> exportData());

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        panel.add(refreshBtn);
        panel.add(exportBtn);
        panel.add(closeBtn);

        return panel;
    }

    public void refreshCharts() {
        Map<String, Double> perfData = analyticsService.getStudentPerformanceWithNames(courseId);
        performanceChart.setData(perfData);

        Map<String, Double> compData = analyticsService.getLessonCompletionRates(courseId);
        completionChart.setData(compData);

        Map<String, Double> quizData = analyticsService.getQuizAverages(courseId);
        quizScoresChart.setData(quizData);

        JOptionPane.showMessageDialog(this, "Charts refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    public void exportData() {
        StringBuilder csv = new StringBuilder();

        csv.append("=== STUDENT PERFORMANCE ===\n");
        csv.append("Student,Progress (%)\n");
        Map<String, Double> perfData = analyticsService.getStudentPerformanceWithNames(courseId);
        for (Map.Entry<String, Double> entry : perfData.entrySet()) {
            csv.append(entry.getKey()).append(",").append(String.format("%.1f", entry.getValue())).append("\n");
        }

        csv.append("\n=== LESSON COMPLETION RATES ===\n");
        csv.append("Lesson,Completion (%)\n");
        Map<String, Double> compData = analyticsService.getLessonCompletionRates(courseId);
        for (Map.Entry<String, Double> entry : compData.entrySet()) {
            csv.append(entry.getKey()).append(",").append(String.format("%.1f", entry.getValue())).append("\n");
        }

        csv.append("\n=== QUIZ AVERAGES ===\n");
        csv.append("Quiz,Average Score\n");
        Map<String, Double> quizData = analyticsService.getQuizAverages(courseId);
        for (Map.Entry<String, Double> entry : quizData.entrySet()) {
            csv.append(entry.getKey()).append(",").append(String.format("%.1f", entry.getValue())).append("\n");
        }

        JTextArea textArea = new JTextArea(csv.toString());
        textArea.setEditable(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        Object[] options = {"Copy to Clipboard", "Close"};
        int result = JOptionPane.showOptionDialog(this, scrollPane, "Export Data",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (result == 0) {
            textArea.selectAll();
            textArea.copy();
            JOptionPane.showMessageDialog(this, "Data copied to clipboard!", "Export", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}