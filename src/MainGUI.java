import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MainGUI extends BaseGUI {
    private StudentManager studentManager;
    private String currentUser;
    private JPanel contentPanel;

    public MainGUI(StudentManager manager, String username) {
        this.studentManager = manager;
        this.currentUser = username;
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        setTitle("Student Management System - Main Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerWindow();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        showWelcomeScreen();

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(0, 70));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + currentUser);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userLabel.setForeground(Color.WHITE);
        userPanel.add(userLabel);

        JButton logoutButton = createStyledButton("Logout", DANGER_COLOR, new Dimension(100, 35));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutButton.addActionListener(e -> handleLogout());

        userPanel.add(logoutButton);

        panel.add(userPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        addMenuButton(panel, "Dashboard", e -> showWelcomeScreen(), INFO_COLOR);
        addMenuButton(panel, "Add Student", e -> showAddStudentForm(), SUCCESS_COLOR);
        addMenuButton(panel, "View All Students", e -> showAllStudents(), PURPLE_COLOR);
        addMenuButton(panel, "Search Student", e -> showSearchForm(), INFO_COLOR);
        addMenuButton(panel, "Update Student", e -> showUpdateForm(), WARNING_COLOR);
        addMenuButton(panel, "Delete Student", e -> showDeleteForm(), DANGER_COLOR);
        addMenuButton(panel, "Statistics", e -> showStatistics(), TEAL_COLOR);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void addMenuButton(JPanel panel, String text, ActionListener action, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 45));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(color.darker());
            }
        });

        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void showWelcomeScreen() {
        contentPanel.removeAll();

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome to Student Management System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(SECONDARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomePanel.add(welcomeLabel, gbc);

        JLabel infoLabel = new JLabel("<html><center>Use the menu on the left to navigate<br>through different functions</center></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setForeground(new Color(127, 140, 141));
        gbc.gridy = 1;
        welcomePanel.add(infoLabel, gbc);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        List<student> allStudents = studentManager.getAllStudents();
        int totalStudents = allStudents.size();
        double avgGPA = StatisticsManager.calculateAverageGPA(allStudents);
        Map<String, Integer> deptMap = StatisticsManager.countByDepartment(allStudents);
        int deptCount = (deptMap != null) ? deptMap.size() : 0;

        statsPanel.add(createStatCard("Total Students", String.valueOf(totalStudents), INFO_COLOR));
        statsPanel.add(createStatCard("Average GPA", String.format("%.2f", avgGPA), SUCCESS_COLOR));
        statsPanel.add(createStatCard("Departments", String.valueOf(deptCount), PURPLE_COLOR));

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        welcomePanel.add(statsPanel, gbc);

        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(200, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        return card;
    }

    private void showAddStudentForm() {
        contentPanel.removeAll();

        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Add New Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        JTextField deptField = new JTextField(20);
        JTextField gpaField = new JTextField(20);

        addFormField(inputPanel, gbc, 0, "Student ID:", idField);
        addFormField(inputPanel, gbc, 1, "Name:", nameField);
        addFormField(inputPanel, gbc, 2, "Age:", ageField);
        addFormField(inputPanel, gbc, 3, "Gender:", genderBox);
        addFormField(inputPanel, gbc, 4, "Department:", deptField);
        addFormField(inputPanel, gbc, 5, "GPA (0.0-4.0):", gpaField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add Student", SUCCESS_COLOR);
        styleButton(addButton, SUCCESS_COLOR);
        addButton.addActionListener(e -> handleAddStudent(idField, nameField, ageField, genderBox, deptField, gpaField));

        JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR);
        styleButton(cancelButton, DANGER_COLOR);
        cancelButton.addActionListener(e -> showWelcomeScreen());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(buttonPanel, gbc);

        formPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleAddStudent(JTextField idField, JTextField nameField, JTextField ageField,
                                  JComboBox<String> genderBox, JTextField deptField, JTextField gpaField) {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String dept = deptField.getText().trim();
            String gpaStr = gpaField.getText().trim();

            if (!Validator.isValidStudentId(id)) {
                showError(Validator.getErrorMessage("id"));
                return;
            }

            if (studentManager.searchByFormattedId(id) != null) {
                showError("Student ID already exists! Please use a different ID.");
                return;
            }

            int age;
            double gpa;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException ex) {
                showError("Age must be a valid number!");
                return;
            }

            try {
                gpa = Double.parseDouble(gpaStr);
            } catch (NumberFormatException ex) {
                showError("GPA must be a valid number!");
                return;
            }

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

            student newStudent = new student(name, age, gender, id, dept, gpa);
            if (studentManager.addStudent(newStudent)) {
                showSuccess("✓ Student added successfully!\nID: " + newStudent.getStudentId());
                idField.setText("");
                nameField.setText("");
                ageField.setText("");
                deptField.setText("");
                gpaField.setText("");
                showWelcomeScreen();
            } else {
                showError("Failed to add student!");
            }

        } catch (Exception ex) {
            showError("An error occurred: " + ex.getMessage());
        }
    }

    private void showAllStudents() {
        contentPanel.removeAll();

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("All Students");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        tablePanel.add(titleLabel, BorderLayout.NORTH);

        List<student> students = studentManager.getAllStudents();
        String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (student s : students) {
            model.addRow(new Object[]{
                    s.getStudentId(),
                    s.getName(),
                    s.getAge(),
                    s.getGender(),
                    s.getDepartment(),
                    String.format("%.2f", s.getGpa())
            });
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(INFO_COLOR);
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JLabel countLabel = new JLabel("Total Students: " + students.size());
        countLabel.setFont(new Font("Arial", Font.BOLD, 14));
        countLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(countLabel, BorderLayout.SOUTH);

        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSearchForm() {
        contentPanel.removeAll();

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Search Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        searchPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search by:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JComboBox<String> searchType = new JComboBox<>(new String[]{"ID", "Name", "Department"});
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton searchButton = createStyledButton("Search", INFO_COLOR);
        styleButton(searchButton, INFO_COLOR);

        inputPanel.add(searchLabel);
        inputPanel.add(searchType);
        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);

        searchButton.addActionListener(e -> handleSearch(searchType, searchField, resultPanel));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(resultPanel, BorderLayout.CENTER);

        searchPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(searchPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleSearch(JComboBox<String> searchType, JTextField searchField, JPanel resultPanel) {
        String type = (String) searchType.getSelectedItem();
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            showWarning("Please enter a search term!");
            return;
        }

        List<student> results = new ArrayList<>();

        if (type.equals("ID")) {
            try {
                int id = Integer.parseInt(query);
                student found = studentManager.searchById(id);
                if (found != null) results.add(found);
            } catch (NumberFormatException ex) {
                showError("ID must be a number!");
                return;
            }
        } else if (type.equals("Name")) {
            results = studentManager.searchByName(query);
        } else {
            results = studentManager.searchByDepartment(query);
        }

        resultPanel.removeAll();

        if (results.isEmpty()) {
            JLabel noResults = new JLabel("No students found!");
            noResults.setFont(new Font("Arial", Font.PLAIN, 16));
            noResults.setHorizontalAlignment(SwingConstants.CENTER);
            resultPanel.add(noResults, BorderLayout.CENTER);
        } else {
            String[] columns = {"ID", "Name", "Age", "Gender", "Department", "GPA"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (student s : results) {
                model.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName(),
                        s.getAge(),
                        s.getGender(),
                        s.getDepartment(),
                        String.format("%.2f", s.getGpa())
                });
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setRowHeight(35);
            table.setGridColor(new Color(189, 195, 199));
            table.setSelectionBackground(INFO_COLOR);
            table.setSelectionForeground(Color.WHITE);

            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(PRIMARY_COLOR);
            header.setForeground(Color.WHITE);
            header.setPreferredSize(new Dimension(header.getWidth(), 40));

            JScrollPane scrollPane = new JScrollPane(table);
            resultPanel.add(scrollPane, BorderLayout.CENTER);

            JLabel countLabel = new JLabel("Found: " + results.size() + " student(s)");
            countLabel.setFont(new Font("Arial", Font.BOLD, 14));
            countLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            resultPanel.add(countLabel, BorderLayout.SOUTH);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void showUpdateForm() {
        contentPanel.removeAll();

        JPanel updatePanel = new JPanel(new BorderLayout());
        updatePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Update Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        updatePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("Enter Student ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField idField = new JTextField(10);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton searchButton = createStyledButton("Find Student", INFO_COLOR);
        styleButton(searchButton, INFO_COLOR);

        searchPanel.add(idLabel);
        searchPanel.add(idField);
        searchPanel.add(searchButton);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        searchButton.addActionListener(e -> handleFindStudentForUpdate(idField, formPanel));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);

        updatePanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(updatePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleFindStudentForUpdate(JTextField idField, JPanel formPanel) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            student existing = studentManager.searchById(id);

            if (existing == null) {
                showWarning("Student not found with ID: " + id);
                return;
            }

            formPanel.removeAll();
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.WEST;

            JTextField nameField = new JTextField(existing.getName(), 20);
            JTextField ageField = new JTextField(String.valueOf(existing.getAge()), 20);
            JComboBox<String> genderBox = new JComboBox<>(new String[]{"Male", "Female"});
            genderBox.setSelectedItem(existing.getGender());
            JTextField deptField = new JTextField(existing.getDepartment(), 20);
            JTextField gpaField = new JTextField(String.format("%.2f", existing.getGpa()), 20);

            addFormField(formPanel, gbc, 0, "Name:", nameField);
            addFormField(formPanel, gbc, 1, "Age:", ageField);
            addFormField(formPanel, gbc, 2, "Gender:", genderBox);
            addFormField(formPanel, gbc, 3, "Department:", deptField);
            addFormField(formPanel, gbc, 4, "GPA:", gpaField);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);

            JButton updateButton = createStyledButton("Update", SUCCESS_COLOR);
            styleButton(updateButton, SUCCESS_COLOR);
            updateButton.addActionListener(ev -> handleUpdateStudent(id, nameField, ageField, genderBox, deptField, gpaField));

            JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR);
            styleButton(cancelButton, DANGER_COLOR);
            cancelButton.addActionListener(ev -> showWelcomeScreen());

            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(buttonPanel, gbc);

            formPanel.revalidate();
            formPanel.repaint();

        } catch (NumberFormatException ex) {
            showError("Please enter a valid ID number!");
        }
    }

    private void handleUpdateStudent(int id, JTextField nameField, JTextField ageField,
                                     JComboBox<String> genderBox, JTextField deptField, JTextField gpaField) {
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = (String) genderBox.getSelectedItem();
            String dept = deptField.getText().trim();
            double gpa = Double.parseDouble(gpaField.getText().trim());

            if (!Validator.isValidName(name) || !Validator.isValidAge(age) ||
                    !Validator.isValidDepartment(dept) || !Validator.isValidGPA(gpa)) {
                showError("Invalid input! Please check all fields.");
                return;
            }

            student updatedStudent = new student(name, age, gender, "", dept, gpa);
            if (studentManager.updateStudent(id, updatedStudent)) {
                showSuccess("Student updated successfully!");
                showWelcomeScreen();
            } else {
                showError("Failed to update student!");
            }
        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers!");
        }
    }

    private void showDeleteForm() {
        contentPanel.removeAll();

        JPanel deletePanel = new JPanel(new BorderLayout());
        deletePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Delete Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        deletePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("Enter Student ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField idField = new JTextField(10);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton searchButton = createStyledButton("Find Student", INFO_COLOR);
        styleButton(searchButton, INFO_COLOR);

        searchPanel.add(idLabel);
        searchPanel.add(idField);
        searchPanel.add(searchButton);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);

        searchButton.addActionListener(e -> handleFindStudentForDelete(idField, infoPanel));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        deletePanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(deletePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleFindStudentForDelete(JTextField idField, JPanel infoPanel) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            student existing = studentManager.searchById(id);

            if (existing == null) {
                showWarning("Student not found with ID: " + id);
                return;
            }

            infoPanel.removeAll();

            JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            detailsPanel.setBackground(Color.WHITE);
            detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            addDetailRow(detailsPanel, "ID:", existing.getStudentId());
            addDetailRow(detailsPanel, "Name:", existing.getName());
            addDetailRow(detailsPanel, "Age:", String.valueOf(existing.getAge()));
            addDetailRow(detailsPanel, "Gender:", existing.getGender());
            addDetailRow(detailsPanel, "Department:", existing.getDepartment());
            addDetailRow(detailsPanel, "GPA:", String.format("%.2f", existing.getGpa()));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBackground(Color.WHITE);

            JButton deleteButton = createStyledButton("Delete Student", DANGER_COLOR);
            styleButton(deleteButton, DANGER_COLOR);
            deleteButton.addActionListener(ev -> handleDeleteStudent(id));

            JButton cancelButton = createStyledButton("Cancel", INFO_COLOR);
            styleButton(cancelButton, INFO_COLOR);
            cancelButton.addActionListener(ev -> showWelcomeScreen());

            buttonPanel.add(deleteButton);
            buttonPanel.add(cancelButton);

            infoPanel.add(detailsPanel, BorderLayout.CENTER);
            infoPanel.add(buttonPanel, BorderLayout.SOUTH);

            infoPanel.revalidate();
            infoPanel.repaint();

        } catch (NumberFormatException ex) {
            showError("Please enter a valid ID number!");
        }
    }

    private void handleDeleteStudent(int id) {
        int confirm = showConfirmDialog(
                "Are you sure you want to delete this student?",
                "Confirm Deletion"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentManager.deleteStudent(id)) {
                showSuccess("Student deleted successfully!");
                showWelcomeScreen();
            } else {
                showError("Failed to delete student!");
            }
        }
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(valueLabel);
    }

    private void showStatistics() {
        contentPanel.removeAll();

        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Student Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        statsPanel.add(titleLabel, BorderLayout.NORTH);

        List<student> students = studentManager.getAllStudents();

        if (students.isEmpty()) {
            JLabel noData = new JLabel("No students in the system.");
            noData.setFont(new Font("Arial", Font.PLAIN, 16));
            noData.setHorizontalAlignment(SwingConstants.CENTER);
            statsPanel.add(noData, BorderLayout.CENTER);
        } else {
            JPanel detailsPanel = new JPanel(new GridBagLayout());
            detailsPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.anchor = GridBagConstraints.WEST;

            double avgGPA = StatisticsManager.calculateAverageGPA(students);
            student highest = StatisticsManager.getHighestGPA(students);
            student lowest = StatisticsManager.getLowestGPA(students);
            Map<String, Integer> deptCount = StatisticsManager.countByDepartment(students);

            int row = 0;
            addStatRow(detailsPanel, gbc, row++, "Total Students:", String.valueOf(students.size()), INFO_COLOR);
            addStatRow(detailsPanel, gbc, row++, "Average GPA:", String.format("%.2f", avgGPA), SUCCESS_COLOR);
            addStatRow(detailsPanel, gbc, row++, "Highest GPA:", highest.getName() + " (" + highest.getGpa() + ")", PURPLE_COLOR);
            addStatRow(detailsPanel, gbc, row++, "Lowest GPA:", lowest.getName() + " (" + lowest.getGpa() + ")", new Color(230, 126, 34));

            JLabel deptLabel = new JLabel("Students by Department:");
            deptLabel.setFont(new Font("Arial", Font.BOLD, 16));
            deptLabel.setForeground(SECONDARY_COLOR);
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 2;
            detailsPanel.add(deptLabel, gbc);

            for (Map.Entry<String, Integer> entry : deptCount.entrySet()) {
                gbc.gridwidth = 1;
                addStatRow(detailsPanel, gbc, row++, "  " + entry.getKey() + ":", entry.getValue() + " students", INFO_COLOR);
            }

            statsPanel.add(detailsPanel, BorderLayout.CENTER);
        }

        contentPanel.add(statsPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addStatRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value, Color color) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 15));
        lblLabel.setForeground(color);
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(lblLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        gbc.gridx = 1;
        panel.add(valueLabel, gbc);
    }

    private void handleLogout() {
        int choice = showConfirmDialog(
                "Are you sure you want to logout?",
                "Confirm Logout"
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.setVisible(true);
            });
        }
    }
}