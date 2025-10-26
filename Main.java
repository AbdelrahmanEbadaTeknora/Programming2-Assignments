import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Starting Student Management System Test ===\n");

            // Initialize FileManager (data/students.csv)
            FileManager fileManager = new FileManager("data/students.csv");

            // Initialize StudentManager
            StudentManager studentManager = new StudentManager(fileManager);

            // ---------- 1. Add Students ----------
            System.out.println("\n--- Adding Students ---");
            student s1 = new student("Alice", 20, "Female", "", "Computer", 3.8);
            student s2 = new student("Bob", 22, "Male", "", "Electrical", 3.2);
            student s3 = new student("Charlie", 21, "Male", "", "Computer", 2.9);

            studentManager.addStudent(s1);
            studentManager.addStudent(s2);
            studentManager.addStudent(s3);

            // ---------- 2. Display All Students ----------
            System.out.println("\n--- All Students ---");
            for (student s : studentManager.getAllStudents()) {
                System.out.println(s);
            }

            // ---------- 3. Search Operations ----------
            System.out.println("\n--- Search by Name 'Alice' ---");
            List<student> searchByName = studentManager.searchByName("Alice");
            searchByName.forEach(System.out::println);

            System.out.println("\n--- Search by Department 'Computer' ---");
            List<student> searchByDept = studentManager.searchByDepartment("Computer");
            searchByDept.forEach(System.out::println);

            // ---------- 4. Update a Student ----------
            System.out.println("\n--- Updating Student with ID 1 ---");
            student updated = new student("Alice Johnson", 20, "Female", "", "Computer", 3.9);
            boolean updatedOk = studentManager.updateStudent(1, updated);
            System.out.println("Update status: " + updatedOk);

            // ---------- 5. Delete a Student ----------
            System.out.println("\n--- Deleting Student with ID 2 ---");
            boolean deleted = studentManager.deleteStudent(2);
            System.out.println("Delete status: " + deleted);

            // ---------- 6. Statistics ----------
            System.out.println("\n--- Statistics ---");
            List<student> allStudents = studentManager.getAllStudents();
            System.out.println("Average GPA: " + StatisticsManager.calculateAverageGPA(allStudents));
            System.out.println("Highest GPA Student: " + StatisticsManager.getHighestGPA(allStudents));
            System.out.println("Lowest GPA Student: " + StatisticsManager.getLowestGPA(allStudents));

            Map<String, Integer> countByDept = StatisticsManager.countByDepartment(allStudents);
            System.out.println("Students per department: " + countByDept);

            // ---------- 7. Sorting ----------
            System.out.println("\n--- Sort by GPA (descending) ---");
            List<student> sorted = StatisticsManager.sortByGPA(new ArrayList<>(allStudents), false);
            sorted.forEach(System.out::println);

            // ---------- 8. Backup & Delete File ----------
            System.out.println("\n--- Creating Backup ---");
            fileManager.backupData();

            System.out.println("\n--- File Exists: " + fileManager.fileExists() + " ---");
            // Uncomment the next line to test deletion:
            // fileManager.deleteData();

            // ---------- 9. UserFileManager Testing ----------
            System.out.println("\n=== Testing User File Manager ===");
            UserFileManager userManager = new UserFileManager("data/users.csv");

            userManager.addUser("john", "12345");
            userManager.addUser("mary", "pass");
            userManager.updateUserPassword("john", "newpass");
            System.out.println("All users: " + userManager.getAllUsernames());
            System.out.println("User count: " + userManager.getUserCount());

            boolean loginOK = userManager.validateLogin("john", "newpass");
            System.out.println("Login (john/newpass): " + loginOK);

            userManager.removeUser("mary");

            System.out.println("\n=== Test Completed Successfully ===");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
