package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

    /**
     * Builds a goal from the form's contents, stores it, and closes the dialog.
     */
    @FXML
    private void onCreateGoal() {
        String title = titleField.getText();
        Category category = categoryComboBox.getValue();
        int target = Integer.parseInt(targetField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        // A new goal always starts at zero progress and incomplete
        Goal goal = new Goal(userId, title, category, startDate, dueDate,
                0, target, CompletionType.PROGRESSIVE, false);

        goalDAO.addGoal(goal);
        close();
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
}