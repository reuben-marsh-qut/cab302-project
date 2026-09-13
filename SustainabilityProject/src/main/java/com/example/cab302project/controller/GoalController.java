package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class GoalController {

    @FXML
    private HBox incompleteGoalsContainer;

    @FXML
    private HBox completeGoalsContainer;

    private final IGoalDAO goalDAO;

    private User currentUser;

    private List<Goal> incompleteUserGoals;

    private List<Goal> completedUserGoals;

    public GoalController() {
        goalDAO = new GoalDAO();
    }

    @FXML
    private Node goalsPanel;

    @FXML
    private BorderPane contentPane;

    /**
     * Refreshes the goal cards using only goals belonging
     * to the currently authenticated user.
     */
    private void syncGoals() {

        incompleteGoalsContainer.getChildren().clear();
        completeGoalsContainer.getChildren().clear();

        if (currentUser == null) {
            incompleteGoalsContainer.setVisible(false);
            incompleteGoalsContainer.setManaged(false);
            completeGoalsContainer.setVisible(false);
            completeGoalsContainer.setManaged(false);
            return;
        }

        incompleteUserGoals = goalDAO.getIncompletedGoalsForUser(
                currentUser.getUserId()
        );

        completedUserGoals = goalDAO.getCompletedGoalsForUser(
                currentUser.getUserId()
        );

        boolean hasIncompleteGoals = !incompleteUserGoals.isEmpty();
        boolean hasCompletedGoals = !completedUserGoals.isEmpty();

        if (hasIncompleteGoals) {
            for (Goal goal : incompleteUserGoals) {
                incompleteGoalsContainer.getChildren().add(
                        goalCard(goal)
                );
            }
        } else {
            incompleteGoalsContainer.getChildren().add(
                    noGoalText()
            );
        }

        if (hasCompletedGoals) {
            for (Goal goal : completedUserGoals) {
                completeGoalsContainer.getChildren().add(
                        goalCard(goal)
                );
            }
        } else {
            completeGoalsContainer.getChildren().add(
                    noCompleteGoalText()
            );
        }

        incompleteGoalsContainer.setVisible(true);
        incompleteGoalsContainer.setManaged(true);
        completeGoalsContainer.setVisible(true);
        completeGoalsContainer.setManaged(true);
    }

    private Label noGoalText() {
        return new Label("No goals yet. Set a goal to track your progress!");
    }

    private Label noCompleteGoalText() {
        return new Label("No completed goals yet. Achieve your goals and they will appear here!");
    }

    /**
     * Creates a visual goal card for the supplied goal.
     */
    private VBox goalCard(Goal goal) {

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

        Button seeMoreButton = new Button("See More");

        seeMoreButton
                .getStyleClass()
                .add("home-nav-buttons");

        seeMoreButton.setOnAction(event -> {
            try {
                handleSeeMoreButton(goal.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Region Hspacer = new Region();

        HBox.setHgrow(
                Hspacer,
                Priority.ALWAYS
        );

        HBox imageButtonContainer = new HBox();
        imageButtonContainer.getChildren().addAll(imageView, Hspacer, seeMoreButton);

        goalCard.getChildren().addAll(
                categoryLabel,
                titleLabel,
                progressLabel,
                endDateLabel,
                spacer,
                imageButtonContainer
        );

        return goalCard;
    }

    private void handleSeeMoreButton(Integer goalId) throws IOException {
        if (currentUser == null) {
            return;
        }



        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("goal-details-view.fxml"));
        Node goalDetailsView = loader.load();

        GoalDetailsController controller = loader.getController();
        controller.setGoalId(goalId);
        controller.setOnFinished(this::showGoalsPanel);

        contentPane.setCenter(goalDetailsView);
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

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("goal-creation-view.fxml"));
        Node goalCreationPanel = loader.load();

        GoalCreationController controller = loader.getController();
        controller.setGoalDAO(goalDAO);
        controller.setUserId(currentUser.getUserId()); // REFACTOR: could we replace this sort of thing just using the singleton instead
        controller.setOnFinished(this::showGoalsPanel);

        contentPane.setCenter(goalCreationPanel);    }

    /**
     * Returns to the goals panel and refreshes the list.
     */
    private void showGoalsPanel() {
        contentPane.setCenter(goalsPanel);
        syncGoals();
    }

    /**
     * when the goal-view is loaded, this is called
     */
    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        goalsPanel = contentPane.getCenter();
        syncGoals();
    }
}