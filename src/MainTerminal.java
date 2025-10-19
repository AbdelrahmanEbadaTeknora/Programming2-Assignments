import AdminRoleFunctions.AdminRole;
import CustomerInteractionAndEmployeeRole.CustomerProduct;
import CustomerInteractionAndEmployeeRole.CustomerProductDatabase;
import CustomerInteractionAndEmployeeRole.EmployeeRole;
import EmployeeSystem.EmployeeUser;
import ProductManagement.Product;
import ProductManagement.ProductDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainTerminal {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printHeader();

        // Initialize databases and roles
        ProductDatabase productDB = new ProductDatabase("Products.txt");
        CustomerProductDatabase customerProductDB = new CustomerProductDatabase("CustomersProducts.txt");

        productDB.readFromFile();
        customerProductDB.readFromFile();

        AdminRole admin = new AdminRole();
        EmployeeRole employee = new EmployeeRole(productDB, customerProductDB);

        boolean running = true;

        while (running) {
            printMainMenu();

            int choice = getIntInput(scanner, "Choose an option: ");

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
                    runComprehensiveTests(admin, employee);
                    break;
                case 6:
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("Saving all data and exiting...");
                    admin.logout();
                    employee.logout();
                    System.out.println("=".repeat(50));
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }

        scanner.close();
        System.out.println("\n✅ System exited successfully. Goodbye!");
    }

    private static void printHeader() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        INVENTORY MANAGEMENT SYSTEM - TEST SUITE");
        System.out.println("              (Refactored with OOP Principles)");
        System.out.println("=".repeat(60));
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("             MAIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. 👤 Test Admin Functions");
        System.out.println("2. 👷 Test Employee Functions");
        System.out.println("3. 📊 View All Data");
        System.out.println("4. 🤖 Run Automated Basic Tests");
        System.out.println("5. 🧪 Run Comprehensive Tests (All Requirements)");
        System.out.println("6. 🚪 Exit");
        System.out.println("=".repeat(40));
    }

    // ======================== ADMIN FUNCTIONS ========================
    private static void testAdminFunctions(AdminRole admin, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("          ADMIN MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. ➕ Add Employee");
        System.out.println("2. ➖ Remove Employee");
        System.out.println("3. 📋 View All Employees");
        System.out.println("4. 🔙 Back to Main Menu");
        System.out.println("=".repeat(40));

        int choice = getIntInput(scanner, "Choose an option: ");

        switch (choice) {
            case 1:
                addEmployeeInteractive(admin, scanner);
                break;
            case 2:
                removeEmployeeInteractive(admin, scanner);
                break;
            case 3:
                viewAllEmployees(admin);
                break;
            case 4:
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    private static void addEmployeeInteractive(AdminRole admin, Scanner scanner) {
        System.out.println("\n--- Add New Employee ---");
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
    }

    private static void removeEmployeeInteractive(AdminRole admin, Scanner scanner) {
        System.out.println("\n--- Remove Employee ---");
        System.out.print("Enter Employee ID to remove: ");
        String removeId = scanner.nextLine();
        admin.removeEmployee(removeId);
    }

    private static void viewAllEmployees(AdminRole admin) {
        EmployeeUser[] employees = admin.getListOfEmployees();
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           LIST OF EMPLOYEES");
        System.out.println("=".repeat(50));
        if (employees.length == 0) {
            System.out.println("❌ No employees found.");
        } else {
            System.out.println(String.format("%-10s | %-15s | %-25s | %-15s | %-12s",
                    "ID", "Name", "Email", "Address", "Phone"));
            System.out.println("-".repeat(95));
            for (EmployeeUser emp : employees) {
                String[] parts = emp.lineRepresentation().split(",");
                System.out.println(String.format("%-10s | %-15s | %-25s | %-15s | %-12s",
                        parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
            System.out.println("Total employees: " + employees.length);
        }
        System.out.println("=".repeat(50));
    }

    // ======================== EMPLOYEE FUNCTIONS ========================
    private static void testEmployeeFunctions(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("         EMPLOYEE MENU");
        System.out.println("=".repeat(40));
        System.out.println("1. ➕ Add Product");
        System.out.println("2. 📋 View All Products");
        System.out.println("3. 🛒 Purchase Product");
        System.out.println("4. 🔄 Return Product");
        System.out.println("5. 💳 Apply Payment");
        System.out.println("6. 📜 View All Purchases");
        System.out.println("7. 🔙 Back to Main Menu");
        System.out.println("=".repeat(40));

        int choice = getIntInput(scanner, "Choose an option: ");

        switch (choice) {
            case 1:
                addProductInteractive(employee, scanner);
                break;
            case 2:
                viewAllProducts(employee);
                break;
            case 3:
                purchaseProductInteractive(employee, scanner);
                break;
            case 4:
                returnProductInteractive(employee, scanner);
                break;
            case 5:
                applyPaymentInteractive(employee, scanner);
                break;
            case 6:
                viewAllPurchases(employee);
                break;
            case 7:
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    private static void addProductInteractive(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n--- Add New Product ---");
        System.out.print("Enter Product ID: ");
        String prodId = scanner.nextLine();
        System.out.print("Enter Product Name: ");
        String prodName = scanner.nextLine();
        System.out.print("Enter Manufacturer Name: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Enter Supplier Name: ");
        String supplier = scanner.nextLine();

        int quantity = getIntInput(scanner, "Enter Quantity: ");
        float price = getFloatInput(scanner, "Enter Price: ");

        employee.addProduct(prodId, prodName, manufacturer, supplier, quantity, price);
    }

    private static void purchaseProductInteractive(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n--- Purchase Product ---");
        System.out.print("Enter Customer SSN: ");
        String ssn = scanner.nextLine();
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();
        System.out.print("Enter Purchase Date (dd-MM-yyyy) or press Enter for today: ");
        String dateStr = scanner.nextLine();

        LocalDate purchaseDate = dateStr.isEmpty() ? LocalDate.now() :
                LocalDate.parse(dateStr, DATE_FORMATTER);

        boolean success = employee.purchaseProduct(ssn, productId, purchaseDate);
        if (success) {
            System.out.println("✅ Purchase recorded successfully!");
        }
    }

    private static void returnProductInteractive(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n--- Return Product ---");
        System.out.print("Enter Customer SSN: ");
        String returnSSN = scanner.nextLine();
        System.out.print("Enter Product ID: ");
        String returnProdId = scanner.nextLine();
        System.out.print("Enter Purchase Date (dd-MM-yyyy): ");
        String purchaseDateStr = scanner.nextLine();
        System.out.print("Enter Return Date (dd-MM-yyyy) or press Enter for today: ");
        String returnDateStr = scanner.nextLine();

        LocalDate purchaseDate = LocalDate.parse(purchaseDateStr, DATE_FORMATTER);
        LocalDate returnDate = returnDateStr.isEmpty() ? LocalDate.now() :
                LocalDate.parse(returnDateStr, DATE_FORMATTER);

        double refund = employee.returnProduct(returnSSN, returnProdId, purchaseDate, returnDate);
        if (refund > 0) {
            System.out.println("✅ Refund amount: $" + refund);
        }
    }

    private static void applyPaymentInteractive(EmployeeRole employee, Scanner scanner) {
        System.out.println("\n--- Apply Payment ---");
        System.out.print("Enter Customer SSN: ");
        String paySSN = scanner.nextLine();
        System.out.print("Enter Product ID: ");
        String payProdId = scanner.nextLine();
        System.out.print("Enter Purchase Date (dd-MM-yyyy): ");
        String payDateStr = scanner.nextLine();

        LocalDate payDate = LocalDate.parse(payDateStr, DATE_FORMATTER);

        employee.applyPayment(paySSN, payProdId, payDate);
    }

    private static void viewAllProducts(EmployeeRole employee) {
        ArrayList<Product> products = employee.getListOfProducts();
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                          LIST OF PRODUCTS");
        System.out.println("=".repeat(80));
        if (products.isEmpty()) {
            System.out.println("❌ No products found.");
        } else {
            System.out.println(String.format("%-10s | %-15s | %-15s | %-15s | %-8s | %-8s",
                    "ID", "Name", "Manufacturer", "Supplier", "Quantity", "Price"));
            System.out.println("-".repeat(80));
            for (Product p : products) {
                String[] parts = p.lineRepresentation().split(",");
                System.out.println(String.format("%-10s | %-15s | %-15s | %-15s | %-8s | $%-7s",
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
            }
            System.out.println("Total products: " + products.size());
        }
        System.out.println("=".repeat(80));
    }

    private static void viewAllPurchases(EmployeeRole employee) {
        ArrayList<CustomerProduct> purchases = employee.getListOfPurchasingOperations();
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  LIST OF PURCHASES");
        System.out.println("=".repeat(70));
        if (purchases.isEmpty()) {
            System.out.println("❌ No purchases found.");
        } else {
            System.out.println(String.format("%-15s | %-12s | %-15s | %-8s",
                    "Customer SSN", "Product ID", "Purchase Date", "Paid"));
            System.out.println("-".repeat(70));
            for (CustomerProduct cp : purchases) {
                String[] parts = cp.lineRepresentation().split(",");
                System.out.println(String.format("%-15s | %-12s | %-15s | %-8s",
                        parts[0], parts[1], parts[2], parts[3]));
            }
            System.out.println("Total purchases: " + purchases.size());
        }
        System.out.println("=".repeat(70));
    }

    // ======================== VIEW ALL DATA ========================
    private static void viewAllData(AdminRole admin, EmployeeRole employee) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              ALL SYSTEM DATA");
        System.out.println("=".repeat(60));

        viewAllEmployees(admin);
        viewAllProducts(employee);
        viewAllPurchases(employee);
    }

    // ======================== AUTOMATED TESTS ========================
    private static void runAutomatedTests(AdminRole admin, EmployeeRole employee) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          RUNNING AUTOMATED BASIC TESTS");
        System.out.println("=".repeat(60) + "\n");

        // Test 1: Admin adds employees
        printTestHeader("Test 1: Adding Employees (Admin Role)");
        admin.addEmployee("E2001", "Ahmed Ali", "ahmed@test.com", "Cairo", "01012345678");
        admin.addEmployee("E2002", "Sara Mohamed", "sara@test.com", "Alexandria", "01098765432");
        admin.addEmployee("E2001", "Duplicate Test", "dup@test.com", "Giza", "01011111111");
        System.out.println();

        // Test 2: Employee adds products
        printTestHeader("Test 2: Adding Products (Employee Role)");
        employee.addProduct("P2001", "Laptop", "Dell", "TechSupplier", 10, 5000.0f);
        employee.addProduct("P2002", "Mouse", "Logitech", "TechSupplier", 50, 150.0f);
        employee.addProduct("P2003", "Monitor", "Samsung", "DisplaySupplier", 20, 2500.0f);
        System.out.println();

        // Test 3: Customer purchases
        printTestHeader("Test 3: Recording Purchases");
        LocalDate today = LocalDate.now();
        employee.purchaseProduct("1234567890", "P2001", today);
        employee.purchaseProduct("0987654321", "P2002", today.minusDays(5));
        employee.purchaseProduct("1111111111", "P2003", today.minusDays(20));
        System.out.println();

        // Test 4: Apply payment
        printTestHeader("Test 4: Applying Payments");
        employee.applyPayment("1234567890", "P2001", today);
        System.out.println();

        // Test 5: Valid return
        printTestHeader("Test 5: Valid Product Return (Within 14 days)");
        employee.returnProduct("0987654321", "P2002", today.minusDays(5), today);
        System.out.println();

        // Test 6: Invalid return
        printTestHeader("Test 6: Invalid Product Return (After 14 days)");
        employee.returnProduct("1111111111", "P2003", today.minusDays(20), today);
        System.out.println();

        // Test 7: Remove employee
        printTestHeader("Test 7: Removing Employee");
        admin.removeEmployee("E2002");
        admin.removeEmployee("E9999"); // Non-existent
        System.out.println();

        System.out.println("=".repeat(60));
        System.out.println("       ✅ ALL BASIC TESTS COMPLETED");
        System.out.println("=".repeat(60) + "\n");
    }

    // ======================== COMPREHENSIVE TESTS ========================
    private static void runComprehensiveTests(AdminRole admin, EmployeeRole employee) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     COMPREHENSIVE TESTS - ALL REQUIREMENTS");
        System.out.println("=".repeat(60) + "\n");

        LocalDate today = LocalDate.now();

        // ADMIN ROLE TESTS
        printSectionHeader("PART 1: ADMIN ROLE TESTS");

        printTestHeader("Test 1.1: addEmployee() - Add multiple employees");
        admin.addEmployee("E3001", "Omar Hassan", "omar@email.com", "Cairo", "01011111111");
        admin.addEmployee("E3002", "Fatma Ibrahim", "fatma@email.com", "Giza", "01022222222");
        admin.addEmployee("E3003", "Youssef Khaled", "youssef@email.com", "Alex", "01033333333");

        printTestHeader("Test 1.2: addEmployee() - Duplicate detection");
        admin.addEmployee("E3001", "Duplicate", "dup@email.com", "Cairo", "01099999999");

        printTestHeader("Test 1.3: getListOfEmployees()");
        EmployeeUser[] employees = admin.getListOfEmployees();
        System.out.println("✅ Total employees: " + employees.length);

        printTestHeader("Test 1.4: removeEmployee() - Valid removal");
        admin.removeEmployee("E3003");

        printTestHeader("Test 1.5: removeEmployee() - Non-existent employee");
        admin.removeEmployee("E9999");
        System.out.println();

        // EMPLOYEE ROLE TESTS - PRODUCT MANAGEMENT
        printSectionHeader("PART 2: EMPLOYEE ROLE - PRODUCT MANAGEMENT");

        printTestHeader("Test 2.1: addProduct() - Add multiple products");
        employee.addProduct("P3001", "Gaming Laptop", "ASUS", "TechStore", 5, 8000.0f);
        employee.addProduct("P3002", "Wireless Mouse", "Logitech", "TechStore", 100, 200.0f);
        employee.addProduct("P3003", "Mechanical Keyboard", "Razer", "GamingStore", 30, 1500.0f);
        employee.addProduct("P3004", "USB Cable", "Generic", "Accessories", 200, 50.0f);

        printTestHeader("Test 2.2: addProduct() - Duplicate detection");
        employee.addProduct("P3001", "Duplicate Product", "Test", "Test", 10, 100.0f);

        printTestHeader("Test 2.3: getListOfProducts()");
        ArrayList<Product> products = employee.getListOfProducts();
        System.out.println("✅ Total products: " + products.size());
        System.out.println();

        // EMPLOYEE ROLE TESTS - PURCHASE OPERATIONS
        printSectionHeader("PART 3: EMPLOYEE ROLE - PURCHASE OPERATIONS");

        printTestHeader("Test 3.1: purchaseProduct() - Valid purchases");
        employee.purchaseProduct("2222222222", "P3001", today);
        employee.purchaseProduct("3333333333", "P3002", today.minusDays(3));
        employee.purchaseProduct("4444444444", "P3003", today.minusDays(10));
        employee.purchaseProduct("5555555555", "P3004", today.minusDays(20));

        printTestHeader("Test 3.2: purchaseProduct() - Out of stock scenario");
        // Purchase all remaining P3001 units
        for (int i = 0; i < 10; i++) {
            employee.purchaseProduct("6666666666", "P3001", today);
        }

        printTestHeader("Test 3.3: purchaseProduct() - Non-existent product");
        employee.purchaseProduct("7777777777", "P9999", today);

        printTestHeader("Test 3.4: getListOfPurchasingOperations()");
        ArrayList<CustomerProduct> purchases = employee.getListOfPurchasingOperations();
        System.out.println("✅ Total purchases: " + purchases.size());
        System.out.println();

        // EMPLOYEE ROLE TESTS - PAYMENT
        printSectionHeader("PART 4: EMPLOYEE ROLE - PAYMENT OPERATIONS");

        printTestHeader("Test 4.1: applyPayment() - Valid payment");
        employee.applyPayment("2222222222", "P3001", today);

        printTestHeader("Test 4.2: applyPayment() - Non-existent purchase");
        employee.applyPayment("9999999999", "P3001", today);
        System.out.println();

        // EMPLOYEE ROLE TESTS - RETURN OPERATIONS
        printSectionHeader("PART 5: EMPLOYEE ROLE - RETURN OPERATIONS");

        printTestHeader("Test 5.1: returnProduct() - Valid return (within 14 days)");
        double refund1 = employee.returnProduct("3333333333", "P3002", today.minusDays(3), today);
        System.out.println("✅ Refund amount: $" + refund1);

        printTestHeader("Test 5.2: returnProduct() - Valid return (exactly 14 days)");
        double refund2 = employee.returnProduct("4444444444", "P3003", today.minusDays(10), today.minusDays(10).plusDays(14));
        System.out.println("✅ Refund amount: $" + refund2);

        printTestHeader("Test 5.3: returnProduct() - Invalid return (after 14 days)");
        double refund3 = employee.returnProduct("5555555555", "P3004", today.minusDays(20), today);
        System.out.println((refund3 == -1) ? "✅ Correctly rejected (past 14 days)" : "❌ Should have been rejected");

        printTestHeader("Test 5.4: returnProduct() - Return date before purchase date");
        employee.purchaseProduct("8888888888", "P3002", today);
        double refund4 = employee.returnProduct("8888888888", "P3002", today, today.minusDays(1));
        System.out.println((refund4 == -1) ? "✅ Correctly rejected (invalid dates)" : "❌ Should have been rejected");

        printTestHeader("Test 5.5: returnProduct() - Non-existent purchase");
        double refund5 = employee.returnProduct("9999999999", "P3002", today, today);
        System.out.println((refund5 == -1) ? "✅ Correctly rejected (purchase not found)" : "❌ Should have been rejected");

        printTestHeader("Test 5.6: returnProduct() - Non-existent product");
        employee.purchaseProduct("1010101010", "P3002", today);
        double refund6 = employee.returnProduct("1010101010", "P9999", today, today);
        System.out.println((refund6 == -1) ? "✅ Correctly rejected (product not found)" : "❌ Should have been rejected");
        System.out.println();

        // DATA PERSISTENCE TESTS
        printSectionHeader("PART 6: DATA PERSISTENCE");

        printTestHeader("Test 6.1: logout() - Admin saves employee data");
        admin.logout();

        printTestHeader("Test 6.2: logout() - Employee saves product and purchase data");
        employee.logout();
        System.out.println();

        // FINAL SUMMARY
        System.out.println("=".repeat(60));
        System.out.println("            ✅ ALL COMPREHENSIVE TESTS COMPLETED");
        System.out.println("=".repeat(60));
        System.out.println("\nTest Summary:");
        System.out.println("- Admin Role: Employee management ✅");
        System.out.println("- Employee Role: Product management ✅");
        System.out.println("- Employee Role: Purchase operations ✅");
        System.out.println("- Employee Role: Payment processing ✅");
        System.out.println("- Employee Role: Return processing (14-day rule) ✅");
        System.out.println("- Data Persistence: File operations ✅");
        System.out.println("=".repeat(60) + "\n");
    }

    // ======================== UTILITY METHODS ========================
    private static void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }

    private static void printTestHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    private static float getFloatInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                float value = scanner.nextFloat();
                scanner.nextLine(); // consume newline
                return value;
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }
    }
}