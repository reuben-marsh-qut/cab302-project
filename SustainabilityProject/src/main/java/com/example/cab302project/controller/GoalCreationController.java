package com.example.cab302project.controller;

import com.example.cab302project.model.IGoalDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for the goal creation page. Collects the details of a new goal
 * and stores it using the DAO supplied by the calling screen.
 */
public class GoalCreationController {
    @FXML
    private TextArea titleArea;
    @FXML
    private TextField targetField;
    @FXML
    private Button mindButton;
    @FXML
    private Button bodyButton;
    @FXML
    private Button worldButton;
    @FXML
    private Button workOverTimeButton;
    @FXML
    private Button oneAndDoneButton;
    @FXML
    private HBox templatesContainer;
    @FXML
    private Label errorLabel;

    private IGoalDAO goalDAO;
    private int userId;
    private Runnable onFinished;

    public void setGoalDAO(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Sets what to run when the user finishes with this form,
     * whether by creating a goal or cancelling.
     */
    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    @FXML
    public void initialize() {
        hideError();
    }

    @FXML
    private void onSelectMind() {
    }

    @FXML
    private void onSelectBody() {
    }

    @FXML
    private void onSelectWorld() {
    }

    @FXML
    private void onWorkOverTime() {
    }

    @FXML
    private void onOneAndDone() {
    }

    @FXML
    private void onCreateGoal() {
        // Wired up in a later commit, once category and
        // completion type can actually be selected.
    }

    @FXML
    private void onCancel() {
        close();
    }

    /**
     * Converts the target field's text into a whole number.
     * @param text The text typed into the target field.
     * @return The number, or null if the text is not a whole number.
     */
    private Integer parseTarget(String text) {
        if (text == null) {
            return null;
        }
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private void close() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}