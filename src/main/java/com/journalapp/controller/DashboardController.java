package main.java.com.journalapp.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DashboardController {

    // Return the "Content" part only (The right side)
    // The Sidebar will be handled globally by MainController so it stays persistent
    public VBox getView() {
        // --- MAIN CONTENT (Center) ---
        VBox content = new VBox();
        content.setPadding(new Insets(60, 0, 0, 40));
        content.setAlignment(Pos.TOP_LEFT);

        Label greetingLabel = new Label("Good morning, Jane.");
        greetingLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 32));
        greetingLabel.setTextFill(Color.web("#1a1a1a"));

        content.getChildren().add(greetingLabel);

        return content;
    }
}