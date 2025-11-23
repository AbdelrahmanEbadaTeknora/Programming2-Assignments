package ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {

    // Chart types enum
    public enum ChartType {
        BAR, LINE, PIE
    }

    private String title;
    private Map<String, Double> data;
    private ChartType chartType;

    // Colors for charts
    private static final Color[] COLORS = {
            new Color(65, 105, 225),   // Royal Blue
            new Color(50, 205, 50),    // Lime Green
            new Color(255, 99, 71),    // Tomato
            new Color(255, 215, 0),    // Gold
            new Color(138, 43, 226),   // Blue Violet
            new Color(0, 206, 209),    // Dark Turquoise
            new Color(255, 140, 0),    // Dark Orange
            new Color(199, 21, 133),   // Medium Violet Red
            new Color(0, 128, 128),    // Teal
            new Color(220, 20, 60)     // Crimson
    };

    private static final int PADDING = 50;

    public ChartPanel(String title, Map<String, Double> data, ChartType chartType) {
        this.title = title;
        this.data = data != null ? data : new LinkedHashMap<>();
        this.chartType = chartType;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawTitle(g2d);

        if (data.isEmpty()) {
            drawNoDataMessage(g2d);
            return;
        }

        switch (chartType) {
            case BAR:
                drawBarChart(g2d);
                break;
            case LINE:
                drawLineChart(g2d);
                break;
            case PIE:
                drawPieChart(g2d);
                break;
        }
    }

    private void drawTitle(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int x = (getWidth() - titleWidth) / 2;
        g2d.drawString(title, x, 25);
    }

    private void drawNoDataMessage(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        g2d.setColor(Color.GRAY);
        String message = "No data available";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(message, x, y);
    }

    public void drawBarChart(Graphics2D g2d) {
        int chartWidth = getWidth() - 2 * PADDING;
        int chartHeight = getHeight() - 2 * PADDING - 40;
        int startX = PADDING;
        int startY = getHeight() - PADDING;

        List<String> labels = new ArrayList<>(data.keySet());
        List<Double> values = new ArrayList<>(data.values());

        double maxValue = getMaxValue();
        if (maxValue == 0) maxValue = 100;

        int barWidth = Math.max(20, (chartWidth - (labels.size() + 1) * 10) / Math.max(1, labels.size()));
        int gap = 10;

        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(startX, startY, startX, PADDING + 30);
        g2d.drawLine(startX, startY, startX + chartWidth, startY);

        // Draw Y-axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int y = startY - (i * chartHeight / 5);
            double value = (i * maxValue / 5);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(startX, y, startX + chartWidth, y);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.format("%.0f", value), 5, y + 5);
        }

        // Draw bars
        int x = startX + gap;
        int colorIndex = 0;

        for (int i = 0; i < labels.size(); i++) {
            double value = values.get(i);
            int barHeight = (int) ((value / maxValue) * chartHeight);

            g2d.setColor(COLORS[colorIndex % COLORS.length]);
            g2d.fillRect(x, startY - barHeight, barWidth, barHeight);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, startY - barHeight, barWidth, barHeight);

            // Value on top
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String valueStr = String.format("%.1f", value);
            FontMetrics fm = g2d.getFontMetrics();
            int valueX = x + (barWidth - fm.stringWidth(valueStr)) / 2;
            g2d.drawString(valueStr, valueX, startY - barHeight - 5);

            // Label below
            g2d.setFont(new Font("Arial", Font.PLAIN, 9));
            String label = labels.get(i);
            if (label.length() > 10) {
                label = label.substring(0, 10) + "...";
            }
            fm = g2d.getFontMetrics();
            int labelX = x + (barWidth - fm.stringWidth(label)) / 2;
            g2d.drawString(label, labelX, startY + 15);

            x += barWidth + gap;
            colorIndex++;
        }
    }

    public void drawLineChart(Graphics2D g2d) {
        int chartWidth = getWidth() - 2 * PADDING;
        int chartHeight = getHeight() - 2 * PADDING - 40;
        int startX = PADDING;
        int startY = getHeight() - PADDING;

        List<String> labels = new ArrayList<>(data.keySet());
        List<Double> values = new ArrayList<>(data.values());

        double maxValue = getMaxValue();
        if (maxValue == 0) maxValue = 100;

        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(startX, startY, startX, PADDING + 30);
        g2d.drawLine(startX, startY, startX + chartWidth, startY);

        // Y-axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int y = startY - (i * chartHeight / 5);
            double value = (i * maxValue / 5);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(startX, y, startX + chartWidth, y);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.format("%.0f", value), 5, y + 5);
        }

        if (values.isEmpty()) return;

        int pointSpacing = values.size() > 1 ? chartWidth / (values.size() - 1) : chartWidth;
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            int px = startX + (values.size() > 1 ? i * pointSpacing : chartWidth / 2);
            int py = startY - (int) ((values.get(i) / maxValue) * chartHeight);
            points.add(new Point(px, py));
        }

        // Draw line
        g2d.setColor(COLORS[0]);
        g2d.setStroke(new BasicStroke(3));
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Draw points and labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);

            g2d.setColor(COLORS[0]);
            g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(p.x - 3, p.y - 3, 6, 6);

            g2d.setColor(Color.BLACK);
            String valueStr = String.format("%.1f", values.get(i));
            g2d.drawString(valueStr, p.x - 10, p.y - 10);

            if (i < labels.size()) {
                String label = labels.get(i);
                if (label.length() > 8) {
                    label = label.substring(0, 8) + "..";
                }
                FontMetrics fm = g2d.getFontMetrics();
                int labelX = p.x - fm.stringWidth(label) / 2;
                g2d.drawString(label, labelX, startY + 15);
            }
        }
    }

    public void drawPieChart(Graphics2D g2d) {
        int centerX = getWidth() / 2 - 50;
        int centerY = getHeight() / 2 + 10;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        List<String> labels = new ArrayList<>(data.keySet());
        List<Double> values = new ArrayList<>(data.values());

        double total = 0;
        for (double v : values) {
            total += v;
        }

        if (total == 0) {
            drawNoDataMessage(g2d);
            return;
        }

        double startAngle = 0;
        for (int i = 0; i < values.size(); i++) {
            double percentage = values.get(i) / total;
            double arcAngle = percentage * 360;

            g2d.setColor(COLORS[i % COLORS.length]);
            g2d.fillArc(centerX - radius, centerY - radius,
                    2 * radius, 2 * radius,
                    (int) startAngle, (int) arcAngle);

            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawArc(centerX - radius, centerY - radius,
                    2 * radius, 2 * radius,
                    (int) startAngle, (int) arcAngle);

            startAngle += arcAngle;
        }

        // Legend
        int legendX = getWidth() - 150;
        int legendY = 60;
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));

        for (int i = 0; i < labels.size(); i++) {
            g2d.setColor(COLORS[i % COLORS.length]);
            g2d.fillRect(legendX, legendY + i * 20, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendY + i * 20, 15, 15);

            String label = labels.get(i);
            if (label.length() > 12) {
                label = label.substring(0, 12) + "...";
            }
            double percentage = (values.get(i) / total) * 100;
            g2d.drawString(String.format("%s (%.1f%%)", label, percentage),
                    legendX + 20, legendY + i * 20 + 12);
        }
    }

    public void setData(Map<String, Double> data) {
        this.data = data != null ? data : new LinkedHashMap<>();
        repaint();
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
        repaint();
    }

    public void setTitle(String title) {
        this.title = title;
        repaint();
    }

    public void refresh() {
        repaint();
    }

    public double getMaxValue() {
        double max = 0;
        for (double v : data.values()) {
            if (v > max) max = v;
        }
        return max;
    }

    public double getMinValue() {
        double min = Double.MAX_VALUE;
        for (double v : data.values()) {
            if (v < min) min = v;
        }
        return min == Double.MAX_VALUE ? 0 : min;
    }
}