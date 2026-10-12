package com.example.cab302project.controller;

// hope you dont mind the fact I just used the goal controller and repurposed it
// I also changed the formatting cause I like mine better


import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class HabitController {

    @FXML
    private HBox todayHabitsContainer;

    @FXML
    private HBox completeHabitsContainer;

    private final IActivityDAO activityDAO;
    private final IGoalDAO goalDAO;
    private final IUserDAO userDAO;
    private final IHabitDAO habitDAO;

    private User currentUser;

    private List<Habit> todayHabits;

    private List<Habit> completedUserHabits;

    public HabitController() {
        activityDAO = new ActivityDAO();
        goalDAO = new GoalDAO();
        habitDAO = new HabitDAO();
        userDAO = new UserDAO();
    }

    @FXML
    private Node habitsPanel;

    @FXML
    private BorderPane contentPane;

    /**
     * Refreshes the habit cards using only habits belonging
     * to the currently authenticated user.
     */
    private void syncHabits() {

        todayHabitsContainer.getChildren().clear();
        completeHabitsContainer.getChildren().clear();

        if (currentUser == null) {
            todayHabitsContainer.setVisible(false);
            todayHabitsContainer.setManaged(false);
            completeHabitsContainer.setVisible(false);
            completeHabitsContainer.setManaged(false);
            return;
        }
        ZoneId timezone = ZoneId.systemDefault();

        todayHabits = habitDAO.getHabitsByUserId(currentUser.getUserId()).stream().
                filter(habit->
                    LocalDateTime.now().isAfter(habit.getStartDateTime().atStartOfDay()) && LocalDateTime.now().isBefore(habit.getDueDateTime().atStartOfDay().plusDays(1).minusSeconds(1))
                ).toList();

        completedUserHabits = habitDAO.getHabitsByUserId(
                currentUser.getUserId()
        );

        boolean hasIncompleteActivities = !todayHabits.isEmpty();
        boolean hasCompletedActivities = !completedUserHabits.isEmpty();

        if (hasIncompleteActivities)
        {
            for (Habit habit : todayHabits)
            {
                todayHabitsContainer.getChildren().add(habitCard(habit,true));
            }
        }
        else
        {
            todayHabitsContainer.getChildren().add(noHabitsText());
        }

        if (hasCompletedActivities)
        {
            for (Habit habit : completedUserHabits)
            {
                if (habitDAO.getAllAssociatedTasks(habit).stream().filter(activity -> activity.isComplete()).toList().size() != 0){
                    completeHabitsContainer.getChildren().add(habitCard(habit,false));
                }
            }
        }
        else
        {
            completeHabitsContainer.getChildren().add(noCompleteHabitsText());
        }

        todayHabitsContainer.setVisible(true);
        todayHabitsContainer.setManaged(true);

        completeHabitsContainer.setVisible(true);
        completeHabitsContainer.setManaged(true);
    }

    /**
     * If there are no current habits, this message is displayed
     * @return no current habits label
     */
    private Label noHabitsText()
    {
        return new Label("No habits yet. Create habits to grow a healthier you!");
    }

    /**
     * If there are no completed habits, this message is displayed
     * @return no completed habits label
     */
    private Label noCompleteHabitsText()
    {
        return new Label("No complete habits yet. Complete your habits each day and they will appear here!");
    }


    private VBox habitCard(Habit habit, boolean openCompleteAssociatedActivityView)
    {

        VBox habitCard = new VBox();

        habitCard.getStyleClass().add("item-card");
        habitCard.setSpacing(10);
        habitCard.setPrefHeight(250);
        habitCard.setPrefWidth(250);
        habitCard.setPadding(new Insets(10, 10, 0, 10));

        Label categoryLabel = new Label(habit.getCategory().toString());

        categoryLabel.getStyleClass().add("item-category");

        Circle streakCircle = new Circle(12);
        streakCircle.getStyleClass().add("item-streak-circle");
        Label streakNumber = new Label(Integer.toString(habitDAO.getHabitCompletionStreak(habit)));
        streakNumber.getStyleClass().add("item-streak-text");
        StackPane streakContainer = new StackPane();
        streakContainer.getChildren().add(streakCircle);
        streakContainer.getChildren().add(streakNumber);

        HBox headerContainer = new HBox(); // https://stackoverflow.com/questions/79083216/aligning-nodes-in-hbox-to-left-and-right
        headerContainer.setAlignment(Pos.CENTER);
        Region horizontalSpacer = new Region();
        HBox.setHgrow(horizontalSpacer, Priority.ALWAYS);
        headerContainer.getChildren().addAll(categoryLabel,horizontalSpacer,streakContainer);

        Label titleLabel = new Label(habit.getTitle());

        titleLabel.getStyleClass().add("item-header");
        Activity currentActivity = habitDAO.getCurrentAssociatedTask(habit);
        // TODO: Fix
        Label progressLabel = new Label(String.format("Progress: %d/%d", currentActivity==null ? 0 : currentActivity.getProgress() , habit.getCompletionThreshold()));

        progressLabel.getStyleClass().add("item-details");
        String howOften = "unknown";
        switch (habit.getRepeatFrequencyType()){
            case DAILY -> howOften = "days";
            case WEEKLY -> howOften = "weeks";
            case MONTHLY -> howOften = "months";
            case YEARLY -> howOften = "years";
        }

        Label frequencyLabel = new Label(String.format("Frequency: every %d %s",habit.getRepeatFrequency(),howOften));

        frequencyLabel.getStyleClass().add("item-details");

        DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String endDateFormat;
        if (habit.getDueDateTime() == null)
        {
            endDateFormat = "No due date";
        }
        else
        {
            endDateFormat = "Due Date: " + habit.getDueDateTime().format(endDateFormatter);
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

        Button seeMoreButton = new Button();
        seeMoreButton.getStyleClass().add("home-nav-buttons");
        if (openCompleteAssociatedActivityView){
            seeMoreButton.setText("Complete habit task");
            seeMoreButton.setOnAction(event ->
                    {
                        try
                        {
                            handleSeeMoreButtonActivity(habitDAO.getCurrentAssociatedTask(habit).getId(),habit.getId());
                        }
                        catch(IOException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } else {
            seeMoreButton.setText("View Habit History");
            seeMoreButton.setOnAction(event ->
                    {
                        try
                        {
                            handleSeeMoreButtonHabit(habit.getId());
                        }
                        catch(IOException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }


        Region Hspacer = new Region();

        HBox.setHgrow(Hspacer, Priority.ALWAYS);

        HBox imageButtonContainer = new HBox();
        imageButtonContainer.getChildren().addAll(imageView, Hspacer, seeMoreButton);

        habitCard.getChildren().addAll( headerContainer, titleLabel, progressLabel, frequencyLabel,endDateLabel, spacer, imageButtonContainer);
        return habitCard;
    }

    /**
     * @param activityId the id of the activity of associated with the habit
     * @throws IOException exception thrown if the activity details view cannot be loaded
     */
    private void handleSeeMoreButtonActivity(Integer activityId, Integer habitId) throws IOException {
        if (currentUser == null)
        {
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("habit-activity-details-view.fxml"));
        Node activityDetailsView = loader.load();

        HabitActivityDetailsController controller = loader.getController();
        controller.setHabitId(habitId);
        controller.setActivityId(activityId);
        controller.setOnFinished(this::showHabitsPanel);

        contentPane.setCenter(activityDetailsView);
    }
    /**
     * @param habitId the id of the habit
     * @throws IOException exception thrown if the activity details view cannot be loaded
     */
    private void handleSeeMoreButtonHabit(Integer habitId) throws IOException {
        if (currentUser == null)
        {
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("habit-details-view.fxml"));
        Node activityDetailsView = loader.load();

        HabitDetailsController controller = loader.getController();
//        controller.setActivityId(activityId);
        controller.setHabitId(habitId);
        controller.setOnFinished(this::showHabitsPanel);

        contentPane.setCenter(activityDetailsView);
    }

    /**
     * Opens the habit creation dialog, then refreshes the
     * authenticated user's habits once it closes.
     */
    @FXML
    private void onNewHabit() throws IOException {
        if (currentUser == null)
        {
            return;
        }

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("habit-creation-view.fxml"));
        Node habitCreationPanel = loader.load();

        HabitCreationController controller = loader.getController();
        controller.setUserId(currentUser.getUserId()); // REFACTOR: could we replace this sort of thing just using the singleton instead
        controller.setUserDAO(userDAO); // REFACTOR: could we replace this sort of thing just using the singleton instead
        controller.setGoalDAO(goalDAO);
        controller.setHabitDAO(habitDAO);
        controller.setOnFinished(this::showHabitsPanel);

        contentPane.setCenter(habitCreationPanel);    }

    /**
     * Returns to the Habits panel and refreshes the list.
     */
    private void showHabitsPanel() {
        contentPane.setCenter(habitsPanel);
        syncHabits();
    }

    /**
     * when the activity-view is loaded, this is called
     */
    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        habitsPanel = contentPane.getCenter();
        syncHabits();
    }
}