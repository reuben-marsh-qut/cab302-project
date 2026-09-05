package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.MockGoalDAO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.text.Element;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomeController {
    @FXML
    private ListView<Goal> goalsListView;
    private IGoalDAO goalDAO;
    @FXML
    private Label goalCategory;
    @FXML
    private Label goalTitle;
    @FXML
    private Label goalProgress;
    @FXML
    private Label goalEndDate;
    @FXML
    private HBox goalsContainer;

    public HomeController() {
        goalDAO = new MockGoalDAO();
    }

    private final Integer USER_ID = 1;
    private List<Goal> userGoals;

    private void syncGoals() {
        goalsContainer.getChildren().clear();

        userGoals = goalDAO.getGoalsForUser(USER_ID);
        boolean hasGoal = !userGoals.isEmpty();

        if (hasGoal) {
            // add all of the user's goals to the goal container as children
            for (Goal goal : userGoals) {
                goalsContainer.getChildren().add(createGoalCard(goal));
            }
        }

        goalsContainer.setVisible(hasGoal);
        goalsContainer.setManaged(hasGoal);
    }

    private VBox createGoalCard(Goal goal)
    {
        VBox goalCard = new VBox();
        goalCard.getStyleClass().add("item-card");
        goalCard.setSpacing(10);
        goalCard.setPrefHeight(250);
        goalCard.setPrefWidth(250);

        goalCard.setPadding(new Insets(10, 10, 0, 10));

        Label categoryLabel = new Label(goal.getCategory().toString());
        categoryLabel.getStyleClass().add("item-category");

        Label titleLabel = new Label(goal.getTitle());
        titleLabel.getStyleClass().add("item-header");

        Label progressLabel = new Label(String.format("Progress: %d/%d", goal.getProgress(), goal.getThreshold()));
        progressLabel.getStyleClass().add("item-details");

        DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String endDateFormat = "End Date: " + goal.getDueDate().format(endDateFormatter);
        Label endDateLabel = new Label(endDateFormat);
        endDateLabel.getStyleClass().add("item-details");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Image image = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/assets/plantPot.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);

        goalCard.getChildren().addAll(categoryLabel, titleLabel, progressLabel, endDateLabel, spacer);

        return goalCard;
    }

    @FXML
    private void initialize() {
        syncGoals();
    }

}
