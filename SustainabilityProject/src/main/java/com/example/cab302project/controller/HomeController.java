package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class HomeController {

    @FXML
    private HBox goalsContainer;

    private final IGoalDAO goalDAO;

    private User currentUser;

    private List<Goal> userGoals;

    public HomeController() {
        goalDAO = new GoalDAO();
    }

    /**
     * Supplies the authenticated user to the home screen.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        syncGoals();
    }

    /**
     * Refreshes the goal cards using only goals belonging
     * to the currently authenticated user.
     */
    private void syncGoals() {

        goalsContainer.getChildren().clear();

        if (currentUser == null) {
            goalsContainer.setVisible(false);
            goalsContainer.setManaged(false);
            return;
        }

        userGoals = goalDAO.getGoalsForUser(
                currentUser.getUserId()
        );

        boolean hasGoals = !userGoals.isEmpty();

        if (hasGoals) {
            for (Goal goal : userGoals) {
                goalsContainer.getChildren().add(
                        createGoalCard(goal)
                );
            }
        }

        goalsContainer.setVisible(hasGoals);
        goalsContainer.setManaged(hasGoals);
    }

    /**
     * Creates a visual goal card for the supplied goal.
     */
    private VBox createGoalCard(Goal goal) {

        VBox goalCard = new VBox();

        goalCard.getStyleClass().add("item-card");
        goalCard.setSpacing(10);
        goalCard.setPrefHeight(250);
        goalCard.setPrefWidth(250);
        goalCard.setPadding(
                new Insets(10, 10, 0, 10)
        );

        Label categoryLabel =
                new Label(
                        goal.getCategory().toString()
                );

        categoryLabel
                .getStyleClass()
                .add("item-category");

        Label titleLabel =
                new Label(goal.getTitle());

        titleLabel
                .getStyleClass()
                .add("item-header");

        Label progressLabel =
                new Label(
                        String.format(
                                "Progress: %d/%d",
                                goal.getProgress(),
                                goal.getThreshold()
                        )
                );

        progressLabel
                .getStyleClass()
                .add("item-details");

        DateTimeFormatter endDateFormatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        String endDateFormat;
        if (goal.getDueDate() == null) {
            endDateFormat = "No end date";
        } else {
            endDateFormat = "End Date: "
                    + goal.getDueDate().format(endDateFormatter);
        }

        Label endDateLabel =
                new Label(endDateFormat);

        endDateLabel
                .getStyleClass()
                .add("item-details");

        Region spacer = new Region();

        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );

        Image image =
                new Image(
                        Objects.requireNonNull(
                                getClass()
                                        .getResourceAsStream(
                                                "/assets/plantPot.png"
                                        )
                        )
                );

        ImageView imageView =
                new ImageView(image);

        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);

        goalCard.getChildren().addAll(
                categoryLabel,
                titleLabel,
                progressLabel,
                endDateLabel,
                spacer,
                imageView
        );

        return goalCard;
    }

    /**
     * Opens the goal creation dialog, then refreshes the
     * authenticated user's goals once it closes.
     */
    @FXML
    private void onNewGoal() throws IOException {

        if (currentUser == null) {
            return;
        }

        FXMLLoader loader =
                new FXMLLoader(
                        HelloApplication.class.getResource(
                                "goal-creation-view.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        GoalCreationController controller =
                loader.getController();

        // Share the same DAO so the newly created goal
        // appears immediately on the home screen.
        controller.setGoalDAO(goalDAO);

        // Associate the new goal with the logged-in user.
        controller.setUserId(
                currentUser.getUserId()
        );

        Stage dialog = new Stage();

        dialog.setTitle("New Goal");
        dialog.initModality(
                Modality.APPLICATION_MODAL
        );

        dialog.setScene(scene);
        dialog.showAndWait();

        syncGoals();
    }

    @FXML
    private void initialize() {
        // User-specific data is loaded after LoginController
        // supplies the authenticated user.
    }
}