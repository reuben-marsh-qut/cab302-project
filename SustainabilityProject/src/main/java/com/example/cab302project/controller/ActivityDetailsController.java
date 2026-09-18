package com.example.cab302project.controller;

import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityDAO;
import com.example.cab302project.model.IActivityDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Optional;


public class ActivityDetailsController {
    private int activityId;
    private Runnable onFinished;
    private final IActivityDAO activityDAO;

    public ActivityDetailsController() {
        activityDAO = new ActivityDAO();
    }

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
    private TextField progressUpdate;

    @FXML
    private HBox completeActivityButtons;

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
        String formattedEndDate = String.format("Activity End Date: %tF", activity.getDueDateTime());
        startDateText.setText(formattedStartDate);
        endDateText.setText(formattedEndDate);
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
     * Handles the delete button click event by deleting the activity (first prompting the user to confirm) and closing the dialog
     */
    @FXML
    private void onDelete() {
        Activity activity = activityDAO.getActivityById(activityId);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Activity");
        alert.setHeaderText("Are you sure you want to delete this activity?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            activityDAO.deleteActivity(activity);
            close();
        }
    }

    /**
     * Loads the activity details into the form
     */
    private void LoadActivityDetails() {
        Activity activity = activityDAO.getActivityById(activityId);
        activityNameLabel.setText(activity.getTitle());
        setDateText(activity);
        setProgressText(activity);
        setCategoryText(activity);
        completeActivityButtons.setVisible(!activity.getIsComplete());
        completeActivityButtons.setManaged(!activity.getIsComplete());
    }

    /**
     * Sets what to run when the user finishes with this form.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
