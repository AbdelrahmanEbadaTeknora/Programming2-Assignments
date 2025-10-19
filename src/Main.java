import AdminRoleFunctions.AdminRole;
import CustomerInteractionAndEmployeeRole.CustomerProduct;
import CustomerInteractionAndEmployeeRole.CustomerProductDatabase;
import CustomerInteractionAndEmployeeRole.EmployeeRole;
import EmployeeSystem.EmployeeUser;
import ProductManagement.Product;
import ProductManagement.ProductDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=================================================");
        System.out.println("   INVENTORY MANAGEMENT SYSTEM - TEST SUITE");
        System.out.println("=================================================\n");

        // Initialize databases and roles
        ProductDatabase productDB = new ProductDatabase("Products.txt");
        CustomerProductDatabase customerProductDB = new CustomerProductDatabase();
        
        productDB.readFromFile();
        
        AdminRole admin = new AdminRole();
        EmployeeRole employee = new EmployeeRole(productDB, customerProductDB);

        boolean running = true;

        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Test Admin Functions");
            System.out.println("2. Test Employee Functions");
            System.out.println("3. View All Data");
            System.out.println("4. Run Automated Tests");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    testAdminFunctions(admin, scanner);
                    break;
                case 2:
                    testEmployeeFunctions(employee, scanner);
                    break;
                case 3:
                    viewAllData(admin, employee);
                    break;
                case 4:
                    runAutomatedTests(admin, employee);
                    break;
                case 5:
                    System.out.println("\nSaving all data and exiting...");
                    admin.logout();
                    employee.logout();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
        System.out.println("System exited successfully.");
    }

    // ======================== ADMIN FUNCTIONS ========================
    private static void testAdminFunctions(AdminRole admin, Scanner scanner) {
        System.out.println("\n=== ADMIN MENU ===");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. View All Employees");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Employee ID: ");
                String empId = scanner.nextLine();
                System.out.print("Enter Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Address: ");
                String address = scanner.nextLine();
                System.out.print("Enter Phone Number: ");
                String phone = scanner.nextLine();
                
                admin.addEmployee(empId, name, email, address, phone);
                break;

            case 2:
                System.out.print("Enter Employee ID to remove: ");
                String removeId = scanner.nextLine();
                admin.removeEmployee(removeId);
                break;

            case 3:
                EmployeeUser[] employees = admin.getListOfEmployees();
                System.out.println("\n--- LIST OF EMPLOYEES ---");
                if (employees.length == 0) {
                    System.out.println("No employees found.");
                } else {
                    for (EmployeeUser emp : employees) {
                        System.out.println(emp.lineRepresentation());
                    }
                }
                break;

            case 4:
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ======================== EMPLOYEE FUNCTIONS ========================
    private static void testEmployeeFunctions(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n=== EMPLOYEE MENU ===");
        System.out.println("1. Add Product");
        System.out.println("2. View All Products");
        System.out.println("3. Purchase Product");
        System.out.println("4. Return Product");
        System.out.println("5. Apply Payment");
        System.out.println("6. View All Purchases");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Product ID: ");
                String prodId = scanner.nextLine();
                System.out.print("Enter Product Name: ");
                String prodName = scanner.nextLine();
                System.out.print("Enter Manufacturer Name: ");
                String manufacturer = scanner.nextLine();
                System.out.print("Enter Supplier Name: ");
                String supplier = scanner.nextLine();
                System.out.print("Enter Quantity: ");
                int quantity = scanner.nextInt();
                System.out.print("Enter Price: ");
                float price = scanner.nextFloat();
                scanner.nextLine();
                
                employee.addProduct(prodId, prodName, manufacturer, supplier, quantity, price);
                break;

            case 2:
                ArrayList<Product> products = employee.getListOfProducts();
                System.out.println("\n--- LIST OF PRODUCTS ---");
                if (products.isEmpty()) {
                    System.out.println("No products found.");
                } else {
                    for (Product p : products) {
                        System.out.println(p.lineRepresentation());
                    }
                }
                break;

            case 3:
                System.out.print("Enter Customer SSN: ");
                String ssn = scanner.nextLine();
                System.out.print("Enter Product ID: ");
                String productId = scanner.nextLine();
                System.out.print("Enter Purchase Date (YYYY-MM-DD): ");
                String dateStr = scanner.nextLine();
                LocalDate purchaseDate = LocalDate.parse(dateStr);
                
                employee.purchaseProduct(ssn, productId, purchaseDate);
                break;

            case 4:
                System.out.print("Enter Customer SSN: ");
                String returnSSN = scanner.nextLine();
                System.out.print("Enter Product ID: ");
                String returnProdId = scanner.nextLine();
                System.out.print("Enter Return Date (YYYY-MM-DD): ");
                String returnDateStr = scanner.nextLine();
                LocalDate returnDate = LocalDate.parse(returnDateStr);
                
                employee.returnProduct(returnSSN, returnProdId, returnDate);
                break;

            case 5:
                System.out.print("Enter Customer SSN: ");
                String paySSN = scanner.nextLine();
                System.out.print("Enter Product ID: ");
                String payProdId = scanner.nextLine();
                System.out.print("Enter Purchase Date (YYYY-MM-DD): ");
                String payDateStr = scanner.nextLine();
                LocalDate payDate = LocalDate.parse(payDateStr);
                
                employee.applyPayment(paySSN, payProdId, payDate);
                break;

            case 6:
                ArrayList<CustomerProduct> purchases = employee.getListOfPurchasingOperations();
                System.out.println("\n--- LIST OF PURCHASES ---");
                if (purchases.isEmpty()) {
                    System.out.println("No purchases found.");
                } else {
                    for (CustomerProduct cp : purchases) {
                        System.out.println(cp.lineRepresentation());
                    }
                }
                break;

            case 7:
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ======================== VIEW ALL DATA ========================
    private static void viewAllData(AdminRole admin, EmployeeRole employee) {
        System.out.println("\n=== ALL SYSTEM DATA ===");
        
        System.out.println("\n--- EMPLOYEES ---");
        EmployeeUser[] employees = admin.getListOfEmployees();
        if (employees.length == 0) {
            System.out.println("No employees in system.");
        } else {
            for (EmployeeUser emp : employees) {
                System.out.println(emp.lineRepresentation());
            }
        }

        System.out.println("\n--- PRODUCTS ---");
        ArrayList<Product> products = employee.getListOfProducts();
        if (products.isEmpty()) {
            System.out.println("No products in system.");
        } else {
            for (Product p : products) {
                System.out.println(p.lineRepresentation());
            }
        }

        System.out.println("\n--- CUSTOMER PURCHASES ---");
        ArrayList<CustomerProduct> purchases = employee.getListOfPurchasingOperations();
        if (purchases.isEmpty()) {
            System.out.println("No purchases in system.");
        } else {
            for (CustomerProduct cp : purchases) {
                System.out.println(cp.lineRepresentation());
            }
        }
    }

    // ======================== AUTOMATED TESTS ========================
    private static void runAutomatedTests(AdminRole admin, EmployeeRole employee) {
        System.out.println("\n=== RUNNING AUTOMATED TESTS ===\n");

        // Test 1: Admin adds employees
        System.out.println("Test 1: Adding Employees");
        admin.addEmployee("E1001", "John Doe", "john@email.com", "Cairo", "01012345678");
        admin.addEmployee("E1002", "Jane Smith", "jane@email.com", "Alexandria", "01098765432");
        admin.addEmployee("E1001", "Duplicate", "dup@email.com", "Giza", "01011111111"); // Should fail
        System.out.println();

        // Test 2: Admin views employees
        System.out.println("Test 2: Viewing Employees");
        EmployeeUser[] employees = admin.getListOfEmployees();
        System.out.println("Total employees: " + employees.length);
        System.out.println();

        // Test 3: Employee adds products
        System.out.println("Test 3: Adding Products");
        employee.addProduct("P1001", "Laptop", "Dell", "TechSupplier", 5, 3000.0f);
        employee.addProduct("P1002", "Mouse", "Logitech", "TechSupplier", 20, 150.0f);
        employee.addProduct("P1003", "Keyboard", "Razer", "GamingSupplier", 15, 500.0f);
        System.out.println();

        // Test 4: Customer purchases
        System.out.println("Test 4: Recording Purchases");
        LocalDate today = LocalDate.now();
        employee.purchaseProduct("1234567890", "P1001", today);
        employee.purchaseProduct("0987654321", "P1002", today.minusDays(5));
        System.out.println();

        // Test 5: Apply payment
        System.out.println("Test 5: Applying Payment");
        employee.applyPayment("1234567890", "P1001", today);
        System.out.println();

        // Test 6: Return product (valid)
        System.out.println("Test 6: Returning Product (Within 14 days)");
        employee.returnProduct("0987654321", "P1002", today.minusDays(3));
        System.out.println();

        // Test 7: Return product (invalid - too late)
        System.out.println("Test 7: Returning Product (After 14 days) - Should Fail");
        employee.purchaseProduct("1111111111", "P1003", today.minusDays(20));
        employee.returnProduct("1111111111", "P1003", today);
        System.out.println();

        // Test 8: Remove employee
        System.out.println("Test 8: Removing Employee");
        admin.removeEmployee("E1002");
        System.out.println();

        // Test 9: View final state
        System.out.println("Test 9: Final System State");
        viewAllData(admin, employee);

        System.out.println("\n=== ALL AUTOMATED TESTS COMPLETED ===\n");
    }
}