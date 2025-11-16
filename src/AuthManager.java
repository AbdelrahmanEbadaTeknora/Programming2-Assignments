import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthManager {

    private static User currentUser = null;


    public static boolean signup(String username, String email, String password, String role) {


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return false;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }


        List<User> users = JsonDatabaseManager.loadUsers();


        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Email already exists.");
                return false;
            }
        }


        String hashedPassword = hashPassword(password);


        String userId = String.valueOf(System.currentTimeMillis());

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, username, email, hashedPassword);
        } else {
            newUser = new Instructor(userId, username, email, hashedPassword);
        }


        JsonDatabaseManager.addUser(newUser);

        System.out.println("Signup successful!");
        return true;
    }



    public static User login(String email, String password) {

        List<User> users = JsonDatabaseManager.loadUsers();

        String hashedPassword = hashPassword(password);

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) &&
                    u.getPasswordHash().equals(hashedPassword)) {

                currentUser = u;
                System.out.println("Login successful!");
                return u;
            }
        }

        System.out.println("Incorrect email or password.");
        return null;
    }



    public static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }



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



    public static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() >= 5;
    }



    public static User getCurrentUser() {
        return currentUser;
    }
}
