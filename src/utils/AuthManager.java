package utils;

import database.JsonDatabaseManager;
import models.*;
import java.security.MessageDigest;
import java.util.UUID;

public class AuthManager {
    private static JsonDatabaseManager dbManager = JsonDatabaseManager.getInstance();

    // Hash password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Login user
    public static User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        User user = dbManager.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword != null && hashedPassword.equals(user.getPasswordHash())) {
            return user;
        }

        return null;
    }

    // Signup user - handles student, instructor, and admin roles
    public static boolean signup(String username, String email, String password, String role) {
        // Validate inputs
        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null || role.isEmpty()) {
            return false;
        }

        // Check if email already exists
        if (dbManager.emailExists(email)) {
            return false;
        }

        // Hash password
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            return false;
        }

        // Generate unique user ID
        String userId = generateUserId(role);

        // Create user based on role
        User newUser = null;

        if (role.equals("student")) {
            newUser = new Student(userId, username, email, hashedPassword);
        } else if (role.equals("instructor")) {
            newUser = new Instructor(userId, username, email, hashedPassword);
        } else if (role.equals("admin")) {
            newUser = new Admin(userId, username, email, hashedPassword);
        } else {
            return false; // Invalid role
        }

        // Save user to database
        return dbManager.saveUser(newUser);
    }

    // Generate unique user ID based on role
    private static String generateUserId(String role) {
        String prefix;
        switch (role) {
            case "student": prefix = "S"; break;
            case "instructor": prefix = "I"; break;
            case "admin": prefix = "A"; break;
            default: prefix = "U";
        }

        String uniquePart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + uniquePart;
    }

    // Verify password
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }
}