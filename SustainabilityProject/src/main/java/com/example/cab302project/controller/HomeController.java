package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class HomeController {

    @FXML
    private HBox incompleteGoalsContainer;

    @FXML
    private HBox incompleteActivitiesContainer;

    @FXML
    private Button goalsButton;

    @FXML
    private Button activitiesButton;

    private final IGoalDAO goalDAO;
    private final IActivityDAO activityDAO;

    private User currentUser;

    private List<Goal> incompleteUserGoals;
    private List<Activity> incompleteUserActivities;

    public HomeController() {
        goalDAO = new GoalDAO();
        activityDAO = new ActivityDAO();
    }

    @FXML
    private Node activitiesPanel;

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
        incompleteActivitiesContainer.getChildren().clear();

        if (currentUser == null) {
            incompleteGoalsContainer.setVisible(false);
            incompleteGoalsContainer.setManaged(false);
            incompleteActivitiesContainer.setVisible(false);
            incompleteActivitiesContainer.setManaged(false);
            return;
        }

        incompleteUserGoals = goalDAO.getIncompletedGoalsForUser(
                currentUser.getUserId()
        );

        incompleteUserActivities = activityDAO.getIncompletedActivitiesForUser(
                currentUser.getUserId()
        );

        boolean hasIncompleteGoals = !incompleteUserGoals.isEmpty();
        boolean hasIncompleteActivities = !incompleteUserActivities.isEmpty();

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

        if (hasIncompleteActivities)
        {
            for (Activity activity : incompleteUserActivities)
            {
                incompleteActivitiesContainer.getChildren().add(activityCard(activity));
            }
        }
        else
        {
            incompleteActivitiesContainer.getChildren().add(noActivityText());
        }

        incompleteGoalsContainer.setVisible(true);
        incompleteGoalsContainer.setManaged(true);
        incompleteActivitiesContainer.setVisible(true);
        incompleteActivitiesContainer.setManaged(true);
    }

    /**
     * If there are no current activities, this message is displayed
     * @return no current activities label
     */
    private Label noActivityText()
    {
        return new Label("No activities yet. Set an activity to track your progress!");
    }

    /**
     * If there are no current goals, this message is displayed
     * @return no current goals label
     */
    private Label noGoalText() {
        return new Label("No goals yet. Set a goal to track your progress!");
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

        Label statusLabel = new Label("Not achieved");

        statusLabel
                .getStyleClass()
                .add("goal-missed");

        statusLabel.setVisible(goal.isNotAchieved());
        statusLabel.setManaged(goal.isNotAchieved());

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

        Region Hspacer = new Region();

        HBox.setHgrow(
                Hspacer,
                Priority.ALWAYS
        );

        HBox imageButtonContainer = new HBox();
        imageButtonContainer.getChildren().addAll(imageView, Hspacer);

        goalCard.getChildren().addAll(
                categoryLabel,
                titleLabel,
                progressLabel,
                endDateLabel,
                statusLabel,
                spacer,
                imageButtonContainer
        );

        return goalCard;
    }

    /**
     * Creates a visual activity card for the supplied activity.
     */
    private VBox activityCard(Activity activity)
    {

        VBox activityCard = new VBox();

        activityCard.getStyleClass().add("item-card");
        activityCard.setSpacing(10);
        activityCard.setPrefHeight(250);
        activityCard.setPrefWidth(250);
        activityCard.setPadding(new Insets(10, 10, 0, 10));

        Label categoryLabel = new Label(activity.getCategory().toString());

        categoryLabel.getStyleClass().add("item-category");

        Label titleLabel = new Label(activity.getTitle());

        titleLabel.getStyleClass().add("item-header");

        Label progressLabel = new Label(String.format("Progress: %d/%d", activity.getProgress(), activity.getCompletionThreshold()));

        progressLabel.getStyleClass().add("item-details");

        DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String endDateFormat;
        if (activity.getDueDateTime() == null)
        {
            endDateFormat = "No end date";
        }
        else
        {
            endDateFormat = "End Date: " + activity.getDueDateTime().format(endDateFormatter);
        }

        Label endDateLabel = new Label(endDateFormat);

        endDateLabel.getStyleClass().add("item-details");

        Region spacer = new Region();

        VBox.setVgrow(spacer, Priority.ALWAYS );

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/plantPot.png")));

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);

        Region Hspacer = new Region();

        HBox.setHgrow(Hspacer, Priority.ALWAYS);

        HBox imageButtonContainer = new HBox();
        imageButtonContainer.getChildren().addAll(imageView, Hspacer);

        activityCard.getChildren().addAll( categoryLabel, titleLabel, progressLabel, endDateLabel, spacer, imageButtonContainer);
        return activityCard;
    }

    @FXML
    protected void onGoalsButtonClick()
            throws IOException {

        openPage(
                goalsButton,
                "goal-view.fxml"
        );
    }

//    @FXML
//    protected void onHabitsButtonClick()
//            throws IOException {
//
//        /*
//         * There is no dedicated habits page yet.
//         * This preserves the behaviour from the old
//         * NavBarController.
//         */
//        openPage(
//                habitsButton,
//                "goal-view.fxml"
//        );
//    }

    @FXML
    protected void onActivitiesButtonClick()
            throws IOException {

        openPage(
                activitiesButton,
                "activity-view.fxml"
        );
    }

    /**
     * when the goal-view is loaded, this is called
     */
    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        goalsPanel = contentPane.getCenter();
        activitiesPanel = contentPane.getCenter();
        syncGoals();
    }

    private void openPage(
            Button sourceButton,
            String resource
    ) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(resource)
        );

        Parent root = loader.load();

        sourceButton.getScene().setRoot(root);
    }
}
