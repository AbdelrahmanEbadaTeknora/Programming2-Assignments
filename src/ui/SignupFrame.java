package ui;

import utils.AuthManager;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public SignupFrame() {
        setTitle("Signup");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 5));


        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);


        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);


        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);


        panel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"student", "instructor"});
        panel.add(roleBox);


        JButton signupButton = new JButton("Sign Up");
        panel.add(signupButton);

        JButton backButton = new JButton("Back to Login");
        panel.add(backButton);

        add(panel);


        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            boolean success = AuthManager.signup(username, email, password, role);

            if (success) {
                JOptionPane.showMessageDialog(null, "Signup successful!");
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Signup failed. Check your input.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}
