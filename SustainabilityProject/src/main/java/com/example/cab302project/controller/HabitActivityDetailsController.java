package com.example.cab302project.controller;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.Optional;

public class HabitActivityDetailsController {

    private int activityId;
    private int habitId;
    private Runnable onFinished;

    private final IActivityDAO activityDAO;
    private final IHabitDAO habitDAO;
    private final IGoalDAO goalDao;

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
    private Label xpRewardLabel;

    @FXML
    private TextField progressUpdate;

    @FXML
    private HBox completeHabitActivityButtons;

    @FXML
    private VBox progressControls;

    @FXML
    private Button completeButton;

    public HabitActivityDetailsController() {
        activityDAO = new ActivityDAO();
        habitDAO = new HabitDAO();
        goalDao = new GoalDAO();
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId == null ? 0 : habitId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
        loadActivityDetails();
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    private void close() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    @FXML
    private void onBack() {
        close();
    }

    @FXML
    private void onUpdateProgress() {
        int additionalProgress;

        try {
            additionalProgress = Integer.parseInt(
                    progressUpdate.getText().trim()
            );

            if (additionalProgress <= 0) {
                showError("Enter a whole number greater than zero.");
                return;
            }
        } catch (NumberFormatException exception) {
            showError("Enter a whole number greater than zero.");
            return;
        }

        try {
            Activity activity = getOwnedActivity();

            if (activity == null) {
                return;
            }

            if (activity.isComplete()) {
                loadActivityDetails();
                return;
            }

            int newProgress = Math.addExact(
                    activity.getProgress(),
                    additionalProgress
            );

            saveProgressAndAwardXp(activity, newProgress, false);
        } catch (ArithmeticException exception) {
            showError("That progress value is too large.");
        } catch (RuntimeException exception) {
            showError("Could not load the habit activity. Please try again.");
        }
    }

    @FXML
    private void onComplete() {
        try {
            Activity activity = getOwnedActivity();

            if (activity == null) {
                return;
            }

            // Preserve progress when retrying an unclaimed reward.
            int newProgress = Math.max(
                    activity.getProgress(),
                    activity.getCompletionThreshold()
            );

            saveProgressAndAwardXp(activity, newProgress, true);
        } catch (RuntimeException exception) {
            showError("Could not load the habit activity. Please try again.");
        }
    }

    private void saveProgressAndAwardXp(
            Activity activity,
            int newProgress,
            boolean closeWhenFinished
    ) {
        try {
            if (activity.getProgress() != newProgress) {
                activity.setProgress(newProgress);
                activityDAO.updateActivity(activity);
            }
        } catch (RuntimeException exception) {
            showError(
                    "Could not save activity progress. "
                            + "Please reopen the activity and try again."
            );
            return;
        }

        int earnedXp;

        try {
            ExperienceDAO experienceDAO = new ExperienceDAO(
                    DatabaseConnection.getInstance()
            );

            earnedXp = experienceDAO.awardXp(
                    UserSession.getInstance().getUser().getUserId(),
                    activityId
            );
        } catch (SQLException | RuntimeException exception) {
            // Progress is saved separately from the XP transaction.
            showError(
                    "Your progress was saved, but the XP award could not "
                            + "be confirmed. Use the completion button again "
                            + "to retry. An already saved reward will not "
                            + "be awarded twice."
            );
            return;
        }

        if (earnedXp > 0) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Habit activity completed",
                    "You earned " + earnedXp + " XP!"
            );
        }

