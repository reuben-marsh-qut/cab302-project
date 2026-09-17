package com.example.cab302project.controller;

// hope you dont mind the fact I just used the goal controller and repurposed it
// I also changed the formatting cause I like mine better


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

public class ActivityController {

    @FXML
    private HBox incompleteActivitiesContainer;

    @FXML
    private HBox completeActivitiesContainer;

    private final IActivityDAO activityDAO;

    private User currentUser;

    private List<Activity> incompleteUserActivities;

    private List<Activity> completedUserActivities;

    public ActivityController() {
        activityDAO = new ActivityDAO();
    }

    @FXML
    private Node activitiesPanel;

    @FXML
    private BorderPane contentPane;

    /**
     * Refreshes the activity cards using only activities belonging
     * to the currently authenticated user.
     */
    private void syncActivities() {

        incompleteActivitiesContainer.getChildren().clear();
        completeActivitiesContainer.getChildren().clear();

        if (currentUser == null) {
            incompleteActivitiesContainer.setVisible(false);
            incompleteActivitiesContainer.setManaged(false);
            completeActivitiesContainer.setVisible(false);
            completeActivitiesContainer.setManaged(false);
            return;
        }

        incompleteUserActivities = activityDAO.getIncompletedActivitiesForUser(
                currentUser.getUserId()
        );

        completedUserActivities = activityDAO.getCompletedActivitiesForUser(
                currentUser.getUserId()
        );

        boolean hasIncompleteActivities = !incompleteUserActivities.isEmpty();
        boolean hasCompletedActivities = !completedUserActivities.isEmpty();

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

        if (hasCompletedActivities)
        {
            for (Activity activity : completedUserActivities)
            {
                completeActivitiesContainer.getChildren().add(activityCard(activity));
            }
        }
        else
        {
            completeActivitiesContainer.getChildren().add(noCompleteActivityText());
        }

        incompleteActivitiesContainer.setVisible(true);
        incompleteActivitiesContainer.setManaged(true);

        completeActivitiesContainer.setVisible(true);
        completeActivitiesContainer.setManaged(true);
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
     * If there are no completed activities, this message is displayed
     * @return no completed activities label
     */
    private Label noCompleteActivityText()
    {
        return new Label("No completed activities yet. Complete your activities and they will appear here!");
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

        Button seeMoreButton = new Button("See More");
        seeMoreButton.getStyleClass().add("home-nav-buttons");
        seeMoreButton.setOnAction(event ->
            {
                try
                {
                    handleSeeMoreButton(activity.getId());
                }
                catch(IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        );

        Region Hspacer = new Region();

        HBox.setHgrow(Hspacer, Priority.ALWAYS);

        HBox imageButtonContainer = new HBox();
        imageButtonContainer.getChildren().addAll(imageView, Hspacer, seeMoreButton);

        activityCard.getChildren().addAll( categoryLabel, titleLabel, progressLabel, endDateLabel, spacer, imageButtonContainer);
        return activityCard;
    }

    /**
     * @param activityId the id of the activity to investigate
     * @throws IOException exception thrown if the activity details view cannot be loaded
     */
    private void handleSeeMoreButton(Integer activityId) throws IOException {
        if (currentUser == null)
        {
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("activity-details-view.fxml"));
        Node activityDetailsView = loader.load();

        ActivityDetailsController controller = loader.getController();
        controller.setActivityId(activityId);
        controller.setOnFinished(this::showActivitiesPanel);

        contentPane.setCenter(activityDetailsView);
    }

    /**
     * Opens the activity creation dialog, then refreshes the
     * authenticated user's activities once it closes.
     */
    @FXML
    private void onNewActivity() throws IOException {
        if (currentUser == null)
        {
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("activity-creation-view.fxml"));
        Node activityCreationPanel = loader.load();

        ActivityCreationController controller = loader.getController();
        controller.setActivityDAO(activityDAO);
        controller.setUserId(currentUser.getUserId()); // REFACTOR: could we replace this sort of thing just using the singleton instead
        controller.setOnFinished(this::showActivitiesPanel);

        contentPane.setCenter(activityCreationPanel);    }

    /**
     * Returns to the activities panel and refreshes the list.
     */
    private void showActivitiesPanel() {
        contentPane.setCenter(activitiesPanel);
        syncActivities();
    }

    /**
     * when the activity-view is loaded, this is called
     */
    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        activitiesPanel = contentPane.getCenter();
        syncActivities();
    }
}