import java.io.*;
import java.util.*;

public class UserFileManager {
    private String userFilePath;
    private Map<String, String> users;
    
    public UserFileManager(String filePath) {
        this.userFilePath = filePath;
        this.users = new HashMap<>();
        
        createFileIfNotExists();
        loadUsers();
    }
    
    
    private boolean checkFileExists(boolean showWarning) {
        File file = new File(userFilePath);
        boolean exists = file.exists();
        
        if (!exists && showWarning) {
            System.err.println("WARNING: User file does not exist: " + userFilePath);
        }
        
        return exists;
    }
    
   
    private void createFileIfNotExists() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created directory: data/");
        }
        
        File file = new File(userFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new user file: " + userFilePath);
                // Initialize with default admin account
                initializeDefaultUsers();
            } catch (IOException e) {
                System.err.println("Error creating user file: " + e.getMessage());
            }
        }
    }
    
    
    private void initializeDefaultUsers() {
        users.put("admin", "admin123");
        saveUsers();
        System.out.println("Initialized default admin account (username: admin, password: admin123)");
    }
    
    
    public boolean validateLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        if (users.isEmpty()) {
            System.err.println("No users loaded. Please check users file.");
            return false;
        }
        
        return users.containsKey(username) && 
               users.get(username).equals(password);
    }
    
    
    private void loadUsers() {
        if (!checkFileExists(false)) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    users.put(username, password);
                } else {
                    System.err.println("Invalid user format at line " + lineNumber + ": " + line);
                }
            }
            
            System.out.println("Loaded " + users.size() + " user(s) from file.");
            
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    
    
    public boolean addUser(String username, String password) {
        if (username == null || password == null || 
            username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Username and password cannot be empty.");
            return false;
        }
        
        if (users.containsKey(username)) {
            System.err.println("User already exists: " + username);
            return false;
        }
        
        users.put(username, password);
        boolean saved = saveUsers();
        
        if (saved) {
            System.out.println("User added successfully: " + username);
        }
        
        return saved;
    }
    
    
    public boolean updateUserPassword(String username, String newPassword) {
        if (!users.containsKey(username)) {
            System.err.println("User not found: " + username);
            return false;
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            System.err.println("Password cannot be empty.");
            return false;
        }
        
        users.put(username, newPassword);
        boolean saved = saveUsers();
        
        if (saved) {
            System.out.println("Password updated successfully for user: " + username);
        }
        
        return saved;
    }
    
    
    public boolean removeUser(String username) {
        if (!users.containsKey(username)) {
            System.err.println("User not found: " + username);
            return false;
        }
        
        users.remove(username);
        boolean saved = saveUsers();
        
        if (saved) {
            System.out.println("User removed successfully: " + username);
        }
        
        return saved;
    }
    
    
    private boolean saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(userFilePath))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            return false;
        }
    }
    
    
    public Set<String> getAllUsernames() {
        return new HashSet<>(users.keySet());
    }
    
    
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    
    public int getUserCount() {
        return users.size();
    }
    
    
    public boolean fileExists() {
        return checkFileExists(false);
    }
}