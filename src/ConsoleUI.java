import java.util.*;

public class ConsoleUI {
    private StudentManager manager;
    private Scanner scanner;
    private boolean running;
    
    public ConsoleUI(StudentManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   STUDENT MANAGEMENT SYSTEM - CONSOLE  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        while (running) {
            showMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleViewStudents();
                    break;
                case 3:
                    handleSearchStudent();
                    break;
                case 4:
                    handleUpdateStudent();
                    break;
                case 5:
                    handleDeleteStudent();
                    break;
                case 6:
                    handleStatistics();
                    break;
                case 7:
                    running = false;
                    System.out.println("\n✓ Thank you for using the system. Goodbye!");
                    break;
                default:
                    System.out.println("\n✗ Invalid choice! Please try again.\n");
            }
            
            if (running && choice >= 1 && choice <= 6) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void showMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("                    MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. View Statistics");
        System.out.println("7. Exit");
        System.out.println("=".repeat(50));
    }
    
    private void handleAddStudent() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           ADD NEW STUDENT              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Get student details
        String name = getStringInput("Enter student name: ");
        if (!Validator.isValidName(name)) {
            System.out.println("\n✗ " + Validator.getErrorMessage("name"));
            return;
        }
        
        int age = getIntInput("Enter age: ");
        if (!Validator.isValidAge(age)) {
            System.out.println("\n✗ " + Validator.getErrorMessage("age"));
            return;
        }
        
        String gender = getStringInput("Enter gender (Male/Female): ");
        if (!Validator.isValidGender(gender)) {
            System.out.println("\n✗ " + Validator.getErrorMessage("gender"));
            return;
        }
        
        String department = getStringInput("Enter department: ");
        if (!Validator.isValidDepartment(department)) {
            System.out.println("\n✗ " + Validator.getErrorMessage("department"));
            return;
        }
        
        double gpa = getDoubleInput("Enter GPA (0.0-4.0): ");
        if (!Validator.isValidGPA(gpa)) {
            System.out.println("\n✗ " + Validator.getErrorMessage("gpa"));
            return;
        }
        
        // Create student object (ID will be auto-generated)
        student newStudent = new student(name, age, gender, "", department, gpa);
        
