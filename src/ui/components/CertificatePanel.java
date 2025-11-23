package ui.components;

import models.Certificate;

import javax.swing.*;
import java.awt.*;

public class CertificatePanel extends JPanel {
    private Certificate certificate;
    private JButton viewButton;
    private JButton downloadButton;

    public CertificatePanel(Certificate certificate) {
        this.certificate = certificate;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(212, 175, 55), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        setBackground(new Color(255, 253, 247));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Header
        JPanel headerPanel = createCertificateHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Body
        JPanel bodyPanel = createCertificateBody();
        add(bodyPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createCertificateFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createCertificateHeader() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        JLabel icon = new JLabel("🏆");
        icon.setFont(new Font("Arial", Font.PLAIN, 36));

        JLabel titleLabel = new JLabel("CERTIFICATE OF COMPLETION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(139, 69, 19));

        panel.add(icon);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCertificateBody() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // Course
        JLabel courseLabel = new JLabel("Course: " + certificate.getCourseName());
        courseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(courseLabel);
        panel.add(Box.createVerticalStrut(8));

        // Student
        JLabel studentLabel = new JLabel("Student: " + certificate.getStudentName());
        studentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(studentLabel);
        panel.add(Box.createVerticalStrut(5));

        // Date
        JLabel dateLabel = new JLabel("Completed: " + certificate.getFormattedIssueDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(5));

        // Score
        JLabel scoreLabel = new JLabel("Final Score: " + certificate.getFormattedScore());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(76, 175, 80));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(scoreLabel);

        return panel;
    }

    private JPanel createCertificateFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Certificate ID
        JLabel idLabel = new JLabel("Certificate ID: " + certificate.getCertificateId());
        idLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        idLabel.setForeground(Color.GRAY);
        panel.add(idLabel, BorderLayout.WEST);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonsPanel.setOpaque(false);

        viewButton = new JButton("View Full Certificate");
        viewButton.setBackground(new Color(33, 150, 243));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFocusPainted(false);

        downloadButton = new JButton("Download");
        downloadButton.setBackground(new Color(76, 175, 80));
        downloadButton.setForeground(Color.WHITE);
        downloadButton.setFocusPainted(false);

        buttonsPanel.add(viewButton);
        buttonsPanel.add(downloadButton);

        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    public JButton getViewButton() {
        return viewButton;
    }

    public JButton getDownloadButton() {
        return downloadButton;
    }

    public Certificate getCertificate() {
        return certificate;
    }
}