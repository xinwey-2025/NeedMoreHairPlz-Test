package main.java.com.journalapp.controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Objects;

// IMPORT YOUR VIEWS
import main.java.com.journalapp.controller.LoginView;
import main.java.com.journalapp.controller.SignUpView;
import main.java.com.journalapp.controller.DashboardController;
import main.java.com.journalapp.controller.JournalPageController;
import main.java.com.journalapp.controller.EntryEditorController;

public class MainController extends Application {

    private Stage primaryStage;
    private BorderPane mainAppLayout;

    // --- SIDEBAR STATE ---
    private VBox sidebar;
    private boolean isCollapsed = false;

    // Buttons declared here so we can change them in toggleSidebar()
    private Button homeBtn, newEntryBtn, journalsBtn, logoutBtn, settingsBtn, collapseBtn;

    // --- THE VIEWS ---
    private LoginView loginView;
    private SignUpView signUpView;
    private DashboardController dashboardView;
    private JournalPageController journalView;
    private EntryEditorController entryEditorView;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // 1. Initialize all views
        loginView = new LoginView(this);
        signUpView = new SignUpView(this);
        dashboardView = new DashboardController();
        journalView = new JournalPageController();
        entryEditorView = new EntryEditorController();

        // 2. Start with Login
        Scene scene = new Scene(loginView.getView(), 1000, 700);

        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/app_icon(1).png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Icon not found.");
        }

        primaryStage.setTitle("More Hair Journaling");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ==========================================
    // NAVIGATION
    // ==========================================

    public void showLoginView() { primaryStage.getScene().setRoot(loginView.getView()); }
    public void showSignUpView() { primaryStage.getScene().setRoot(signUpView.getView()); }

    public void showMainApp() {
        if (mainAppLayout == null) createMainAppLayout();
        primaryStage.getScene().setRoot(mainAppLayout);
        loadDashboard();
    }

    public void logout() { showLoginView(); }

    private void loadDashboard() { mainAppLayout.setCenter(dashboardView.getView()); }
    private void loadJournals() { mainAppLayout.setCenter(journalView.getView()); }
    private void loadNewEntry() { mainAppLayout.setCenter(entryEditorView.getView()); }

    // ==========================================
    // LAYOUT & SIDEBAR
    // ==========================================

    private void createMainAppLayout() {
        mainAppLayout = new BorderPane();
        mainAppLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #CFE3F3, #FAD0C4);");
        createSidebar();
        mainAppLayout.setLeft(sidebar);
    }

    private void createSidebar() {
        sidebar = new VBox(20);
        sidebar.setPadding(new Insets(40, 20, 40, 30));
        sidebar.setPrefWidth(220);
        sidebar.setAlignment(Pos.TOP_LEFT);

        // --- DEFINE BUTTONS ---
        homeBtn = createMenuButton("⌂", "Home");
        homeBtn.setOnAction(e -> loadDashboard());

        newEntryBtn = createMenuButton("⊕", "New Entry");
        newEntryBtn.setOnAction(e -> loadNewEntry());

        journalsBtn = createMenuButton("d", "Journals");
        journalsBtn.setOnAction(e -> loadJournals());

        logoutBtn = createMenuButton("→", "Log out");
        logoutBtn.setOnAction(e -> logout());

        settingsBtn = createMenuButton("⚙", "Settings");

        // --- COLLAPSE / RETURN BUTTON ---
        collapseBtn = createMenuButton("≡", "Collapse");
        collapseBtn.setOnAction(e -> toggleSidebar());

        Region spacerMiddle = new Region();
        VBox.setVgrow(spacerMiddle, Priority.ALWAYS);

        sidebar.getChildren().addAll(
                homeBtn, newEntryBtn, journalsBtn,
                spacerMiddle,
                logoutBtn, settingsBtn,
                new Region() {{ setMinHeight(30); }},
                collapseBtn
        );
    }

    // ==========================================
    // COLLAPSE LOGIC (THE FIX)
    // ==========================================

    private void toggleSidebar() {
        isCollapsed = !isCollapsed;

        if (isCollapsed) {
            // --- STATE: COLLAPSED (Mini Mode) ---
            sidebar.setPrefWidth(70);
            sidebar.setAlignment(Pos.TOP_CENTER);
            sidebar.setPadding(new Insets(40, 10, 40, 10));

            // Hide text, keep icons
            updateButtonText(homeBtn, "⌂", "");
            updateButtonText(newEntryBtn, "⊕", "");
            updateButtonText(journalsBtn, "d", "");
            updateButtonText(logoutBtn, "→", "");
            updateButtonText(settingsBtn, "⚙", "");

            // CHANGE COLLAPSE BUTTON TO "EXPAND" ICON
            // Use "»" to show it goes back to the right
            updateButtonText(collapseBtn, "»", "");
            collapseBtn.setTooltip(new Tooltip("Expand Sidebar"));

        } else {
            // --- STATE: EXPANDED (Full Mode) ---
            sidebar.setPrefWidth(220);
            sidebar.setAlignment(Pos.TOP_LEFT);
            sidebar.setPadding(new Insets(40, 20, 40, 30));

            // Show text
            updateButtonText(homeBtn, "⌂", "Home");
            updateButtonText(newEntryBtn, "⊕", "New Entry");
            updateButtonText(journalsBtn, "d", "Journals");
            updateButtonText(logoutBtn, "→", "Log out");
            updateButtonText(settingsBtn, "⚙", "Settings");

            // CHANGE BUTTON BACK TO "COLLAPSE" ICON
            updateButtonText(collapseBtn, "≡", "Collapse");
            collapseBtn.setTooltip(new Tooltip("Collapse Sidebar"));
        }
    }

    private void updateButtonText(Button btn, String icon, String text) {
        if (text.isEmpty()) {
            btn.setText(icon); // Icon Only
        } else {
            btn.setText(icon + "   " + text); // Icon + Text
        }
    }

    // ==========================================
    // STYLE HELPER
    // ==========================================

    private Button createMenuButton(String iconText, String labelText) {
        Button btn = new Button(iconText + "   " + labelText);

        String baseStyle = "-fx-background-color: transparent; -fx-text-fill: #1a1a1a; -fx-font-size: 16px; -fx-cursor: hand;";
        btn.setStyle(baseStyle + "-fx-alignment: CENTER_LEFT;");

        // Hover Effects (Centers the icon if collapsed)
        btn.setOnMouseEntered(e -> {
            String align = isCollapsed ? "CENTER" : "CENTER_LEFT";
            btn.setStyle(baseStyle + "-fx-background-color: rgba(255,255,255,0.4); -fx-background-radius: 5; -fx-alignment: " + align + ";");
        });

        btn.setOnMouseExited(e -> {
            String align = isCollapsed ? "CENTER" : "CENTER_LEFT";
            btn.setStyle(baseStyle + "-fx-alignment: " + align + ";");
        });

        btn.setMaxWidth(Double.MAX_VALUE);

        // Initial Tooltip
        btn.setTooltip(new Tooltip(labelText));

        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}