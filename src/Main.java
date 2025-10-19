import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import AdminRoleFunctions.AdminRole;
import CustomerInteractionAndEmployeeRole.CustomerProduct;
import CustomerInteractionAndEmployeeRole.CustomerProductDatabase;
import CustomerInteractionAndEmployeeRole.EmployeeRole;
import EmployeeSystem.EmployeeUser;
import ProductManagement.Product;
import ProductManagement.ProductDatabase;

import java.time.LocalDate;

public class Main extends Application {

    private AdminRole admin;
    private EmployeeRole employee;
    private ProductDatabase productDB;
    private CustomerProductDatabase customerProductDB;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Initialize databases and roles
        productDB = new ProductDatabase("Products.txt");
        customerProductDB = new CustomerProductDatabase("CustomersProducts.txt");
        productDB.readFromFile();
        customerProductDB.readFromFile();

        admin = new AdminRole();
        employee = new EmployeeRole(productDB, customerProductDB);

        // Main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Tab Pane for different sections
        TabPane tabPane = createTabPane();
        root.setCenter(tabPane);

        // Status bar
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        updateStatus("System ready");
    }

    // ======================== HEADER ========================
    private VBox createHeader() {
        VBox header = new VBox();
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 15;");
        header.setSpacing(10);

        Label titleLabel = new Label("INVENTORY MANAGEMENT SYSTEM");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("Manage Employees, Products, and Customer Purchases");
        subtitleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #bdc3c7;");

        titleLabel.setEffect(new javafx.scene.effect.DropShadow());

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    // ======================== TAB PANE ========================
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 12;");

        Tab adminTab = new Tab("Admin Functions", createAdminTab());
        Tab employeeTab = new Tab("Employee Functions", createEmployeeTab());
        Tab viewTab = new Tab("View All Data", createViewTab());

        tabPane.getTabs().addAll(adminTab, employeeTab, viewTab);
        return tabPane;
    }

    // ======================== ADMIN TAB ========================
    private VBox createAdminTab() {
        VBox adminBox = new VBox();
        adminBox.setStyle("-fx-padding: 15; -fx-spacing: 15;");

        // Add Employee Section
        VBox addEmpBox = createStyledBox("Add Employee");
        GridPane addEmpForm = new GridPane();
        addEmpForm.setHgap(10);
        addEmpForm.setVgap(10);
        addEmpForm.setPadding(new Insets(10));

        TextField empIdField = new TextField();
        empIdField.setPromptText("Employee ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        addEmpForm.add(new Label("ID:"), 0, 0);
        addEmpForm.add(empIdField, 1, 0);
        addEmpForm.add(new Label("Name:"), 0, 1);
        addEmpForm.add(nameField, 1, 1);
        addEmpForm.add(new Label("Email:"), 0, 2);
        addEmpForm.add(emailField, 1, 2);
        addEmpForm.add(new Label("Address:"), 0, 3);
        addEmpForm.add(addressField, 1, 3);
        addEmpForm.add(new Label("Phone:"), 0, 4);
        addEmpForm.add(phoneField, 1, 4);

        Button addEmpBtn = createStyledButton("Add Employee");
        addEmpBtn.setOnAction(e -> {
            if (!empIdField.getText().isEmpty()) {
                admin.addEmployee(empIdField.getText(), nameField.getText(),
                        emailField.getText(), addressField.getText(), phoneField.getText());
                updateStatus("Employee added successfully");
                empIdField.clear();
                nameField.clear();
                emailField.clear();
                addressField.clear();
                phoneField.clear();
            }
        });

        addEmpBox.getChildren().addAll(addEmpForm, addEmpBtn);

        // Remove Employee Section
        VBox removeEmpBox = createStyledBox("Remove Employee");
        HBox removeEmpForm = new HBox(10);
        removeEmpForm.setPadding(new Insets(10));
        TextField removeIdField = new TextField();
        removeIdField.setPromptText("Employee ID to remove");
        Button removeEmpBtn = createStyledButton("Remove");
        removeEmpBtn.setOnAction(e -> {
            admin.removeEmployee(removeIdField.getText());
            updateStatus("Employee removed");
            removeIdField.clear();
        });
        removeEmpForm.getChildren().addAll(removeIdField, removeEmpBtn);
        removeEmpBox.getChildren().add(removeEmpForm);

        // View Employees Table
        VBox viewEmpBox = createStyledBox("View All Employees");
        TableView<EmployeeUser> empTable = new TableView<>();

        TableColumn<EmployeeUser, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSearchKey()));

        TableColumn<EmployeeUser, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().lineRepresentation().split(",")[1]));

        TableColumn<EmployeeUser, String> emailCol = new TableColumn<>("EMAIL");
        emailCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().lineRepresentation().split(",")[2]));

        TableColumn<EmployeeUser, String> addressCol = new TableColumn<>("ADDRESS");
        addressCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().lineRepresentation().split(",")[3]));

        TableColumn<EmployeeUser, String> phoneNumCol = new TableColumn<>("PHONE NUMBER");
        phoneNumCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().lineRepresentation().split(",")[4]));

        empTable.getColumns().addAll(idCol, nameCol ,  emailCol, addressCol, phoneNumCol);

        Button refreshEmpBtn = createStyledButton("Refresh");
        refreshEmpBtn.setOnAction(e -> {
            empTable.getItems().clear();
            empTable.getItems().addAll(admin.getListOfEmployees());
        });

        VBox tableBox = new VBox(5);
        tableBox.getChildren().addAll(refreshEmpBtn, empTable);
        viewEmpBox.getChildren().add(tableBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(new VBox(10, addEmpBox, removeEmpBox, viewEmpBox));
        scrollPane.setFitToWidth(true);

        adminBox.getChildren().add(scrollPane);
        return adminBox;
    }

    // ======================== EMPLOYEE TAB ========================
    private VBox createEmployeeTab() {
        VBox employeeBox = new VBox();
        employeeBox.setStyle("-fx-padding: 15; -fx-spacing: 15;");

        // Add Product Section
        VBox addProdBox = createStyledBox("Add Product");
        GridPane addProdForm = new GridPane();
        addProdForm.setHgap(10);
        addProdForm.setVgap(10);
        addProdForm.setPadding(new Insets(10));

        TextField prodIdField = new TextField();
        prodIdField.setPromptText("Product ID");
        TextField prodNameField = new TextField();
        prodNameField.setPromptText("Product Name");
        TextField manufacturerField = new TextField();
        manufacturerField.setPromptText("Manufacturer");
        TextField supplierField = new TextField();
        supplierField.setPromptText("Supplier");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        addProdForm.add(new Label("ID:"), 0, 0);
        addProdForm.add(prodIdField, 1, 0);
        addProdForm.add(new Label("Name:"), 0, 1);
        addProdForm.add(prodNameField, 1, 1);
        addProdForm.add(new Label("Manufacturer:"), 0, 2);
        addProdForm.add(manufacturerField, 1, 2);
        addProdForm.add(new Label("Supplier:"), 0, 3);
        addProdForm.add(supplierField, 1, 3);
        addProdForm.add(new Label("Quantity:"), 0, 4);
        addProdForm.add(quantityField, 1, 4);
        addProdForm.add(new Label("Price:"), 0, 5);
        addProdForm.add(priceField, 1, 5);

        Button addProdBtn = createStyledButton("Add Product");
        addProdBtn.setOnAction(e -> {
            try {
                employee.addProduct(prodIdField.getText(), prodNameField.getText(),
                        manufacturerField.getText(), supplierField.getText(),
                        Integer.parseInt(quantityField.getText()),
                        Float.parseFloat(priceField.getText()));
                productDB.saveToFile(); // Auto-save after adding
                updateStatus("Product added and saved successfully");
                clearFields(prodIdField, prodNameField, manufacturerField, supplierField, quantityField, priceField);
            } catch (NumberFormatException ex) {
                updateStatus("Error: Invalid number format");
            }
        });

        addProdBox.getChildren().addAll(addProdForm, addProdBtn);

        // Purchase Product Section
        VBox purchaseBox = createStyledBox("Purchase Product");
        GridPane purchaseForm = new GridPane();
        purchaseForm.setHgap(10);
        purchaseForm.setVgap(10);
        purchaseForm.setPadding(new Insets(10));

        TextField ssnField = new TextField();
        ssnField.setPromptText("Customer SSN");
        TextField purchaseProdField = new TextField();
        purchaseProdField.setPromptText("Product ID");
        DatePicker purchaseDatePicker = new DatePicker();

        purchaseForm.add(new Label("SSN:"), 0, 0);
        purchaseForm.add(ssnField, 1, 0);
        purchaseForm.add(new Label("Product ID:"), 0, 1);
        purchaseForm.add(purchaseProdField, 1, 1);
        purchaseForm.add(new Label("Date:"), 0, 2);
        purchaseForm.add(purchaseDatePicker, 1, 2);

        Button purchaseBtn = createStyledButton("Purchase");
        purchaseBtn.setOnAction(e -> {
            if (purchaseDatePicker.getValue() != null) {
                boolean success = employee.purchaseProduct(ssnField.getText(), purchaseProdField.getText(),
                        purchaseDatePicker.getValue());
                if (success) {
                    employee.logout(); // Auto-save after purchase
                    updateStatus("Purchase recorded - Quantity decremented");
                    ssnField.clear();
                    purchaseProdField.clear();
                    purchaseDatePicker.setValue(null);
                } else {
                    updateStatus("Purchase failed");
                }
            }
        });

        purchaseBox.getChildren().addAll(purchaseForm, purchaseBtn);

        // Apply Payment Section
        VBox paymentBox = createStyledBox("Apply Payment");
        GridPane paymentForm = new GridPane();
        paymentForm.setHgap(10);
        paymentForm.setVgap(10);
        paymentForm.setPadding(new Insets(10));

        TextField paySSNField = new TextField();
        paySSNField.setPromptText("Customer SSN");
        TextField payProdField = new TextField();
        payProdField.setPromptText("Product ID");
        DatePicker payDatePicker = new DatePicker();

        paymentForm.add(new Label("SSN:"), 0, 0);
        paymentForm.add(paySSNField, 1, 0);
        paymentForm.add(new Label("Product ID:"), 0, 1);
        paymentForm.add(payProdField, 1, 1);
        paymentForm.add(new Label("Date:"), 0, 2);
        paymentForm.add(payDatePicker, 1, 2);

        Button paymentBtn = createStyledButton("Apply Payment");
        paymentBtn.setOnAction(e -> {
            if (payDatePicker.getValue() != null) {
                employee.applyPayment(paySSNField.getText(), payProdField.getText(), payDatePicker.getValue());
                updateStatus("Payment applied");
            }
        });

        paymentBox.getChildren().addAll(paymentForm, paymentBtn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(new VBox(10, addProdBox, purchaseBox, paymentBox));
        scrollPane.setFitToWidth(true);

        employeeBox.getChildren().add(scrollPane);
        return employeeBox;
    }

    // ======================== VIEW ALL DATA TAB ========================
    private VBox createViewTab() {
        VBox viewBox = new VBox(10);
        viewBox.setStyle("-fx-padding: 15;");

        // Products Table
        VBox productsSection = createStyledBox("Products");
        TableView<Product> productsTable = new TableView<>();

        TableColumn<Product, String> prodIdCol = new TableColumn<>("Product ID");
        prodIdCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSearchKey()));

        TableColumn<Product, String> prodNameCol = new TableColumn<>("Name");
        prodNameCol.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().lineRepresentation().split(",");
            return new javafx.beans.property.SimpleStringProperty(parts.length > 1 ? parts[1] : "");
        });

        TableColumn<Product, String> manuCol = new TableColumn<>("Manufacturer");
        manuCol.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().lineRepresentation().split(",");
            return new javafx.beans.property.SimpleStringProperty(parts.length > 2 ? parts[2] : "");
        });

        TableColumn<Product, String> suppCol = new TableColumn<>("Supplier");
        suppCol.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().lineRepresentation().split(",");
            return new javafx.beans.property.SimpleStringProperty(parts.length > 3 ? parts[3] : "");
        });

        TableColumn<Product, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().lineRepresentation().split(",");
            return new javafx.beans.property.SimpleStringProperty(parts.length > 4 ? parts[4] : "");
        });

        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> {
            String[] parts = cellData.getValue().lineRepresentation().split(",");
            return new javafx.beans.property.SimpleStringProperty(parts.length > 5 ? parts[5] : "");
        });

        productsTable.getColumns().addAll(prodIdCol, prodNameCol, manuCol, suppCol, qtyCol, priceCol);
        productsTable.getItems().addAll(employee.getListOfProducts());

        Button refreshProductsBtn = createStyledButton("Refresh Products");
        refreshProductsBtn.setOnAction(e -> {
            productDB = new ProductDatabase("Products.txt");
            productDB.readFromFile();
            employee = new EmployeeRole(productDB, customerProductDB);
            productsTable.getItems().clear();
            productsTable.getItems().addAll(employee.getListOfProducts());
            updateStatus("Products refreshed");
        });

        VBox productsBox = new VBox(5);
        productsBox.getChildren().addAll(refreshProductsBtn, productsTable);
        productsSection.getChildren().add(productsBox);

        // Purchases Table
        VBox purchasesSection = createStyledBox("Customer Purchases");
        TableView<CustomerProduct> purchasesTable = new TableView<>();

        TableColumn<CustomerProduct, String> ssnCol = new TableColumn<>("Customer SSN");
        ssnCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCustomerSSN()));

        TableColumn<CustomerProduct, String> prodIdPurCol = new TableColumn<>("Product ID");
        prodIdPurCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProductID()));

        TableColumn<CustomerProduct, String> dateCol = new TableColumn<>("Purchase Date");
        dateCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPurchaseDate().toString()));

        TableColumn<CustomerProduct, String> paidCol = new TableColumn<>("Paid");
        paidCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().isPaid() ? "Yes" : "No"));

        purchasesTable.getColumns().addAll(ssnCol, prodIdPurCol, dateCol, paidCol);
        purchasesTable.getItems().addAll(employee.getListOfPurchasingOperations());

        Button refreshPurchasesBtn = createStyledButton("Refresh Purchases");
        refreshPurchasesBtn.setOnAction(e -> {
            customerProductDB = new CustomerProductDatabase("CustomersProducts.txt");
            customerProductDB.readFromFile();
            employee = new EmployeeRole(productDB, customerProductDB);
            purchasesTable.getItems().clear();
            purchasesTable.getItems().addAll(employee.getListOfPurchasingOperations());
            updateStatus("Purchases refreshed");
        });

        VBox purchasesBox = new VBox(5);
        purchasesBox.getChildren().addAll(refreshPurchasesBtn, purchasesTable);
        purchasesSection.getChildren().add(purchasesBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(new VBox(15, productsSection, purchasesSection));
        scrollPane.setFitToWidth(true);

        viewBox.getChildren().add(scrollPane);
        return viewBox;
    }

    // ======================== STATUS BAR ========================
    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-background-color: #34495e; -fx-padding: 10;");
        statusBar.setPrefHeight(50);

        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12;");

        Button exitBtn = createStyledButton("Exit & Save");
        exitBtn.setStyle("-fx-padding: 8 15 8 15;");
        exitBtn.setOnAction(e -> {
            admin.logout();
            employee.logout();
            System.exit(0);
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, spacer, exitBtn);
        return statusBar;
    }

    // ======================== HELPER METHODS ========================
    private VBox createStyledBox(String title) {
        VBox box = new VBox();
        box.setStyle("-fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-color: white;");
        box.setPadding(new Insets(10));
        box.setSpacing(10);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        box.getChildren().add(titleLabel);
        return box;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 12; -fx-padding: 8 15 8 15; -fx-background-color: #3498db; " +
                "-fx-text-fill: white; -fx-border-radius: 5;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 12; -fx-padding: 8 15 8 15; " +
                "-fx-background-color: #2980b9; -fx-text-fill: white; -fx-border-radius: 5;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 12; -fx-padding: 8 15 8 15; " +
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5;"));
        return btn;
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), statusLabel);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}