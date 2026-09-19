package com.example.cab302project.controller;

import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class HabitActivityDetailsController {
    private int activityId;
    private int habitId;
    private Runnable onFinished;
    private final IActivityDAO activityDAO;
    private final IHabitDAO habitDAO;
    private final IGoalDAO goalDao;


    public HabitActivityDetailsController() {
        activityDAO = new ActivityDAO();
        habitDAO = new HabitDAO();
        goalDao = new GoalDAO();
    }

    @FXML
    private Label habitNameLabel;

    @FXML
    private Text startDateText;

    @FXML
    private Text endDateText;

    @FXML
    private Text progressText;

    @FXML
    private Text habitStreakText;
    @FXML
    private Text goalText;

    @FXML
    private Text categoryText;

    @FXML
    private TextField progressUpdate;


    @FXML
    private HBox completeHabitActivityButtons;

    /**
     * Closes the current window.
     */
    private void close() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    /**
     * Handles the back button click event.
     */
    @FXML
    private void onBack() {
        close();
    }

    /**
     * Sets the activity ID and loads the activity details.
     *
     * @param activityId The ID of the activity.
     */
    public void setActivityId(int activityId) {
        this.activityId = activityId;
        LoadActivityDetails();
    }

    /**
     * Set the start and end date text
     *
     * @param activity the activity with start/end dates
     */
    private void setDateText(Activity activity) {
        String formattedStartDate = String.format("Activity Start Date: %tF", activity.getStartDateTime());
        String formattedEndDate = String.format("Activity Due Date: %tF", activity.getDueDateTime());
        startDateText.setText(formattedStartDate);
        endDateText.setText(formattedEndDate);
    }
    /**
     * Set the habit streak text
     *
     * @param habit the habit
     */
    private void setHabitStreakText(Habit habit) {
        String streak = String.format("Current habit completion streak: %d", habitDAO.getHabitCompletionStreak(habit));
        habitStreakText.setText(streak);
    }

    /**
     * Set the goal name text
     *
     * @param goalId the id of goal (nullable)
     */
    private void setGoalNameText(Integer goalId) {
        if (goalId == null){
            goalText.setVisible(false);
            return;
        }
        goalText.setVisible(true);
        String goalName = "Goal: %s".formatted(goalDao.getGoalById(goalId).getTitle());
        goalText.setText(goalName);
    }


    /**
     * Set the progress text
     *
     * @param activity the activity with progress
     */
    private void setProgressText(Activity activity) {
        String formattedProgress = String.format("Progress: %d/%d", activity.getProgress(),
                activity.getCompletionThreshold());
        progressText.setText(formattedProgress);
    }

    private void setCategoryText(Activity activity) {
        String formattedCategory = String.format("Category: %s", activity.getCategory());
        categoryText.setText(formattedCategory);
    }

    /**
     * Handles the update progress button click event by updating the activity's progress and displayed text
     */
    @FXML
    private void onUpdateProgress() {
        Activity activity = activityDAO.getActivityById(activityId);
        Integer progressUpdate = Integer.parseInt(this.progressUpdate.getText());
        Integer currentProgress = activity.getProgress();
        activity.setProgress(currentProgress + progressUpdate);
        activityDAO.updateActivity(activity);
        setProgressText(activity);
    }

    /**
     * Handles the complete button click event by updating the activities's progress to the threshold and closing the dialog
     */
    @FXML
    private void onComplete() {
        Activity activity = activityDAO.getActivityById(activityId);
        Integer activityThreshold = activity.getCompletionThreshold();
        activity.setProgress(activityThreshold);
        activityDAO.updateActivity(activity);
        close();
    }

    /**
     * Handles the delete button click event by deleting the habit (first prompting the user to confirm) and closing the dialog
     */
    @FXML
    private void onDelete() {
        Activity activity = activityDAO.getActivityById(activityId);
        Habit habit = habitDAO.getHabitById(activity.getHabitId());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Habit");
        alert.setHeaderText("Are you sure you want to delete this habit?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            habitDAO.deleteHabit(habit);
            close();
        }
    }

    /**
     * Loads the activity details into the form
     */
    private void LoadActivityDetails() {
        Activity activity = activityDAO.getActivityById(activityId);
        Habit habit = habitDAO.getHabitById(habitId);
        habitNameLabel.setText(activity.getTitle());
        setDateText(activity);
        setProgressText(activity);
        setCategoryText(activity);
        setHabitStreakText(habit);
        if ((habit.getGoalId() != 0)){
            setGoalNameText(habit.getGoalId());
        }
//        completeHabitActivityButtons.setVisible(!activity.getIsComplete());
//        completeHabitActivityButtons.setManaged(!activity.getIsComplete());
    }

    /**
     * Sets what to run when the user finishes with this form.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId;
    }
}