        if (manager.addStudent(newStudent)) {
            System.out.println("\n✓ Student added successfully! ID: " + newStudent.getStudentId());
        } else {
            System.out.println("\n✗ Failed to add student!");
        }
    }
    
    private void handleViewStudents() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          ALL STUDENTS LIST             ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        List<student> students = manager.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students found in the system.");
            return;
        }
        
        displayStudentTable(students);
    }
    
    private void handleSearchStudent() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           SEARCH STUDENT               ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("Search by:");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. Department");
        
        int choice = getIntInput("Enter choice: ");
        
        switch (choice) {
            case 1:
                int id = getIntInput("Enter student ID: ");
                student found = manager.searchById(id);
                if (found != null) {
                    List<student> result = new ArrayList<>();
                    result.add(found);
                    displayStudentTable(result);
                } else {
                    System.out.println("\n✗ Student not found with ID: " + id);
                }
                break;
                
            case 2:
                String name = getStringInput("Enter student name (partial match): ");
                List<student> byName = manager.searchByName(name);
                if (!byName.isEmpty()) {
                    displayStudentTable(byName);
                } else {
                    System.out.println("\n✗ No students found with name: " + name);
                }
                break;
                
            case 3:
                String dept = getStringInput("Enter department: ");
                List<student> byDept = manager.searchByDepartment(dept);
                if (!byDept.isEmpty()) {
                    displayStudentTable(byDept);
                } else {
                    System.out.println("\n✗ No students found in department: " + dept);
                }
                break;
                
            default:
                System.out.println("\n✗ Invalid choice!");
        }
    }
    
    private void handleUpdateStudent() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          UPDATE STUDENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        int id = getIntInput("Enter student ID to update: ");
        student existing = manager.searchById(id);
        
        if (existing == null) {
            System.out.println("\n✗ Student not found with ID: " + id);
            return;
        }
        
        System.out.println("\nCurrent Details:");
        List<student> current = new ArrayList<>();
        current.add(existing);
        displayStudentTable(current);
        
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        // Get new details
        String name = getStringInputOptional("Enter new name [" + existing.getName() + "]: ");
        if (name.isEmpty()) name = existing.getName();
        
        String ageStr = getStringInputOptional("Enter new age [" + existing.getAge() + "]: ");
        int age = ageStr.isEmpty() ? existing.getAge() : Integer.parseInt(ageStr);
        
        String gender = getStringInputOptional("Enter new gender [" + existing.getGender() + "]: ");
        if (gender.isEmpty()) gender = existing.getGender();
        
        String dept = getStringInputOptional("Enter new department [" + existing.getDepartment() + "]: ");
        if (dept.isEmpty()) dept = existing.getDepartment();
        
        String gpaStr = getStringInputOptional("Enter new GPA [" + existing.getGpa() + "]: ");
        double gpa = gpaStr.isEmpty() ? existing.getGpa() : Double.parseDouble(gpaStr);
        
        // Validate
        if (!Validator.isValidName(name) || !Validator.isValidAge(age) || 
            !Validator.isValidGender(gender) || !Validator.isValidDepartment(dept) || 
            !Validator.isValidGPA(gpa)) {
            System.out.println("\n✗ Invalid input! Update cancelled.");
            return;
        }
        
        student updatedStudent = new student(name, age, gender, "", dept, gpa);
        
        if (manager.updateStudent(id, updatedStudent)) {
            System.out.println("\n✓ Student updated successfully!");
        } else {
            System.out.println("\n✗ Failed to update student!");
        }
    }
    
    private void handleDeleteStudent() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          DELETE STUDENT                ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        int id = getIntInput("Enter student ID to delete: ");
        student existing = manager.searchById(id);
        
        if (existing == null) {
            System.out.println("\n✗ Student not found with ID: " + id);
            return;
        }
        
        System.out.println("\nStudent to be deleted:");
        List<student> toDelete = new ArrayList<>();
        toDelete.add(existing);
        displayStudentTable(toDelete);
        
        String confirm = getStringInput("\nAre you sure you want to delete this student? (yes/no): ");
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (manager.deleteStudent(id)) {
                System.out.println("\n✓ Student deleted successfully!");
            } else {
                System.out.println("\n✗ Failed to delete student!");
            }
        } else {
            System.out.println("\n✓ Deletion cancelled.");
        }
    }
    
    private void handleStatistics() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          STUDENT STATISTICS            ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        List<student> students = manager.getAllStudents();
        
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        
        // Calculate statistics
        double avgGPA = StatisticsManager.calculateAverageGPA(students);
        student highest = StatisticsManager.getHighestGPA(students);
        student lowest = StatisticsManager.getLowestGPA(students);
        Map<String, Integer> deptCount = StatisticsManager.countByDepartment(students);
        
        System.out.println("Total Students: " + students.size());
        System.out.println("Average GPA: " + String.format("%.2f", avgGPA));
        System.out.println("\nHighest GPA: " + highest.getName() + " (" + highest.getGpa() + ")");
        System.out.println("Lowest GPA: " + lowest.getName() + " (" + lowest.getGpa() + ")");
        
        System.out.println("\nStudents by Department:");
        for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " students");
        }
    }
    
    private void displayStudentTable(List<student> students) {
        System.out.println("\n" + "=".repeat(100));
        System.out.printf("%-6s %-20s %-5s %-8s %-20s %-6s\n",
            "ID", "Name", "Age", "Gender", "Department", "GPA");
        System.out.println("=".repeat(100));
        
        for (student s : students) {
            System.out.printf("%-6s %-20s %-5d %-8s %-20s %.2f\n",
                s.getStudentId(),
                truncate(s.getName(), 20),
                s.getAge(),
                s.getGender(),
                truncate(s.getDepartment(), 20),
                s.getGpa());
        }
        System.out.println("=".repeat(100));
        System.out.println("Total: " + students.size() + " student(s)");
    }
    
    private String truncate(String str, int length) {
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private String getStringInputOptional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid number! Please try again.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("✗ Invalid number! Please try again.");
            }
        }
    }
}