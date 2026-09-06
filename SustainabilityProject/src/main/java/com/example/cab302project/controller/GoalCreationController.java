package com.example.cab302project.controller;

import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.enums.Category;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class GoalCreationController {

    @FXML private TextField titleField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField targetField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker dueDatePicker;

    private IGoalDAO goalDAO;
    private int userId;

    public void setGoalDAO(IGoalDAO goalDAO) { this.goalDAO = goalDAO; }
    public void setUserId(int userId) { this.userId = userId; }

    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(Category.values()));
        categoryComboBox.getSelectionModel().selectFirst();
        startDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void onCancel() {
    }

    @FXML
    private void onCreateGoal() {
    }
}