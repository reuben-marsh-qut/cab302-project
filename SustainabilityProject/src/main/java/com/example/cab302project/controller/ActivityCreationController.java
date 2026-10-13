package com.example.cab302project.controller;

import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityFactory;
import com.example.cab302project.model.ActivityTemplate;
import com.example.cab302project.model.IActivityDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Collects the details of a new activity and stores it using
 * the DAO supplied by the calling screen.
 */
public class ActivityCreationController {

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

    @FXML
    private Button template1Button;

    @FXML
    private Button template2Button;

    @FXML
    private Button template3Button;

    private final ActivityFactory activityFactory = new ActivityFactory();

    private IActivityDAO activityDAO;
    private int userId;
    private int goalId;
    private int habitId;
    private Runnable onFinished;

    private static final String SELECTED_STYLE_CLASS = "option-selected";

    private Category selectedCategory;
    private TaskType selectedTaskType;
    private List<ActivityTemplate> shownTemplates;

    public void setActivityDAO(IActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    @FXML
    public void initialize() {
        hideError();
        selectCategory(Category.MIND);
        selectTaskType(TaskType.PROGRESSIVE);
    }

    @FXML
    private void onSelectMind() {
        selectCategory(Category.MIND);
    }

    @FXML
    private void onSelectBody() {
        selectCategory(Category.BODY);
    }

    @FXML
    private void onSelectWorld() {
        selectCategory(Category.WORLD);
    }

    @FXML
    private void onWorkOverTime() {
        selectTaskType(TaskType.PROGRESSIVE);
    }

    @FXML
    private void onOneAndDone() {
        selectTaskType(TaskType.BINARY);
    }

    @FXML
    private void onTemplate1() {
        applyTemplate(shownTemplates.get(0));
    }

    @FXML
    private void onTemplate2() {
        applyTemplate(shownTemplates.get(1));
    }

    @FXML
    private void onTemplate3() {
        applyTemplate(shownTemplates.get(2));
    }

    @FXML
    private void onCreateActivity() {
        hideError();

        String title = titleArea.getText();
        int target = 1;

        if (selectedTaskType == TaskType.PROGRESSIVE) {
            Integer enteredTarget = parseTarget(targetField.getText());

            if (enteredTarget == null) {
                showError("Target must be a whole number, for example 600.");
                return;
            }

            target = enteredTarget;
        }

        try {
            Activity activity = activityFactory.createActivity(
                    goalId,
                    habitId,
                    userId,
                    title,
                    selectedCategory,
                    selectedTaskType,
                    target
            );

            activityDAO.addActivity(activity);
            close();
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    /**
     * Converts the target field's text into a whole number.
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

    private void selectCategory(Category category) {
        selectedCategory = category;

        highlight(mindButton, category == Category.MIND);
        highlight(bodyButton, category == Category.BODY);
        highlight(worldButton, category == Category.WORLD);

        showTemplates();
    }

    private void showTemplates() {
        if (selectedCategory == null || selectedTaskType == null) {
            return;
        }

        shownTemplates = ActivityTemplate.getTemplatesFor(
                selectedCategory,
                selectedTaskType
        );

        template1Button.setText(shownTemplates.get(0).getTitle());
        template2Button.setText(shownTemplates.get(1).getTitle());
        template3Button.setText(shownTemplates.get(2).getTitle());
    }

    private void applyTemplate(ActivityTemplate template) {
        hideError();
        titleArea.setText(template.getTitle());

        if (template.getTaskType() == TaskType.PROGRESSIVE) {
            targetField.setText(String.valueOf(template.getTarget()));
        }
    }

    private void highlight(Button button, boolean selected) {
        button.getStyleClass().remove(SELECTED_STYLE_CLASS);

        if (selected) {
            button.getStyleClass().add(SELECTED_STYLE_CLASS);
        }
    }

    private void selectTaskType(TaskType taskType) {
        selectedTaskType = taskType;

        highlight(workOverTimeButton, taskType == TaskType.PROGRESSIVE);
        highlight(oneAndDoneButton, taskType == TaskType.BINARY);

        boolean needsTarget = taskType == TaskType.PROGRESSIVE;
        targetField.setDisable(!needsTarget);

        if (!needsTarget) {
            targetField.clear();
        }

        showTemplates();
    }
}