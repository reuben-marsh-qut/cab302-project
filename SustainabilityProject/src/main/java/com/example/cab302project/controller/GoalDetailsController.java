package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.GoalDAO;
import com.example.cab302project.model.IGoalDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Optional;


public class GoalDetailsController {
    private int goalId;
    private Runnable onFinished;
    private final IGoalDAO goalDAO;
    public GoalDetailsController() {
        goalDAO = new GoalDAO();
    }

    @FXML
    private Label goalNameLabel;

    @FXML
    private Text startDateText;

    @FXML
    private Text endDateText;

    @FXML
    private Text progressText;

    @FXML
    private TextField progressUpdate;

    private void close() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    @FXML
    private void onBack() {
        close();
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
        LoadGoalDetails();
    }

    private void setDateText(Goal goal) {
        String formattedStartDate = String.format("Goal Start Date: %tF", goal.getStartDate());
        String formattedEndDate = String.format("Goal End Date: %tF", goal.getDueDate());
        startDateText.setText(formattedStartDate);
        endDateText.setText(formattedEndDate);
    }

    private void setProgressText(Goal goal) {
        String formattedProgress = String.format("Progress: %d/%d", goal.getProgress(),
                goal.getThreshold());
        progressText.setText(formattedProgress);
    }

    @FXML
    private void onUpdateProgress() {
        Goal goal = goalDAO.getGoalById(goalId);
        Integer progressUpdate = Integer.parseInt(this.progressUpdate.getText());
        Integer currentProgress = goal.getProgress();
        goal.setProgress(currentProgress + progressUpdate);
        goalDAO.updateGoal(goal);
        setProgressText(goal);
    }

    @FXML
    private void onComplete() {
        Goal goal = goalDAO.getGoalById(goalId);
        Integer goalThreshold = goal.getThreshold();
        goal.setProgress(goalThreshold);
        goalDAO.updateGoal(goal);
        close();
    }


    @FXML
    private void onDelete() {
        Goal goal = goalDAO.getGoalById(goalId);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Goal");
        alert.setHeaderText("Are you sure you want to delete this goal?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            goalDAO.deleteGoal(goal);
            close();
        }
    }

    private void LoadGoalDetails() {
        Goal goal = goalDAO.getGoalById(goalId);
        goalNameLabel.setText(goal.getTitle());
        setDateText(goal);
        setProgressText(goal);
    }

    /**
     * Sets what to run when the user finishes with this form.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
