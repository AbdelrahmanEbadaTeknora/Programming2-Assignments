import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthManager {

    private static User currentUser = null;   // Stores the logged-in user

    // --------------------------
    // SIGNUP
    // --------------------------
    public static boolean signup(String username, String email, String password, String role) {

        // Basic validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return false;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

        // Load all users from JSON (Member 4 handles this part)
        List<User> users = JsonDatabaseManager.loadUsers();

        // Check if email already exists
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Email already exists.");
                return false;
            }
        }

        // Hash password
        String hashedPassword = hashPassword(password);

        // Create new user
        String userId = String.valueOf(System.currentTimeMillis()); // simple unique ID

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, username, email, hashedPassword);
        } else {
            newUser = new Instructor(userId, username, email, hashedPassword);
        }

        // Save new user in JSON
        JsonDatabaseManager.addUser(newUser);

        System.out.println("Signup successful!");
        return true;
    }


    // --------------------------
    // LOGIN
    // --------------------------
    public static User login(String email, String password) {

        List<User> users = JsonDatabaseManager.loadUsers();

        String hashedPassword = hashPassword(password);

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                    u.getPasswordHash().equals(hashedPassword)) {

                currentUser = u;   // set current logged-in user
                System.out.println("Login successful!");
                return u;
            }
        }

        System.out.println("Incorrect email or password.");
        return null;
    }


    // --------------------------
    // LOGOUT
    // --------------------------
    public static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }


    // --------------------------
    // SHA-256 PASSWORD HASHING
    // --------------------------
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    // --------------------------
    // SIMPLE EMAIL VALIDATION
    // --------------------------
    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() >= 5;
    }


    // --------------------------
    // GET CURRENT USER
    // --------------------------
    public static User getCurrentUser() {
        return currentUser;
    }
}
