package MainGui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Dialog for selecting a Sudoku file to load
 * Provides file chooser for loading solved Sudoku puzzles
 */
public class FileInputDialog extends JDialog {
    private String selectedFilePath;
    private boolean cancelled;
    private JFileChooser fileChooser;

    /**
     * Constructor
     * @param parent Parent frame
     */
    public FileInputDialog(JFrame parent) {
        super(parent, "Select Sudoku File", true);
        this.selectedFilePath = null;
        this.cancelled = true;

        setupUI();
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * Sets up the dialog UI
     */
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Load Solved Sudoku Puzzle");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // File Chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));
        fileChooser.setPreferredSize(new Dimension(500, 350));
        add(fileChooser, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(getContentPane().getBackground());

        // Load Button
        JButton loadButton = new JButton("Load");
        loadButton.setPreferredSize(new Dimension(100, 35));
        loadButton.setFont(new Font("Arial", Font.BOLD, 12));
        loadButton.setBackground(new Color(100, 180, 100));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(e -> loadFile());
        buttonPanel.add(loadButton);

        // Cancel Button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(200, 100, 100));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> {
            selectedFilePath = null;
            cancelled = true;
            dispose();
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles load file action
     */
    private void loadFile() {
        File selectedFile = fileChooser.getSelectedFile();

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a file",
                    "No File Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!selectedFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "File does not exist: " + selectedFile.getName(),
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!selectedFile.isFile()) {
            JOptionPane.showMessageDialog(this,
                    "Selected item is not a file",
                    "Invalid Selection",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedFilePath = selectedFile.getAbsolutePath();
        cancelled = false;
        dispose();
    }

    /**
     * Shows dialog and returns selected file path
     * @param parent Parent frame
     * @return File path or null if cancelled
     */
    public static String showDialog(JFrame parent) {
        FileInputDialog dialog = new FileInputDialog(parent);
        dialog.setVisible(true);
        return dialog.selectedFilePath;
    }

    /**
     * Gets selected file path
     * @return Absolute path to selected file or null
     */
    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    /**
     * Checks if dialog was cancelled
     * @return true if cancelled, false if file was selected
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets initial directory for file chooser
     * @param path Directory path
     */
    public void setInitialDirectory(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        File file = new File(path);
        if (file.isDirectory()) {
            fileChooser.setCurrentDirectory(file);
        }
    }

    /**
     * Sets initial file
     * @param filename File name to pre-select
     */
    public void setInitialFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        fileChooser.setSelectedFile(new File(filename));
    }

    /**
     * Gets the file chooser component
     * @return JFileChooser instance
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Sets dialog size
     * @param width Dialog width
     * @param height Dialog height
     */
    public void setDialogSize(int width, int height) {
        setSize(width, height);
    }

    /**
     * Enables/disables file type filtering
     * @param accept true to accept all file types
     */
    public void setAcceptAllFileFilter(boolean accept) {
        fileChooser.setAcceptAllFileFilterUsed(accept);
    }
}