package com.example.cab302project.controller;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityDAO;
import com.example.cab302project.model.ExperienceDAO;
import com.example.cab302project.model.IActivityDAO;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserDAO;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.Optional;

public class ActivityDetailsController {

    private int activityId;
    private Runnable onFinished;
    private final IActivityDAO activityDAO;

    @FXML
    private Label activityNameLabel;

    @FXML
    private Text startDateText;

    @FXML
    private Text endDateText;

    @FXML
    private Text progressText;

    @FXML
    private Text categoryText;

    @FXML
    private Label xpRewardLabel;

    @FXML
    private TextField progressUpdate;

    @FXML
    private HBox completeActivityButtons;

    @FXML
    private VBox progressControls;

    @FXML
    private Button completeButton;

    public ActivityDetailsController() {
        activityDAO = new ActivityDAO();
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
            showError("Could not load the activity. Please try again.");
        }
    }

    @FXML
    private void onComplete() {
        try {
            Activity activity = getOwnedActivity();

            if (activity == null) {
                return;
            }

            // Preserve existing progress when retrying an unclaimed reward.
            int newProgress = Math.max(
                    activity.getProgress(),
                    activity.getCompletionThreshold()
            );

            saveProgressAndAwardXp(activity, newProgress, true);
        } catch (RuntimeException exception) {
            showError("Could not load the activity. Please try again.");
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
            // Progress and the XP award are separate operations.
            // Keep the screen open so the reward can be retried.
            showError(
                    "Your progress was saved, but the XP award could not "
                            + "be confirmed. Click Mark as Complete again "
                            + "to retry. An already saved reward will not "
                            + "be awarded twice."
            );
            return;
        }

        if (earnedXp > 0) {
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Activity completed",
                    "You earned " + earnedXp + " XP!"
            );
        }

        try {
            // Refresh the session from the saved database total.
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

        return activity;
    }

    private void loadActivityDetails() {
        Activity activity = getOwnedActivity();

        if (activity == null) {
            return;
        }

        activityNameLabel.setText(activity.getTitle());
        categoryText.setText("Category: " + activity.getCategory());

        startDateText.setText(
                "Activity Start Date: "
                        + activity.getStartDateTime().toLocalDate()
        );

        endDateText.setText(
                activity.getDueDateTime() == null
                        ? "Activity End Date: No due date"
                        : "Activity End Date: "
                        + activity.getDueDateTime().toLocalDate()
        );

        progressText.setText(
                "Progress: " + activity.getProgress()
                        + "/" + activity.getCompletionThreshold()
        );

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
        completeActivityButtons.setVisible(showActions);
        completeActivityButtons.setManaged(showActions);

        completeButton.setText(
                rewardPending ? "Retry XP Award" : "Mark as Complete"
        );
    }

    @FXML
    private void onDelete() {
        try {
            Activity activity = getOwnedActivity();

            if (activity == null) {
                return;
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Activity");
            alert.setHeaderText("Are you sure you want to delete this activity?");
            alert.setContentText("This action cannot be undone.");
            alert.initOwner(activityNameLabel.getScene().getWindow());

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                activityDAO.deleteActivity(activity);
                close();
            }
        } catch (RuntimeException exception) {
            showError("Could not delete the activity. Please try again.");
        }
    }

    private void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Activity update", message);
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
        alert.initOwner(activityNameLabel.getScene().getWindow());
        alert.showAndWait();
    }
}