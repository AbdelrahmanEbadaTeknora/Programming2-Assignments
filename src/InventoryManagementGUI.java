//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.time.LocalDate;
//
//public class InventoryManagementGUI extends Application {
//    private static final String DARK_BG = "#1e1e1e";
//    private static final String PRIMARY_COLOR = "#2196F3";
//    private static final String SECONDARY_COLOR = "#FFC107";
//    private static final String SUCCESS_COLOR = "#4CAF50";
//    private static final String DANGER_COLOR = "#F44336";
//    private static final String TEXT_COLOR = "#FFFFFF";
//
//    private BorderPane mainLayout;
//    private StackPane contentArea;
//    private Label userRoleLabel;
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Inventory Management System");
//        primaryStage.setWidth(1200);
//        primaryStage.setHeight(800);
//
//        mainLayout = new BorderPane();
//        mainLayout.setStyle("-fx-background-color: " + DARK_BG + ";");
//
//        setupHeader();
//        setupSidebar();
//        setupContent();
//
//        Scene scene = new Scene(mainLayout);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        showDashboard();
//    }
//
//    private void setupHeader() {
//        HBox header = new HBox();
//        header.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-padding: 15;");
//        header.setAlignment(Pos.CENTER_LEFT);
//        header.setSpacing(20);
//
//        Label titleLabel = new Label("📦 Inventory Management System");
//        titleLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//
//        userRoleLabel = new Label("Role: Admin");
//        userRoleLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 14;");
//
//        Button logoutBtn = createStyledButton("🚪 Logout", DANGER_COLOR);
//        logoutBtn.setOnAction(e -> showAlert("Logout", "Logged out successfully!"));
//
//        header.getChildren().addAll(titleLabel, spacer, userRoleLabel, logoutBtn);
//        mainLayout.setTop(header);
//    }
//
//    private void setupSidebar() {
//        VBox sidebar = new VBox();
//        sidebar.setStyle("-fx-background-color: #2a2a2a; -fx-padding: 10;");
//        sidebar.setPrefWidth(200);
//        sidebar.setSpacing(10);
//
//        Label menuTitle = new Label("MENU");
//        menuTitle.setStyle("-fx-text-fill: " + PRIMARY_COLOR + "; -fx-font-size: 14; -fx-font-weight: bold;");
//        menuTitle.setPadding(new Insets(10, 0, 20, 0));
//
//        Button dashboardBtn = createSidebarButton("📊 Dashboard", e -> showDashboard());
//        Button adminBtn = createSidebarButton("👤 Admin Panel", e -> showAdminPanel());
//        Button employeeBtn = createSidebarButton("👷 Employee Panel", e -> showEmployeePanel());
//        Button productsBtn = createSidebarButton("📦 Products", e -> showProductsView());
//        Button purchasesBtn = createSidebarButton("🛒 Purchases", e -> showPurchasesView());
//
//        sidebar.getChildren().addAll(menuTitle, dashboardBtn, adminBtn, employeeBtn, productsBtn, purchasesBtn);
//        mainLayout.setLeft(new ScrollPane(sidebar));
//    }
//
//    private void setupContent() {
//        contentArea = new StackPane();
//        contentArea.setStyle("-fx-background-color: " + DARK_BG + ";");
//        mainLayout.setCenter(contentArea);
//    }
//
//    private void showDashboard() {
//        VBox dashboard = new VBox();
//        dashboard.setStyle("-fx-background-color: " + DARK_BG + ";");
//        dashboard.setPadding(new Insets(30));
//        dashboard.setSpacing(20);
//        dashboard.setAlignment(Pos.TOP_CENTER);
//
//        Label title = new Label("Dashboard");
//        title.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 28; -fx-font-weight: bold;");
//
//        HBox statsBox = new HBox();
//        statsBox.setSpacing(20);
//        statsBox.setPrefHeight(150);
//
//        statsBox.getChildren().addAll(
//            createStatCard("Total Employees", "24", PRIMARY_COLOR),
//            createStatCard("Total Products", "87", SECONDARY_COLOR),
//            createStatCard("Total Purchases", "156", SUCCESS_COLOR),
//            createStatCard("Pending Payments", "12", DANGER_COLOR)
//        );
//
//        ScrollPane scrollPane = new ScrollPane(statsBox);
//        scrollPane.setStyle("-fx-control-inner-background: " + DARK_BG + "; -fx-background: " + DARK_BG + ";");
//        scrollPane.setFitToHeight(true);
//
//        dashboard.getChildren().addAll(title, scrollPane);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(dashboard);
//    }
//
//    private VBox createStatCard(String title, String value, String color) {
//        VBox card = new VBox();
//        card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 10; -fx-padding: 20;");
//        card.setAlignment(Pos.CENTER);
//        card.setPrefWidth(200);
//        card.setSpacing(10);
//
//        Label titleLabel = new Label(title);
//        titleLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 14;");
//
//        Label valueLabel = new Label(value);
//        valueLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 32; -fx-font-weight: bold;");
//
//        card.getChildren().addAll(titleLabel, valueLabel);
//        return card;
//    }
//
//    private void showAdminPanel() {
//        VBox adminPanel = new VBox();
//        adminPanel.setStyle("-fx-background-color: " + DARK_BG + ";");
//        adminPanel.setPadding(new Insets(30));
//        adminPanel.setSpacing(20);
//
//        Label title = new Label("Admin Panel");
//        title.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setPadding(new Insets(20));
//        form.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 10;");
//
//        TextField empIdField = createStyledTextField("Employee ID");
//        TextField nameField = createStyledTextField("Name");
//        TextField emailField = createStyledTextField("Email");
//        TextField addressField = createStyledTextField("Address");
//        TextField phoneField = createStyledTextField("Phone");
//
//        Label idLabel = new Label("Employee ID:");
//        Label nameLabel = new Label("Name:");
//        Label emailLabel = new Label("Email:");
//        Label addressLabel = new Label("Address:");
//        Label phoneLabel = new Label("Phone:");
//
//        styleLabel(idLabel);
//        styleLabel(nameLabel);
//        styleLabel(emailLabel);
//        styleLabel(addressLabel);
//        styleLabel(phoneLabel);
//
//        form.add(idLabel, 0, 0);
//        form.add(empIdField, 1, 0);
//        form.add(nameLabel, 0, 1);
//        form.add(nameField, 1, 1);
//        form.add(emailLabel, 0, 2);
//        form.add(emailField, 1, 2);
//        form.add(addressLabel, 0, 3);
//        form.add(addressField, 1, 3);
//        form.add(phoneLabel, 0, 4);
//        form.add(phoneField, 1, 4);
//
//        Button addBtn = createStyledButton("➕ Add Employee", SUCCESS_COLOR);
//        Button removeBtn = createStyledButton("➖ Remove Employee", DANGER_COLOR);
//        Button viewBtn = createStyledButton("📋 View Employees", PRIMARY_COLOR);
//
//        HBox buttonBox = new HBox(15);
//        buttonBox.getChildren().addAll(addBtn, removeBtn, viewBtn);
//
//        addBtn.setOnAction(e -> showAlert("Success", "Employee added successfully!"));
//        removeBtn.setOnAction(e -> showAlert("Action", "Enter Employee ID in the first field and click Remove"));
//        viewBtn.setOnAction(e -> showEmployeesTable());
//
//        VBox scrollablePanel = new VBox(15);
//        scrollablePanel.setStyle("-fx-background-color: " + DARK_BG + ";");
//        scrollablePanel.getChildren().addAll(title, form, buttonBox);
//
//        ScrollPane scrollPane = new ScrollPane(scrollablePanel);
//        scrollPane.setStyle("-fx-control-inner-background: " + DARK_BG + "; -fx-background: " + DARK_BG + ";");
//
//        adminPanel.getChildren().add(scrollPane);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(adminPanel);
//    }
//
//    private void showEmployeePanel() {
//        VBox employeePanel = new VBox();
//        employeePanel.setStyle("-fx-background-color: " + DARK_BG + ";");
//        employeePanel.setPadding(new Insets(30));
//        employeePanel.setSpacing(20);
//
//        Label panelTitle = new Label("Employee Operations");
//        panelTitle.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        TabPane tabPane = new TabPane();
//        tabPane.setStyle("-fx-control-inner-background: " + DARK_BG + "; -fx-background-color: " + DARK_BG + ";");
//        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
//
//        Tab productsTab = new Tab("📦 Products", createProductsTab());
//        Tab purchasesTab = new Tab("🛒 Purchases", createPurchasesTab());
//        Tab returnsTab = new Tab("↩️ Returns", createReturnsTab());
//        Tab paymentTab = new Tab("💳 Payments", createPaymentTab());
//
//        productsTab.setStyle("-fx-padding: 10;");
//        purchasesTab.setStyle("-fx-padding: 10;");
//        returnsTab.setStyle("-fx-padding: 10;");
//        paymentTab.setStyle("-fx-padding: 10;");
//
//        tabPane.getTabs().addAll(productsTab, purchasesTab, returnsTab, paymentTab);
//
//        employeePanel.getChildren().addAll(panelTitle, tabPane);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(employeePanel);
//    }
//
//    private VBox createProductsTab() {
//        VBox tab = new VBox(15);
//        tab.setPadding(new Insets(20));
//        tab.setStyle("-fx-background-color: " + DARK_BG + ";");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 10; -fx-padding: 15;");
//
//        TextField prodIdField = createStyledTextField("Product ID");
//        TextField prodNameField = createStyledTextField("Product Name");
//        TextField manufacturerField = createStyledTextField("Manufacturer");
//        TextField supplierField = createStyledTextField("Supplier");
//        TextField quantityField = createStyledTextField("Quantity");
//        TextField priceField = createStyledTextField("Price");
//
//        Label prodIdLabel = new Label("Product ID:");
//        Label prodNameLabel = new Label("Product Name:");
//        Label manufacturerLabel = new Label("Manufacturer:");
//        Label supplierLabel = new Label("Supplier:");
//        Label quantityLabel = new Label("Quantity:");
//        Label priceLabel = new Label("Price:");
//
//        styleLabel(prodIdLabel);
//        styleLabel(prodNameLabel);
//        styleLabel(manufacturerLabel);
//        styleLabel(supplierLabel);
//        styleLabel(quantityLabel);
//        styleLabel(priceLabel);
//
//        form.add(prodIdLabel, 0, 0);
//        form.add(prodIdField, 1, 0);
//        form.add(prodNameLabel, 0, 1);
//        form.add(prodNameField, 1, 1);
//        form.add(manufacturerLabel, 0, 2);
//        form.add(manufacturerField, 1, 2);
//        form.add(supplierLabel, 0, 3);
//        form.add(supplierField, 1, 3);
//        form.add(quantityLabel, 0, 4);
//        form.add(quantityField, 1, 4);
//        form.add(priceLabel, 0, 5);
//        form.add(priceField, 1, 5);
//
//        Button addBtn = createStyledButton("➕ Add Product", SUCCESS_COLOR);
//        addBtn.setOnAction(e -> showAlert("Success", "Product added successfully!"));
//
//        tab.getChildren().addAll(form, addBtn);
//        return tab;
//    }
//
//    private VBox createPurchasesTab() {
//        VBox tab = new VBox(15);
//        tab.setPadding(new Insets(20));
//        tab.setStyle("-fx-background-color: " + DARK_BG + ";");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 10; -fx-padding: 15;");
//
//        TextField ssnField = createStyledTextField("Customer SSN");
//        TextField prodIdField = createStyledTextField("Product ID");
//        DatePicker datePicker = new DatePicker(LocalDate.now());
//        datePicker.setStyle("-fx-control-inner-background: #3a3a3a; -fx-text-fill: " + TEXT_COLOR + ";");
//
//        Label ssnLabel = new Label("Customer SSN:");
//        Label prodLabel = new Label("Product ID:");
//        Label dateLabel = new Label("Purchase Date:");
//
//        styleLabel(ssnLabel);
//        styleLabel(prodLabel);
//        styleLabel(dateLabel);
//
//        form.add(ssnLabel, 0, 0);
//        form.add(ssnField, 1, 0);
//        form.add(prodLabel, 0, 1);
//        form.add(prodIdField, 1, 1);
//        form.add(dateLabel, 0, 2);
//        form.add(datePicker, 1, 2);
//
//        Button purchaseBtn = createStyledButton("🛒 Record Purchase", SUCCESS_COLOR);
//        purchaseBtn.setOnAction(e -> showAlert("Success", "Purchase recorded successfully!"));
//
//        tab.getChildren().addAll(form, purchaseBtn);
//        return tab;
//    }
//
//    private VBox createReturnsTab() {
//        VBox tab = new VBox(15);
//        tab.setPadding(new Insets(20));
//        tab.setStyle("-fx-background-color: " + DARK_BG + ";");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 10; -fx-padding: 15;");
//
//        TextField ssnField = createStyledTextField("Customer SSN");
//        TextField prodIdField = createStyledTextField("Product ID");
//        DatePicker purchaseDatePicker = new DatePicker(LocalDate.now());
//        DatePicker returnDatePicker = new DatePicker(LocalDate.now());
//
//        purchaseDatePicker.setStyle("-fx-control-inner-background: #3a3a3a; -fx-text-fill: " + TEXT_COLOR + ";");
//        returnDatePicker.setStyle("-fx-control-inner-background: #3a3a3a; -fx-text-fill: " + TEXT_COLOR + ";");
//
//        Label ssnLabel = new Label("Customer SSN:");
//        Label prodLabel = new Label("Product ID:");
//        Label purchaseDateLabel = new Label("Purchase Date:");
//        Label returnDateLabel = new Label("Return Date:");
//
//        styleLabel(ssnLabel);
//        styleLabel(prodLabel);
//        styleLabel(purchaseDateLabel);
//        styleLabel(returnDateLabel);
//
//        form.add(ssnLabel, 0, 0);
//        form.add(ssnField, 1, 0);
//        form.add(prodLabel, 0, 1);
//        form.add(prodIdField, 1, 1);
//        form.add(purchaseDateLabel, 0, 2);
//        form.add(purchaseDatePicker, 1, 2);
//        form.add(returnDateLabel, 0, 3);
//        form.add(returnDatePicker, 1, 3);
//
//        Button returnBtn = createStyledButton("↩️ Process Return", SUCCESS_COLOR);
//        returnBtn.setOnAction(e -> showAlert("Return", "Return processed! Refund: $150.00"));
//
//        tab.getChildren().addAll(form, returnBtn);
//        return tab;
//    }
//
//    private VBox createPaymentTab() {
//        VBox tab = new VBox(15);
//        tab.setPadding(new Insets(20));
//        tab.setStyle("-fx-background-color: " + DARK_BG + ";");
//
//        GridPane form = new GridPane();
//        form.setHgap(15);
//        form.setVgap(15);
//        form.setStyle("-fx-background-color: #2a2a2a; -fx-border-radius: 10; -fx-padding: 15;");
//
//        TextField ssnField = createStyledTextField("Customer SSN");
//        TextField prodIdField = createStyledTextField("Product ID");
//        DatePicker datePicker = new DatePicker(LocalDate.now());
//        datePicker.setStyle("-fx-control-inner-background: #3a3a3a; -fx-text-fill: " + TEXT_COLOR + ";");
//
//        Label ssnLabel = new Label("Customer SSN:");
//        Label prodLabel = new Label("Product ID:");
//        Label dateLabel = new Label("Payment Date:");
//
//        styleLabel(ssnLabel);
//        styleLabel(prodLabel);
//        styleLabel(dateLabel);
//
//        form.add(ssnLabel, 0, 0);
//        form.add(ssnField, 1, 0);
//        form.add(prodLabel, 0, 1);
//        form.add(prodIdField, 1, 1);
//        form.add(dateLabel, 0, 2);
//        form.add(datePicker, 1, 2);
//
//        Button paymentBtn = createStyledButton("💳 Apply Payment", SUCCESS_COLOR);
//        paymentBtn.setOnAction(e -> showAlert("Success", "Payment processed successfully!"));
//
//        tab.getChildren().addAll(form, paymentBtn);
//        return tab;
//    }
//
//    private void showProductsView() {
//        VBox productsView = new VBox(15);
//        productsView.setStyle("-fx-background-color: " + DARK_BG + ";");
//        productsView.setPadding(new Insets(30));
//
//        Label title = new Label("Products Inventory");
//        title.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        TableView<ProductData> table = createProductsTable();
//
//        productsView.getChildren().addAll(title, table);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(productsView);
//    }
//
//    private TableView<ProductData> createProductsTable() {
//        TableView<ProductData> table = new TableView<>();
//        table.setStyle("-fx-control-inner-background: #2a2a2a; -fx-table-cell-border-color: #3a3a3a;");
//
//        TableColumn<ProductData, String> idCol = new TableColumn<>("ID");
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<ProductData, String> nameCol = new TableColumn<>("Name");
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<ProductData, String> manufacturerCol = new TableColumn<>("Manufacturer");
//        manufacturerCol.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
//
//        TableColumn<ProductData, String> supplierCol = new TableColumn<>("Supplier");
//        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
//
//        TableColumn<ProductData, Integer> quantityCol = new TableColumn<>("Quantity");
//        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//
//        TableColumn<ProductData, Double> priceCol = new TableColumn<>("Price");
//        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
//
//        ObservableList<ProductData> products = FXCollections.observableArrayList(
//            new ProductData("P001", "Laptop", "Dell", "TechStore", 10, 5000),
//            new ProductData("P002", "Mouse", "Logitech", "TechStore", 50, 150),
//            new ProductData("P003", "Monitor", "Samsung", "DisplayStore", 20, 2500)
//        );
//
//        table.setItems(products);
//        table.getColumns().addAll(idCol, nameCol, manufacturerCol, supplierCol, quantityCol, priceCol);
//        return table;
//    }
//
//    private void showPurchasesView() {
//        VBox purchasesView = new VBox(15);
//        purchasesView.setStyle("-fx-background-color: " + DARK_BG + ";");
//        purchasesView.setPadding(new Insets(30));
//
//        Label title = new Label("Purchase History");
//        title.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        TableView<PurchaseData> table = new TableView<>();
//        table.setStyle("-fx-control-inner-background: #2a2a2a; -fx-table-cell-border-color: #3a3a3a;");
//
//        TableColumn<PurchaseData, String> ssnCol = new TableColumn<>("Customer SSN");
//        ssnCol.setCellValueFactory(new PropertyValueFactory<>("ssn"));
//
//        TableColumn<PurchaseData, String> prodCol = new TableColumn<>("Product ID");
//        prodCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
//
//        TableColumn<PurchaseData, String> dateCol = new TableColumn<>("Purchase Date");
//        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
//
//        TableColumn<PurchaseData, String> statusCol = new TableColumn<>("Status");
//        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
//
//        ObservableList<PurchaseData> purchases = FXCollections.observableArrayList(
//            new PurchaseData("1234567890", "P001", "2024-10-15", "Paid"),
//            new PurchaseData("0987654321", "P002", "2024-10-18", "Pending")
//        );
//
//        table.setItems(purchases);
//        table.getColumns().addAll(ssnCol, prodCol, dateCol, statusCol);
//
//        purchasesView.getChildren().addAll(title, table);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(purchasesView);
//    }
//
//    private void showEmployeesTable() {
//        VBox employeesView = new VBox(15);
//        employeesView.setStyle("-fx-background-color: " + DARK_BG + ";");
//        employeesView.setPadding(new Insets(30));
//
//        Label title = new Label("Employees List");
//        title.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 24; -fx-font-weight: bold;");
//
//        TableView<EmployeeData> table = new TableView<>();
//        table.setStyle("-fx-control-inner-background: #2a2a2a; -fx-table-cell-border-color: #3a3a3a;");
//
//        TableColumn<EmployeeData, String> idCol = new TableColumn<>("ID");
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<EmployeeData, String> nameCol = new TableColumn<>("Name");
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<EmployeeData, String> emailCol = new TableColumn<>("Email");
//        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
//
//        TableColumn<EmployeeData, String> phoneCol = new TableColumn<>("Phone");
//        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
//
//        ObservableList<EmployeeData> employees = FXCollections.observableArrayList(
//            new EmployeeData("E001", "Ahmed Ali", "ahmed@email.com", "01012345678"),
//            new EmployeeData("E002", "Fatma Ibrahim", "fatma@email.com", "01098765432")
//        );
//
//        table.setItems(employees);
//        table.getColumns().addAll(idCol, nameCol, emailCol, phoneCol);
//
//        employeesView.getChildren().addAll(title, table);
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(employeesView);
//    }
//
//    private Button createStyledButton(String text, String color) {
//        Button button = new Button(text);
//        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: " + TEXT_COLOR +
//                        "; -fx-font-size: 14; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + adjustBrightness(color) +
//                                                      "; -fx-text-fill: " + TEXT_COLOR +
//                                                      "; -fx-font-size: 14; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: " + TEXT_COLOR +
//                                                     "; -fx-font-size: 14; -fx-padding: 10 20; -fx-border-radius: 5; -fx-cursor: hand;"));
//        return button;
//    }
//
//    private Button createSidebarButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
//        Button button = new Button(text);
//        button.setPrefWidth(180);
//        button.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: " + TEXT_COLOR +
//                        "; -fx-font-size: 13; -fx-padding: 10; -fx-border-radius: 5; -fx-cursor: hand;");
//        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: " + TEXT_COLOR +
//                                                      "; -fx-font-size: 13; -fx-padding: 10; -fx-border-radius: 5; -fx-cursor: hand;"));
//        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: " + TEXT_COLOR +
//                                                     "; -fx-font-size: 13; -fx-padding: 10; -fx-border-radius: 5; -fx-cursor: hand;"));
//        button.setOnAction(handler);
//        return button;
//    }
//
//    private TextField createStyledTextField(String prompt) {
//        TextField field = new TextField();
//        field.setPromptText(prompt);
//        field.setStyle("-fx-control-inner-background: #3a3a3a; -fx-text-fill: " + TEXT_COLOR +
//                       "; -fx-prompt-text-fill: #999999; -fx-font-size: 12; -fx-padding: 8;");
//        return field;
//    }
//
//    private void styleLabel(Label label) {
//        label.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 12;");
//    }
//
//    private String adjustBrightness(String hexColor) {
//        return hexColor;
//    }
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // Data Classes
//    public static class ProductData {
//        private String id;
//        private String name;
//        private String manufacturer;
//        private String supplier;
//        private int quantity;
//        private double price;
//
//        public ProductData(String id, String name, String manufacturer, String supplier, int quantity, double price) {
//            this.id = id;
//            this.name = name;
//            this.manufacturer = manufacturer;
//            this.supplier = supplier;
//            this.quantity = quantity;
//            this.price = price;
//        }
//
//        public String getId() { return id; }
//        public String getName() { return name; }
//        public String getManufacturer() { return manufacturer; }
//        public String getSupplier() { return supplier; }
//        public int getQuantity() { return quantity; }
//        public double getPrice() { return price; }
//    }
//
//    public static class EmployeeData {
//        private String id;
//        private String name;
//        private String email;
//        private String phone;
//
//        public EmployeeData(String id, String name, String email, String phone) {
//            this.id = id;
//            this.name = name;
//            this.email = email;
//            this.phone = phone;
//        }
//
//        public String getId() { return id; }
//        public String getName() { return name; }
//        public String getEmail() { return email; }
//        public String getPhone() { return phone; }
//    }
//
//    public static class PurchaseData {
//        private String ssn;
//        private String productId;
//        private String date;
//        private String status;
//
//        public PurchaseData(String ssn, String productId, String date, String status) {
//            this.ssn = ssn;
//            this.productId = productId;
//            this.date = date;
//            this.status = status;
//        }
//
//        public String getSsn() { return ssn; }
//        public String getProductId() { return productId; }
//        public String getDate() { return date; }
//        public String getStatus() { return status; }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}