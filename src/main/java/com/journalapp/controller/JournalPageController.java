package main.java.com.journalapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.java.com.journalapp.model.Entry;
import main.java.com.journalapp.util.UserEntries;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class JournalPageController {

    private final UserEntries userEntries;
    private final ObservableList<Entry> diaryEntries = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");

    public JournalPageController(String username) {
        this.userEntries = new UserEntries(username);
        loadEntries();
    }

    // Load entry
    private void loadEntries() {
        diaryEntries.clear();
        List<Entry> entries = userEntries.listEntries();
        // 按日期倒序
        entries.sort(Comparator.comparing(Entry::getDate).reversed());
        diaryEntries.addAll(entries);
    }

    // Page reload
    public void refresh() {
        loadEntries();
    }

    public VBox getView() {
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(30, 50, 30, 50));

        Label pageTitle = new Label("Journals");
        pageTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 300; -fx-text-fill: #333333;");

        Separator separator = new Separator();
        separator.setOpacity(0.4);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox journalListContainer = new VBox(20);
        journalListContainer.setPadding(new Insets(10, 0, 0, 0));
        journalListContainer.setStyle("-fx-background-color: transparent;");

        // Entry Journal
        for (Entry entry : diaryEntries) {
            journalListContainer.getChildren().add(createJournalCard(entry));
        }

        scrollPane.setContent(journalListContainer);
        contentBox.getChildren().addAll(pageTitle, separator, scrollPane);

        return contentBox;
    }

    // 创建每条日记卡片
    private VBox createJournalCard(Entry entry) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15, 20, 15, 20));

        // Card Pattern
        String styleNormal = "-fx-background-color: rgba(255,255,255,0.3);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: rgba(255,255,255,0.6);" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 1px;";
        String styleHover = "-fx-background-color: rgba(255,255,255,0.6);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);";

        card.setStyle(styleNormal);
        card.setOnMouseEntered(e -> { card.setStyle(styleHover); card.setCursor(javafx.scene.Cursor.HAND); });
        card.setOnMouseExited(e -> card.setStyle(styleNormal));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label timestampLabel = new Label(entry.getDate().format(dateFormatter));
        timestampLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #222222;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label metadataLabel = new Label("Mood: " + entry.getMood() + " | Weather: " + entry.getWeather());
        metadataLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");
        header.getChildren().addAll(timestampLabel, spacer, metadataLabel);

        Label contentLabel = new Label(entry.getContent());
        contentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444; -fx-padding-top: 5px;");
        contentLabel.setWrapText(true);

        TextArea editArea = new TextArea(entry.getContent());
        editArea.setWrapText(true);
        editArea.setPrefHeight(80);
        editArea.setVisible(false);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        Button editBtn = new Button("Edit");
        Button saveBtn = new Button("Save");
        Button deleteBtn = new Button("Delete");
        saveBtn.setVisible(false);
        buttonBox.getChildren().addAll(editBtn, saveBtn, deleteBtn);

        // Edit Button
        editBtn.setOnAction(e -> {
            contentLabel.setVisible(false);
            editArea.setVisible(true);
            editBtn.setVisible(false);
            saveBtn.setVisible(true);
        });

        // Save Button
        saveBtn.setOnAction(e -> {
            String newContent = editArea.getText().trim();
            if (!newContent.isEmpty()) {
                UserEntries.editEntry(entry.getId(), entry.getDate(), newContent, entry.getMood(), entry.getWeather());
                refresh();
            }
        });

        // Delete Button
        deleteBtn.setOnAction(e -> {
            UserEntries.deleteEntry(entry.getId());
            refresh();
        });

        card.getChildren().addAll(header, contentLabel, editArea, buttonBox);
        return card;
    }
}

