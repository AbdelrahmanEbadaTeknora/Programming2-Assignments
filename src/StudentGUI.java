import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class StudentGUI extends JFrame {
    private StudentManager manager;
    private UserFileManager userManager;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BG_COLOR = new Color(236, 240, 241);
    
    public StudentGUI(StudentManager manager, UserFileManager userManager) {
        this.manager = manager;
        this.userManager = userManager;
        
        // Show login first
        if (!showLoginDialog()) {
            System.exit(0);
        }
        
        initializeGUI();
    }
    
    private boolean showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Login - Student Management System", true);
        loginDialog.setSize(400, 250);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        

        JLabel titleLabel = new JLabel("Student Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Username:"), gbc);
        
        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passField, gbc);
        
        // Info label
        JLabel infoLabel = new JLabel("Default: admin / admin123");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(Color.GRAY);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(infoLabel, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton loginBtn = createStyledButton("Login", PRIMARY_COLOR);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR);
        
        final boolean[] loginSuccess = {false};
        
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            
            if (userManager.validateLogin(username, password)) {
                loginSuccess[0] = true;
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        });
        
        cancelBtn.addActionListener(e -> {
            loginDialog.dispose();
        });
        
        passField.addActionListener(e -> loginBtn.doClick());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);
        
        loginDialog.add(panel);
        loginDialog.setVisible(true);
        
        return loginSuccess[0];
    }
    
    private void initializeGUI() {
        setTitle("Student Management System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all panels
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(createAddStudentPanel(), "add");
        mainPanel.add(createViewStudentsPanel(), "view");
        mainPanel.add(createSearchPanel(), "search");
        mainPanel.add(createUpdatePanel(), "update");
        mainPanel.add(createDeletePanel(), "delete");
        mainPanel.add(createStatisticsPanel(), "statistics");
        
        // Create menu bar
        setJMenuBar(createMenuBar());
        
        add(mainPanel);
        cardLayout.show(mainPanel, "dashboard");
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(SECONDARY_COLOR);
        
        JMenu fileMenu = new JMenu("File");
        JMenu studentMenu = new JMenu("Students");
        JMenu helpMenu = new JMenu("Help");
        
        // File menu items
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(dashboardItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Student menu items
        JMenuItem addItem = new JMenuItem("Add Student");
        addItem.addActionListener(e -> cardLayout.show(mainPanel, "add"));
        
        JMenuItem viewItem = new JMenuItem("View All Students");
        viewItem.addActionListener(e -> {
            refreshTable();
            cardLayout.show(mainPanel, "view");
        });
        
        JMenuItem searchItem = new JMenuItem("Search Student");
        searchItem.addActionListener(e -> cardLayout.show(mainPanel, "search"));
        
        JMenuItem statsItem = new JMenuItem("Statistics");
        statsItem.addActionListener(e -> {
            refreshStatistics();
            cardLayout.show(mainPanel, "statistics");
        });
        
        studentMenu.add(addItem);
        studentMenu.add(viewItem);
        studentMenu.add(searchItem);
        studentMenu.addSeparator();
        studentMenu.add(statsItem);
        
        // Help menu
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this,
                "Student Management System v1.0\n" +
                "Developed for CC272 - Programming II\n" +
                "Alexandria University",
                "About",
                JOptionPane.INFORMATION_MESSAGE));
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Student Management System - Dashboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Center buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        
        // Create dashboard buttons
        String[] buttonLabels = {
            "Add Student", "View Students", "Search Student",
            "Update Student", "Delete Student", "Statistics"
        };
        String[] cardNames = {"add", "view", "search", "update", "delete", "statistics"};
        
        int row = 0, col = 0;
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton btn = createDashboardButton(buttonLabels[i]);
            final String cardName = cardNames[i];
            btn.addActionListener(e -> {
                if (cardName.equals("view") || cardName.equals("statistics")) {
                    if (cardName.equals("view")) refreshTable();
                    if (cardName.equals("statistics")) refreshStatistics();
                }
                cardLayout.show(mainPanel, cardName);
            });
            
            gbc.gridx = col;
            gbc.gridy = row;
            centerPanel.add(btn, gbc);
            
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 100));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField deptField = new JTextField(20);
        JTextField gpaField = new JTextField(20);
        
        addFormField(formPanel, gbc, 0, "Name:", nameField);
        addFormField(formPanel, gbc, 1, "Age:", ageField);
        addFormField(formPanel, gbc, 2, "Gender:", genderBox);
        addFormField(formPanel, gbc, 3, "Department:", deptField);
        addFormField(formPanel, gbc, 4, "GPA:", gpaField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addBtn = createStyledButton("Add Student", SUCCESS_COLOR);
        JButton clearBtn = createStyledButton("Clear", Color.GRAY);
        JButton backBtn = createStyledButton("Back to Dashboard", SECONDARY_COLOR);
        
        addBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = (String) genderBox.getSelectedItem();
                String dept = deptField.getText().trim();
                double gpa = Double.parseDouble(gpaField.getText().trim());
                
                // Validate
                if (!Validator.isValidName(name)) {
                    showError(Validator.getErrorMessage("name"));
                    return;
                }
                if (!Validator.isValidAge(age)) {
                    showError(Validator.getErrorMessage("age"));
                    return;
                }
                if (!Validator.isValidDepartment(dept)) {
                    showError(Validator.getErrorMessage("department"));
                    return;
                }
                if (!Validator.isValidGPA(gpa)) {
                    showError(Validator.getErrorMessage("gpa"));
                    return;
                }
                
                student newStudent = new student(name, age, gender, "", dept, gpa);
                
                if (manager.addStudent(newStudent)) {
                    showSuccess("Student added successfully! ID: " + newStudent.getStudentId());
                    nameField.setText("");
                    ageField.setText("");
                    deptField.setText("");
                    gpaField.setText("");
                } else {
                    showError("Failed to add student!");
                }
                
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values for Age and GPA!");
            }
        });
        
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            ageField.setText("");
            deptField.setText("");
            gpaField.setText("");
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(addBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createViewStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("All Students", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(PRIMARY_COLOR);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BG_COLOR);
        
        JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
        JButton backBtn = createStyledButton("Back", SECONDARY_COLOR);
        
        refreshBtn.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Search Student", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JComboBox<String> searchType = new JComboBox<>(new String[]{"By ID", "By Name", "By Department"});
        JTextField searchField = new JTextField(20);
        JButton searchBtn = createStyledButton("Search", PRIMARY_COLOR);
        JButton backBtn = createStyledButton("Back", SECONDARY_COLOR);
        
        searchPanel.add(new JLabel("Search Type:"));
        searchPanel.add(searchType);
        searchPanel.add(new JLabel("Search Value:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(backBtn);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Results table
        String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
        DefaultTableModel searchTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable searchTable = new JTable(searchTableModel);
        searchTable.setFont(new Font("Arial", Font.PLAIN, 12));
        searchTable.setRowHeight(25);
        searchTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        searchTable.getTableHeader().setBackground(PRIMARY_COLOR);
        searchTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(searchTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        searchBtn.addActionListener(e -> {
            String searchValue = searchField.getText().trim();
            if (searchValue.isEmpty()) {
                showError("Please enter a search value!");
                return;
            }
            
            searchTableModel.setRowCount(0);
            
            int selectedType = searchType.getSelectedIndex();
            List<student> results = null;
            
            if (selectedType == 0) { // By ID
                try {
                    int id = Integer.parseInt(searchValue);
                    student s = manager.searchById(id);
                    if (s != null) {
                        results = new java.util.ArrayList<>();
                        results.add(s);
                    }
                } catch (NumberFormatException ex) {
                    showError("Invalid ID format!");
                    return;
                }
            } else if (selectedType == 1) { // By Name
                results = manager.searchByName(searchValue);
            } else { // By Department
                results = manager.searchByDepartment(searchValue);
            }
            
            if (results == null || results.isEmpty()) {
                showInfo("No students found!");
            } else {
                for (student s : results) {
                    searchTableModel.addRow(new Object[]{
                        s.getStudentId(), s.getName(), s.getAge(),
                        s.getGender(), s.getDepartment(), s.getGpa()
                    });
                }
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        return panel;
    }
    
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Update Student", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_COLOR);
        
        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find Student"));
        
        JTextField idField = new JTextField(10);
        JButton findBtn = createStyledButton("Find", PRIMARY_COLOR);
        
        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(idField);
        searchPanel.add(findBtn);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Update form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField deptField = new JTextField(20);
        JTextField gpaField = new JTextField(20);
        
        addFormField(formPanel, gbc, 0, "Name:", nameField);
        addFormField(formPanel, gbc, 1, "Age:", ageField);
        addFormField(formPanel, gbc, 2, "Gender:", genderBox);
        addFormField(formPanel, gbc, 3, "Department:", deptField);
        addFormField(formPanel, gbc, 4, "GPA:", gpaField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateBtn = createStyledButton("Update", SUCCESS_COLOR);
        JButton backBtn = createStyledButton("Back", SECONDARY_COLOR);
        
        updateBtn.setEnabled(false);
        
        findBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                student s = manager.searchById(id);
                
                if (s != null) {
                    nameField.setText(s.getName());
                    ageField.setText(String.valueOf(s.getAge()));
                    genderBox.setSelectedItem(s.getGender());
                    deptField.setText(s.getDepartment());
                    gpaField.setText(String.valueOf(s.getGpa()));
                    updateBtn.setEnabled(true);
                    showInfo("Student found! You can now edit the details.");
                } else {
                    showError("Student not found with ID: " + id);
                    updateBtn.setEnabled(false);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid ID format!");
            }
        });
        
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String gender = (String) genderBox.getSelectedItem();
                String dept = deptField.getText().trim();
                double gpa = Double.parseDouble(gpaField.getText().trim());
                
                // Validate
                if (!Validator.isValidName(name) || !Validator.isValidAge(age) ||
                    !Validator.isValidDepartment(dept) || !Validator.isValidGPA(gpa)) {
                    showError("Invalid input data!");
                    return;
                }
                
                student updatedStudent = new student(name, age, gender, "", dept, gpa);
                
                if (manager.updateStudent(id, updatedStudent)) {
                    showSuccess("Student updated successfully!");
                    nameField.setText("");
                    ageField.setText("");
                    deptField.setText("");
                    gpaField.setText("");
                    idField.setText("");
                    updateBtn.setEnabled(false);
                } else {
                    showError("Failed to update student!");
                }
                
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values!");
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        centerPanel.add(formPanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Delete Student", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(DANGER_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Search section
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(Color.WHITE);
        
        JTextField idField = new JTextField(10);
        JButton findBtn = createStyledButton("Find Student", PRIMARY_COLOR);
        
        searchPanel.add(new JLabel("Enter Student ID:"));
        searchPanel.add(idField);
        searchPanel.add(findBtn);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Info panel
        JTextArea infoArea = new JTextArea(10, 40);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(infoArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton deleteBtn = createStyledButton("Delete Student", DANGER_COLOR);
        JButton backBtn = createStyledButton("Back", SECONDARY_COLOR);
        
        deleteBtn.setEnabled(false);
        
        final int[] currentId = {-1};
        
        findBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                student s = manager.searchById(id);
                
                if (s != null) {
                    currentId[0] = id;
                    infoArea.setText(String.format(
                        "Student Details:\n\n" +
                        "ID: %s\n" +
                        "Name: %s\n" +
                        "Age: %d\n" +
                        "Gender: %s\n" +
                        "Department: %s\n" +
                        "GPA: %.2f\n\n" +
                        "Click 'Delete Student' to remove this student.",
                        s.getStudentId(), s.getName(), s.getAge(),
                        s.getGender(), s.getDepartment(), s.getGpa()
                    ));
                    deleteBtn.setEnabled(true);
                } else {
                    infoArea.setText("Student not found with ID: " + id);
                    deleteBtn.setEnabled(false);
                }
            } catch (NumberFormatException ex) {
                showError("Invalid ID format!");
            }
        });
        
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?\nThis action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (manager.deleteStudent(currentId[0])) {
                    showSuccess("Student deleted successfully!");
                    infoArea.setText("");
                    idField.setText("");
                    deleteBtn.setEnabled(false);
                } else {
                    showError("Failed to delete student!");
                }
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);
        
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header
        JLabel headerLabel = new JLabel("Student Statistics", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Stats area
        JTextArea statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statsArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(statsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BG_COLOR);
        
        JButton refreshBtn = createStyledButton("Refresh", PRIMARY_COLOR);
        JButton backBtn = createStyledButton("Back", SECONDARY_COLOR);
        
        refreshBtn.addActionListener(e -> updateStatisticsArea(statsArea));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        updateStatisticsArea(statsArea);
        
        return panel;
    }
    
    private void updateStatisticsArea(JTextArea statsArea) {
        List<student> students = manager.getAllStudents();
        
        if (students.isEmpty()) {
            statsArea.setText("No students in the system.");
            return;
        }
        
        double avgGPA = StatisticsManager.calculateAverageGPA(students);
        student highest = StatisticsManager.getHighestGPA(students);
        student lowest = StatisticsManager.getLowestGPA(students);
        Map<String, Integer> deptCount = StatisticsManager.countByDepartment(students);
        
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("                  STUDENT STATISTICS\n");
        sb.append("=".repeat(60)).append("\n\n");
        
        sb.append("Total Students: ").append(students.size()).append("\n");
        sb.append("Average GPA: ").append(String.format("%.2f", avgGPA)).append("\n\n");
        
        sb.append("Highest GPA:\n");
        sb.append("  Name: ").append(highest.getName()).append("\n");
        sb.append("  GPA: ").append(highest.getGpa()).append("\n\n");
        
        sb.append("Lowest GPA:\n");
        sb.append("  Name: ").append(lowest.getName()).append("\n");
        sb.append("  GPA: ").append(lowest.getGpa()).append("\n\n");
        
        sb.append("Students by Department:\n");
        for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
            sb.append(String.format("  %-20s: %d student(s)\n", 
                entry.getKey(), entry.getValue()));
        }
        
        sb.append("\n").append("=".repeat(60));
        
        statsArea.setText(sb.toString());
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<student> students = manager.getAllStudents();
        for (student s : students) {
            tableModel.addRow(new Object[]{
                s.getStudentId(), s.getName(), s.getAge(),
                s.getGender(), s.getDepartment(), s.getGpa()
            });
        }
    }
    
    private void refreshStatistics() {
        // This will be called when switching to statistics panel
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                             String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}