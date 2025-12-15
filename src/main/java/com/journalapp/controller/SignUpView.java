package main.java.com.journalapp.controller;

import main.java.com.journalapp.controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SignUpView {
    private final MainController mainController;
    private final VBox mainContainer;

    public SignUpView(MainController mainController) {
        this.mainController = mainController;

        mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #CFE3F3, #FAD0C4);");
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxWidth(Double.MAX_VALUE);
        mainContainer.setMaxHeight(Double.MAX_VALUE);

        VBox formContainer = new VBox(15);
        formContainer.setMaxWidth(350);
        formContainer.setAlignment(Pos.CENTER);

        //Glass Style
        formContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.4);" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: rgba(255, 255, 255, 0.8);" +
                "-fx-border-radius: 15;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 40;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 4);"
        );

        Label titleLabel = new Label("Create Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        String inputStyle = "-fx-background-color: rgba(255,255,255,0.7); " +
                "-fx-background-radius: 5; " +
                "-fx-padding: 10; -fx-font-size: 14px; " +
                "-fx-border-color: rgba(0,0,0,0.1); " +
                "-fx-border-radius: 5;";

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");
        emailField.setStyle(inputStyle);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Create Password");
        passField.setStyle(inputStyle);

        Button signUpBtn = new Button("Sign Up");
        signUpBtn.setStyle(
                "-fx-background-color: #2ecc71; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand;"
        );
        signUpBtn.setMaxWidth(Double.MAX_VALUE);

        // Hover Effect
        signUpBtn.setOnMouseEntered(e -> signUpBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));
        signUpBtn.setOnMouseExited(e -> signUpBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));

        signUpBtn.setOnAction(e -> {
            System.out.println("Sign Up Successful");
            // Auto-login after signup, or go back to login
            mainController.showMainApp();
        });

        Button loginLink = new Button("Already have an account? Login");
        loginLink.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-cursor: hand;");
        loginLink.setOnAction(e -> mainController.showLoginView());

        formContainer.getChildren().addAll(titleLabel, emailField, passField, signUpBtn, loginLink);

        mainContainer.getChildren().add(formContainer);
    }

    public VBox getView() {
        return mainContainer;
    }
}