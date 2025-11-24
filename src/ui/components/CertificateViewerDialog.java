package ui.components;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import javax.swing.*;
import models.Certificate;
import services.CertificateService;

public class CertificateViewerDialog extends JDialog {
    private Certificate certificate;
    private CertificateService certificateService;

    public CertificateViewerDialog(JFrame parent, Certificate certificate) {
        super(parent, "Certificate of Completion", true);
        this.certificate = certificate;
        this.certificateService = new CertificateService();

        initializeUI();

        setSize(700, 600);
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Certificate display
        JPanel certificatePanel = createCertificateDisplay();
        add(certificatePanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JPanel createCertificateDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 253, 247));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(212, 175, 55), 5),
                        BorderFactory.createEmptyBorder(40, 40, 40, 40)
                )
        ));

        // Top decoration
        JLabel topDecoration = new JLabel("═══════════════════════════");
        topDecoration.setFont(new Font("Arial", Font.PLAIN, 20));
        topDecoration.setForeground(new Color(139, 69, 19));
        topDecoration.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(topDecoration);
        panel.add(Box.createVerticalStrut(10));

        // Title
        JLabel titleLabel = new JLabel("CERTIFICATE OF COMPLETION");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        // Bottom decoration
        JLabel bottomDecoration = new JLabel("═══════════════════════════");
        bottomDecoration.setFont(new Font("Arial", Font.PLAIN, 20));
        bottomDecoration.setForeground(new Color(139, 69, 19));
        bottomDecoration.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(bottomDecoration);
        panel.add(Box.createVerticalStrut(40));

        // "This is to certify that"
        JLabel certifyLabel = new JLabel("This is to certify that");
        certifyLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        certifyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(certifyLabel);
        panel.add(Box.createVerticalStrut(20));

        // Student name
        JLabel nameLabel = new JLabel(certificate.getStudentName());
        nameLabel.setFont(new Font("Serif", Font.BOLD, 32));
        nameLabel.setForeground(new Color(33, 150, 243));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));

        // Has successfully completed
        JLabel completedLabel = new JLabel("has successfully completed the course");
        completedLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        completedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(completedLabel);
        panel.add(Box.createVerticalStrut(20));

        // Course name
        JLabel courseLabel = new JLabel(certificate.getCourseName());
        courseLabel.setFont(new Font("Serif", Font.BOLD, 24));
        courseLabel.setForeground(new Color(76, 175, 80));
        courseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(courseLabel);
        panel.add(Box.createVerticalStrut(20));

        // Score
        JLabel scoreLabel = new JLabel("with a final score of " + certificate.getFormattedScore());
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(scoreLabel);
        panel.add(Box.createVerticalStrut(30));

        // Issue date
        JLabel dateLabel = new JLabel("Issued on: " + certificate.getFormattedIssueDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(40));

        // Signature line
        JPanel signaturePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signaturePanel.setOpaque(false);

        JPanel sigBox = new JPanel();
        sigBox.setLayout(new BoxLayout(sigBox, BoxLayout.Y_AXIS));
        sigBox.setOpaque(false);

        JLabel sigLine = new JLabel("_____________________________");
        sigLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructorLabel = new JLabel(certificate.getInstructorName());
        instructorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("Course Instructor");
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sigBox.add(sigLine);
        sigBox.add(Box.createVerticalStrut(5));
        sigBox.add(instructorLabel);
        sigBox.add(roleLabel);

        signaturePanel.add(sigBox);
        panel.add(signaturePanel);
        panel.add(Box.createVerticalStrut(30));

        // Certificate ID
        JLabel idLabel = new JLabel("Certificate ID: " + certificate.getCertificateId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        idLabel.setForeground(Color.GRAY);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(idLabel);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton downloadButton = new JButton("Download as JSON");
        downloadButton.setBackground(new Color(76, 175, 80));
        downloadButton.setForeground(Color.BLACK);
        downloadButton.setFocusPainted(false);
        downloadButton.addActionListener(e -> downloadCertificate());

        JButton printButton = new JButton("Print");
        printButton.addActionListener(e -> printCertificate());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        panel.add(downloadButton);
        panel.add(printButton);
        panel.add(closeButton);

        return panel;
    }

    private void downloadCertificate() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(certificate.getCertificateId() + ".json"));
        fileChooser.setDialogTitle("Save Certificate");

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                String json = certificateService.exportAsJSON(certificate);

                FileWriter writer = new FileWriter(file);
                writer.write(json);
                writer.close();

                JOptionPane.showMessageDialog(this,
                        "Certificate downloaded successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Failed to download certificate: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printCertificate() {
        JOptionPane.showMessageDialog(this,
                "Print functionality not implemented.\nUse your browser's print feature to print this certificate.",
                "Print",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void shareCertificate() {
        // Copy certificate ID to clipboard
        java.awt.datatransfer.StringSelection stringSelection =
                new java.awt.datatransfer.StringSelection(certificate.getCertificateId());
        java.awt.datatransfer.Clipboard clipboard =
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

        JOptionPane.showMessageDialog(this,
                "Certificate ID copied to clipboard!",
                "Shared",
                JOptionPane.INFORMATION_MESSAGE);
    }
}