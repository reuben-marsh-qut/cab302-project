package com.example.cab302project.controller;

import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class HabitDetailsController {
    private int habitId;

    private Runnable onFinished;
    private final IActivityDAO activityDAO;
    private final IGoalDAO goalDAO;
    private final IHabitDAO habitDAO;

    public HabitDetailsController() {
        activityDAO = new ActivityDAO();
        habitDAO = new HabitDAO();
        goalDAO = new GoalDAO();
    }

    @FXML
    private Label habitNameLabel;

    @FXML
    private Text startDateText;

    @FXML
    private Text endDateText;

    @FXML
    private TextArea completionLog;
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


    /**
     * Set the start and end date text
     *
     * @param habit the habit with start/end dates
     */
    private void setDateText(Habit habit) {
        String formattedStartDate = String.format("Activity Start Date: %tF", habit.getStartDateTime());
        String formattedEndDate = String.format("Activity End Date: %tF", habit.getDueDateTime());
        startDateText.setText(formattedStartDate);
        endDateText.setText(formattedEndDate);
    }



    private void setCategoryText(Habit habit) {
        String formattedCategory = String.format("Category: %s", habit.getCategory());
        categoryText.setText(formattedCategory);
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
        String goalName = "Goal: %s".formatted(goalDAO.getGoalById(goalId).getTitle());
        habitStreakText.setText(goalName);
    }
    /**
     * Set the habit completion log text
     *
     * @param habit the habit
     */
    private void setHabitCompletionLog(Habit habit) {
        List<Activity> activities = habitDAO.getAllAssociatedTasks(habit).stream().filter(activity -> activity.getStartDateTime().isBefore(LocalDateTime.now())).toList();
        if (activities.size() == 0){
            completionLog.setText("Empty completion log, complete some habits to grow your mind and fill this box");
            return;
        }
        StringBuilder log = new StringBuilder();
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            log.append(String.format("Date:%tF %s Progress:%d/%d\n",
                    activity.getStartDateTime(),activity.isComplete() ? "Complete: ✔" : "Incomplete: ✘",activity.getProgress(),activity.getCompletionThreshold()));
        }
        completionLog.setText(log.toString());

    }


    /**
     * Handles the delete button click event by deleting the habit (first prompting the user to confirm) and closing the dialog
     */
    @FXML
    private void onDelete() {
        Habit habit = habitDAO.getHabitById(habitId);
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
     * Loads the habit details into the form
     */
    private void LoadHabitDetails() {

        Habit habit = habitDAO.getHabitById(habitId);
        habitNameLabel.setText(habit.getTitle());
        setDateText(habit);
        setCategoryText(habit);
        setHabitStreakText(habit);
        setHabitCompletionLog(habit);
        if ((habit.getGoalId() != 0)){
            setGoalNameText(habit.getGoalId());
        }
    }

    /**
     * Sets what to run when the user finishes with this form.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId;
        LoadHabitDetails();
    }

}
