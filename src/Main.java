import javax.swing.*;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Set look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== SkillForge Learning Platform ===");
            System.out.println("Starting application...");

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

            System.out.println("Application started successfully!");
            System.out.println("Test Credentials:");
            System.out.println("- Student: ahmed@skillforge.com / student123");
            System.out.println("- Instructor: layla@skillforge.com / instructor123");
            System.out.println("- Admin: Create a new admin account from signup!");
        });
    }
}