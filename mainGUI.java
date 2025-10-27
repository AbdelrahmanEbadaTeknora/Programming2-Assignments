import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.Scanner;

public class mainGUI
{
    public static void main(String[] args) {
        try {
            IDataStorage fileStorage = new FileManager("data/students.csv");
            UserFileManager userManager = new UserFileManager("data/users.csv");

            StudentManager studentManager = new StudentManager(fileStorage);

            // Ask user which interface they prefer
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   STUDENT MANAGEMENT SYSTEM            ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\nSelect Interface:");
            System.out.println("1. Console UI (Text-based)");
            System.out.println("2. Graphical UI (GUI)");
            System.out.print("\nEnter choice (1 or 2): ");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                // Phase 1: Console UI
                LoginUI loginUI = new LoginUI(userManager);

                if (loginUI.showConsoleLogin()) {
                    ConsoleUI consoleUI = new ConsoleUI(studentManager);
                    consoleUI.start();
                } else {
                    System.out.println("\nLogin failed. Exiting system.");
                }

            } else if (choice.equals("2")) {
                // Phase 2: GUI
                SwingUtilities.invokeLater(() -> {
                    LoginGUI loginGUI = new LoginGUI();
                    loginGUI.setVisible(true);
                });

            } else {
                System.out.println("Invalid choice. Exiting.");
            }

        } catch (IOException e) {
            System.err.println("Error initializing system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}