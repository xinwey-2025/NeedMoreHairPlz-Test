package main.java.com.journalapp.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

// Import your Weather utility
import main.java.com.journalapp.util.Weather;

public class EntryEditorController {

    public VBox getView() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setPadding(new Insets(40, 50, 40, 50));

        // 1. Header
        Label header = new Label("What's on your mind?");
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        header.setStyle("-fx-text-fill: #333;");

        // 2. Info Line (Date & Weather)
        LocalDate today = LocalDate.now();
        Label prompt = new Label("Entry for " + today + ":");
        prompt.setFont(Font.font("Arial", 14));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // --- WEATHER LOGIC START ---
        // Initial state: "Loading..."
        Label condition = new Label("Weather: Loading... | Mood: (Pending)");
        condition.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        condition.setStyle("-fx-text-fill: #666;");

        // Run the API call in the BACKGROUND so the app doesn't freeze
        CompletableFuture.runAsync(() -> {
            // 1. Fetch data (Slow operation)
            String currentWeather = Weather.getCurrentWeather();

            // 2. Update UI (Must be on JavaFX Thread)
            Platform.runLater(() -> {
                condition.setText("Weather: " + currentWeather + " | Mood: (Pending)");
            });
        });
        // --- WEATHER LOGIC END ---

        HBox infoLine = new HBox(10);
        infoLine.getChildren().addAll(prompt, spacer, condition);

        // 3. Text Area
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPromptText("Start writing your thoughts here...");

        // Glassy Style
        textArea.setStyle(
                "-fx-control-inner-background: rgba(255,255,255,0.5);" +
                        "-fx-background-color: transparent;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-color: rgba(255,255,255,0.6);" +
                        "-fx-border-radius: 5;"
        );

        VBox.setVgrow(textArea, Priority.ALWAYS);

        // 4. Save Button
        Button save = new Button("Save Entry");
        save.setPrefWidth(150);
        save.setPrefHeight(35);
        save.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        save.setOnAction(e -> {
            String content = textArea.getText().trim();
            if (content.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please write something first!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Journal saved successfully!");
                alert.showAndWait();
                textArea.clear();
            }
        });

        layout.getChildren().addAll(header, infoLine, textArea, save);
        return layout;
    }
}