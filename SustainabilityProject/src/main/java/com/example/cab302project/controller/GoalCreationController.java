package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;


public class GoalCreationController {
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField targetField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker dueDatePicker;

    private IGoalDAO goalDAO;
    private int userId;


    public void setGoalDAO(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @FXML
    public void initialize() {
        // Fill the dropdown from the enum so new categories appear here automatically
        categoryComboBox.setItems(FXCollections.observableArrayList(Category.values()));
        categoryComboBox.getSelectionModel().selectFirst();
        startDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void onCreateGoal() {
        hideError();

        String title = titleField.getText();
        Category category = categoryComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (dueDate == null) {
            showError("Please choose a due date.");
            return;
        }

        Integer target = parseTarget(targetField.getText());
        if (target == null) {
            showError("Target must be a whole number, for example 600.");
            return;
        }

        try {
            // A new goal always starts at zero progress and incomplete
            Goal goal = new Goal(userId, title, category, startDate, dueDate,
                    0, target, CompletionType.PROGRESSIVE, false);
            goalDAO.addGoal(goal);
            close();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }


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
    /**
     * Discards the form and closes the dialog without creating anything.
     */
    @FXML
    private void onCancel() {
        close();
    }

    /**
     * Closes the window this dialog is displayed in.
     */
    private void close() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays a validation message on the form.
     * @param message The message to show the user.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    /**
     * Hides any validation message currently on the form.
     */
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private Label errorLabel;


}