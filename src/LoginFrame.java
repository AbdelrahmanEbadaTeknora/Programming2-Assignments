import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));


        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);


        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);


        JButton loginButton = new JButton("Login");
        panel.add(loginButton);


        JButton signupButton = new JButton("Go to Signup");
        panel.add(signupButton);

        add(panel);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                User user = AuthManager.login(email, password);

                if (user == null) {
                    JOptionPane.showMessageDialog(null,
                            "Incorrect email or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Login successful!");

                    dispose();

                    if (user.getRole().equals("student")) {
                        new StudentDashboardFrame().setVisible(true);
                    } else {
                        new InstructorDashboardFrame().setVisible(true);
                    }
                }
            }
        });


        signupButton.addActionListener(e -> {
            dispose();
            new SignupFrame().setVisible(true);
        });
    }


    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}
