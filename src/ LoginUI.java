import java.util.Scanner;

public class LoginUI {
    private UserFileManager userManager;
    private Scanner scanner;
    private int maxAttempts = 3;

    public LoginUI(UserFileManager userManager) {
        this.userManager = userManager;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows console login interface
     * @return true if login successful, false otherwise
     */
    public boolean showConsoleLogin() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         LOGIN TO SYSTEM                ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nDefault credentials: admin / admin123");
        System.out.println();

        int attempts = 0;

        while (attempts < maxAttempts) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (attemptLogin(username, password)) {
                System.out.println("\n✓ Login successful! Welcome, " + username + "!");
                return true;
            }

            attempts++;
            System.out.println("\n✗ Invalid credentials! " +
                    (maxAttempts - attempts) + " attempt(s) remaining.\n");
        }

        System.out.println("Maximum login attempts reached. Exiting...");
        return false;
    }

    /**
     * Attempts to validate login credentials
     * @param username User's username
     * @param password User's password
     * @return true if credentials are valid, false otherwise
     */
    private boolean attemptLogin(String username, String password) {
        if (username == null || password == null ||
                username.isEmpty() || password.isEmpty()) {
            return false;
        }

        return userManager.validateLogin(username, password);
    }

    /**
     * Displays login screen information (for GUI integration)
     */
    public void displayLoginScreen() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║    STUDENT MANAGEMENT SYSTEM LOGIN     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nPlease log in to access the system.");
        System.out.println("If you don't have an account, contact the administrator.");
    }
}