        try {
            UserSession session = UserSession.getInstance();

            User refreshedUser = new UserDAO().getUserById(
                    session.getUser().getUserId()
            );

            if (refreshedUser == null) {
                showError(
                        "Your changes were saved, but your account "
                                + "could not be refreshed. Please sign in again."
                );
                return;
            }

            session.setUser(refreshedUser);

            if (closeWhenFinished) {
                close();
            } else {
                progressUpdate.clear();
                loadActivityDetails();
            }
        } catch (RuntimeException exception) {
            showError(
                    "Your changes were saved, but the screen could not "
                            + "be refreshed. Please reopen the activity."
            );
        }
    }

    private Activity getOwnedActivity() {
        User user = UserSession.getInstance().getUser();

        if (user == null) {
            showError("Please sign in before changing an activity.");
            return null;
        }

        Activity activity = activityDAO.getActivityById(activityId);

        if (activity == null) {
            showError("This activity could not be found.");
            return null;
        }

        if (!Integer.valueOf(user.getUserId()).equals(activity.getUserId())) {
            showError("This activity does not belong to your account.");
            return null;
        }

        if (habitId <= 0
                || !Integer.valueOf(habitId).equals(activity.getHabitId())) {
            showError("This activity does not belong to the selected habit.");
            return null;
        }

        return activity;
    }

    private void loadActivityDetails() {
        Activity activity = getOwnedActivity();

        if (activity == null) {
            return;
        }

        Habit habit = habitDAO.getHabitById(habitId);

        if (habit == null
                || !activity.getUserId().equals(habit.getUserId())) {
            showError("The associated habit could not be loaded.");
            return;
        }

        habitNameLabel.setText(activity.getTitle());
        categoryText.setText("Category: " + activity.getCategory());

        startDateText.setText(
                "Activity Start Date: "
                        + activity.getStartDateTime().toLocalDate()
        );

        endDateText.setText(
                activity.getDueDateTime() == null
                        ? "Activity Due Date: No due date"
                        : "Activity Due Date: "
                        + activity.getDueDateTime().toLocalDate()
        );

        progressText.setText(
                "Progress: " + activity.getProgress()
                        + "/" + activity.getCompletionThreshold()
        );

        habitStreakText.setText(
                "Current habit completion streak: "
                        + habitDAO.getHabitCompletionStreak(habit)
        );

        setGoalNameText(habit.getGoalId());

        boolean complete = activity.isComplete();
        boolean rewardPending = complete
                && activity.getBaseXpReward() > 0
                && activity.getAwardedXpReward() == 0;

        if (activity.getAwardedXpReward() > 0) {
            xpRewardLabel.setText(
                    "XP earned: " + activity.getAwardedXpReward()
            );
        } else if (rewardPending) {
            xpRewardLabel.setText(
                    "Reward pending: " + activity.getBaseXpReward()
                            + " XP — retry the award below."
            );
        } else {
            xpRewardLabel.setText(
                    "Completion reward: " + activity.getBaseXpReward() + " XP"
            );
        }

        progressControls.setVisible(!complete);
        progressControls.setManaged(!complete);

        boolean showActions = !complete || rewardPending;
        completeHabitActivityButtons.setVisible(showActions);
        completeHabitActivityButtons.setManaged(showActions);

        completeButton.setText(
                rewardPending ? "Retry XP Award" : "Mark as Complete"
        );
    }

    private void setGoalNameText(Integer goalId) {
        if (goalId == null || goalId == 0) {
            goalText.setVisible(false);
            goalText.setManaged(false);
            return;
        }

        Goal goal = goalDao.getGoalById(goalId);

        if (goal == null) {
            goalText.setVisible(false);
            goalText.setManaged(false);
            return;
        }

        goalText.setText("Goal: " + goal.getTitle());
        goalText.setVisible(true);
        goalText.setManaged(true);
    }

    @FXML
    private void onDelete() {
        try {
            Activity activity = getOwnedActivity();

            if (activity == null) {
                return;
            }

            Habit habit = habitDAO.getHabitById(activity.getHabitId());

            if (habit == null
                    || !activity.getUserId().equals(habit.getUserId())) {
                showError("The associated habit could not be loaded.");
                return;
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Habit");
            alert.setHeaderText("Are you sure you want to delete this habit?");
            alert.setContentText("This action cannot be undone.");
            alert.initOwner(habitNameLabel.getScene().getWindow());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                habitDAO.deleteHabit(habit);
                close();
            }
        } catch (RuntimeException exception) {
            showError("Could not delete the habit. Please try again.");
        }
    }

    private void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Habit activity update", message);
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(habitNameLabel.getScene().getWindow());
        alert.showAndWait();
    }
}