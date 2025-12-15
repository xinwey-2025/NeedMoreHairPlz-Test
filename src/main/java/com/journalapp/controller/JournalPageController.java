package main.java.com.journalapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;

public class JournalPageController {

    private final HashMap<LocalDate, String> diaryMap = new HashMap<>();
    private final ObservableList<LocalDate> diaryDates = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm a");

    public JournalPageController() {
        // Initialize Mock Data
        diaryMap.put(LocalDate.of(2025, 11, 24), "Lorem ipsum dolor sit amet\nMore text...");
        diaryMap.put(LocalDate.of(2025, 11, 23), "Another reflective entry here...");
        diaryMap.put(LocalDate.of(2025, 11, 22), "A sunny day with positive mood.");

        diaryDates.addAll(diaryMap.keySet());
        FXCollections.sort(diaryDates, Comparator.reverseOrder());
    }

    public VBox getView() {
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(30, 50, 30, 50));

        // NOTE: We do NOT set a background color here.
        // This allows the MainController's pink/blue gradient to show through!

        // Page Title
        Label pageTitle = new Label("Journals");
        pageTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: 300; -fx-text-fill: #333333;");

        Separator separator = new Separator();
        // Make separator slightly visible but subtle
        separator.setOpacity(0.4);

        // Scroll Pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        // Make the ScrollPane and its viewport completely transparent
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Journal List Container
        VBox journalListContainer = new VBox(20);
        journalListContainer.setPadding(new Insets(10, 0, 0, 0));
        journalListContainer.setStyle("-fx-background-color: transparent;"); // Transparent container

        for (LocalDate date : diaryDates) {
            String content = diaryMap.get(date);
            journalListContainer.getChildren().add(createJournalCard(date, content));
        }

        scrollPane.setContent(journalListContainer);
        contentBox.getChildren().addAll(pageTitle, separator, scrollPane);

        return contentBox;
    }

    private VBox createJournalCard(LocalDate date, String content) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15, 20, 15, 20));

        // --- STYLE CONSTANTS ---
        // 1. Normal State: Very transparent white (Glass effect) with a thin border
        String styleNormal = "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: rgba(255, 255, 255, 0.6);" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 1px;";

        // 2. Hover State: Slightly darker/more opaque to look "active"
        String styleHover = "-fx-background-color: rgba(255, 255, 255, 0.6);" +
                "-fx-background-radius: 15px;" +
                "-fx-border-color: #ffffff;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-width: 1px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"; // Add shadow on hover

        // Apply initial style
        card.setStyle(styleNormal);

        // --- HOVER INTERACTION ---
        // When mouse enters, switch to Hover Style
        card.setOnMouseEntered(e -> {
            card.setStyle(styleHover);
            card.setCursor(javafx.scene.Cursor.HAND); // Change cursor to hand to show it's clickable
        });

        // When mouse leaves, switch back to Normal Style
        card.setOnMouseExited(e -> {
            card.setStyle(styleNormal);
        });

        // --- CARD CONTENT ---
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label timestampLabel = new Label(date.atTime(14, 39).format(dateFormatter));
        timestampLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #222222;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label metadataLabel = new Label("Mood: Positive | Weather: Sunny");
        metadataLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

        header.getChildren().addAll(timestampLabel, spacer, metadataLabel);

        // Truncate text for preview
        String previewText = content.replace("\n", " ");
        if (previewText.length() > 60) previewText = previewText.substring(0, 60) + "...";

        Label contentLabel = new Label(previewText);
        contentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #444444; -fx-padding-top: 5px;");
        contentLabel.setWrapText(true);

        card.getChildren().addAll(header, contentLabel);
        return card;
    }
}