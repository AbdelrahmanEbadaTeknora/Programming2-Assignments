package utils;

import database.JsonDatabaseManager;
import models.Instructor;
import models.Student;
import models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    private static User currentUser = null;
    private static JsonDatabaseManager dbManager = JsonDatabaseManager.getInstance();

    public static boolean signup(String username, String email, String password, String role) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return false;
        }

        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return false;
        }

        // FIXED: Use JsonDatabaseManager methods
        if (dbManager.emailExists(email)) {
            System.out.println("Email already exists.");
            return false;
        }

        String hashedPassword = hashPassword(password);
        String userId = String.valueOf(System.currentTimeMillis());

        User newUser;
        if (role.equalsIgnoreCase("student")) {
            newUser = new Student(userId, username, email, hashedPassword);
        } else {
            newUser = new Instructor(userId, username, email, hashedPassword);
        }

        // FIXED: Use saveUser method
        boolean success = dbManager.saveUser(newUser);

        if (success) {
            System.out.println("Signup successful!");
            return true;
        } else {
            System.out.println("Signup failed. User might already exist.");
            return false;
        }
    }

    public static User login(String email, String password) {
        // FIXED: Use getUserByEmail method
        User user = dbManager.getUserByEmail(email);

        if (user != null) {
            String hashedPassword = hashPassword(password);

            if (user.getPasswordHash().equals(hashedPassword)) {
                currentUser = user;
                System.out.println("Login successful!");
                return user;
            }
        }

        System.out.println("Incorrect email or password.");
        return null;
    }

    // Rest of the methods remain the same...
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