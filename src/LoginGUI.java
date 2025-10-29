import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginGUI extends BaseGUI {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private UserFileManager userManager;

    public LoginGUI() {
        userManager = new UserFileManager("src/data/users.csv");
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setTitle("Student Management System - Login");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerWindow();
        setResizable(false);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(PRIMARY_COLOR);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 15, 20));

        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Please login to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(BG_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(subtitleLabel);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        userLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(35);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField.setPreferredSize(new Dimension(450, 55));
        usernameField.setMargin(new Insets(12, 18, 12, 18));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INFO_COLOR, 3),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        passLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField(35);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        passwordField.setPreferredSize(new Dimension(450, 55));
        passwordField.setMargin(new Insets(12, 18, 12, 18));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INFO_COLOR, 3),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(180, 60));
        loginButton.setBackground(SUCCESS_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exitButton.setPreferredSize(new Dimension(180, 60));
        exitButton.setBackground(DANGER_COLOR);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        addHoverEffect(loginButton, SUCCESS_COLOR, new Color(39, 174, 96));
        addHoverEffect(exitButton, DANGER_COLOR, new Color(192, 57, 43));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 25, 20));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("Default credentials: admin / admin123");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(BG_COLOR);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(statusLabel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        bottomPanel.add(infoLabel);

        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);
        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(bottomPanel, BorderLayout.SOUTH);
        add(southContainer, BorderLayout.SOUTH);

        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> handleLogin());

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                usernameField.requestFocus();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠ Please enter username and password!");
            statusLabel.setForeground(new Color(255, 220, 220));
            return;
        }

        if (userManager.validateLogin(username, password)) {
            statusLabel.setText("✓ Login successful! Loading system...");
            statusLabel.setForeground(new Color(200, 255, 200));
            loginButton.setEnabled(false);
            exitButton.setEnabled(false);

            Timer timer = new Timer(500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        FileManager fileManager = new FileManager("src/data/students.csv");
                        StudentManager studentManager = new StudentManager(fileManager);

                        MainGUI mainGUI = new MainGUI(studentManager, username);
                        mainGUI.setVisible(true);
                        dispose();

                    } catch (IOException ex) {
                        showError("Error loading system: " + ex.getMessage());
                        loginButton.setEnabled(true);
                        exitButton.setEnabled(true);
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            statusLabel.setText("✗ Invalid username or password!");
            statusLabel.setForeground(new Color(255, 200, 200));
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
        });
    }
}