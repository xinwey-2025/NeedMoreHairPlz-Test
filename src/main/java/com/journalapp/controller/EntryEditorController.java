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

// Call weather and mood analyzer API
import main.java.com.journalapp.model.Entry;
import main.java.com.journalapp.util.UserEntries;
import main.java.com.journalapp.util.Weather;
import main.java.com.journalapp.util.MoodAnalyzer;

public class EntryEditorController {

    private Label condition;
    private String fixWeather = "Loading...";

    private static String currentUsername = "default";
    private String username;

    private UserEntries userEntries;
    private String entryId = null;

    public EntryEditorController() {
        userEntries = new UserEntries(currentUsername);
    }

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
        condition = new Label("Weather: Loading... | Mood: Analyzing...");
        condition.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        condition.setStyle("-fx-text-fill: #666;");

        // Run the API call in the BACKGROUND so the app doesn't freeze
        CompletableFuture.runAsync(() -> {
            // 1. Fetch data (Slow operation)
            this.fixWeather = Weather.getCurrentWeather();

            // 2. Update UI (Must be on JavaFX Thread)
            Platform.runLater(() -> {
//                condition.setText("Weather: " + currentWeather + " | Mood: (Pending)");
                updateStatus("Analyzing...");
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

        // 4. Button list
        Button[] actionButton = {new  Button("Save Entry"), new Button("Edit Entry"), new Button("Delete Entry")};
        Button save = actionButton[0];
        Button edit = actionButton[1];
        Button delete = actionButton[2];

        edit.setVisible(false);
        delete.setVisible(false);

        String btnStyle = "-fx-background-color: #3498db;" +
                          "-fx-text-fill: white;" +
                          "-fx-font-weight: bold;" +
                          "-fx-font-size: 14px;" +
                          "-fx-background-radius: 20;" +
                          "-fx-cursor: hand;" +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);";

        for (Button btn  : actionButton) {
            btn.setPrefWidth(150);
            btn.setPrefHeight(35);
            btn.setStyle(btnStyle);

            btn.managedProperty().bind(btn.visibleProperty());
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().addAll(save, edit, delete);

        save.setOnAction(e -> {
            String content = textArea.getText().trim();
            if (content.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please write something first!");
                alert.showAndWait();
                return;
            }

            save.setDisable(true);
            save.setText("Analyzing mood...");

            CompletableFuture.supplyAsync(() -> {
                // Call mood analyzer API
                return MoodAnalyzer.analyze(content);
            }).thenAccept(mood -> {
                // Update UI on the JavaFX thread
                Platform.runLater(() -> {
                    try {
                        updateStatus(mood);

                        if (this.entryId == null) {
                            userEntries.createEntry(today, content, mood, fixWeather);

                            for (Entry entry : userEntries.listEntries()) {
                                if (entry.getDate().equals(today) &&
                                        entry.getContent().equals(content) &&
                                        entry.getMood().equals(mood)) {
                                    this.entryId = entry.getId();
                                    break;
                                }
                            }
                        } else {
                            userEntries.editEntry(this.entryId, today, content, mood, fixWeather);
                        }

                        save.setVisible(false);
                        edit.setVisible(true);
                        delete.setVisible(true);
                        textArea.setEditable(false);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Journal saved successfully!");
                        alert.showAndWait();
                    } finally {
                        save.setText("Save Entry");
                        save.setDisable(false);
                    }
                });
            });

        });

        edit.setOnAction(e -> {
            textArea.setEditable(true);
            textArea.setFocusTraversable(true); // Allow cursor focus
            textArea.requestFocus();

            edit.setVisible(false);
            save.setVisible(true);
        });

        delete.setOnAction(e -> {
            if (this.entryId != null) {
                userEntries.deleteEntry(this.entryId);
            }

            textArea.clear();
            this.entryId = null;

            save.setVisible(true);
            save.setText("Save Entry");
            edit.setVisible(false);
            delete.setVisible(false);

            textArea.setEditable(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Journal deleted successfully!");
            alert.showAndWait();
        });

        layout.getChildren().addAll(header, infoLine, textArea, buttonBox);
        return layout;
    }

    private void updateStatus(String mood) {
        condition.setText("Weather: " + fixWeather + " | Mood: " + mood);
    }
}