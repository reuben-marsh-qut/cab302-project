package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.GoalTemplate;
import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

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
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Label pageHeaderLabel;
    @FXML
    private Button createButton;


    private IGoalDAO goalDAO;
    private int userId;
    private Goal goalToEdit;
    private Runnable onFinished;
    private static final String SELECTED_STYLE_CLASS = "option-selected";

    private Category selectedCategory;
    private CompletionType selectedCompletionType;

    public void setGoalDAO(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Puts the page into edit mode for an existing goal, filling the form
     * with its current details.
     * @param goal The goal being edited.
     */
    public void setGoalToEdit(Goal goal) {
        this.goalToEdit = goal;

        pageHeaderLabel.setText("Edit Your Goal");
        createButton.setText("Save Changes");

        templatesContainer.setVisible(false);
        templatesContainer.setManaged(false);

        selectCategory(goal.getCategory());
        selectCompletionType(goal.getCompletionType());

        titleArea.setText(goal.getTitle());
        targetField.setText(String.valueOf(goal.getThreshold()));
        dueDatePicker.setValue(goal.getDueDate());
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
        selectCategory(Category.MIND);
        selectCompletionType(CompletionType.PROGRESSIVE);
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
        selectCompletionType(CompletionType.PROGRESSIVE);
    }

    @FXML
    private void onOneAndDone() {
        selectCompletionType(CompletionType.BINARY);
    }
    @FXML
    private Button template1Button;
    @FXML
    private Button template2Button;
    @FXML
    private Button template3Button;

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


    private List<GoalTemplate> shownTemplates;

    @FXML
    private void onCreateGoal() {
        hideError();

        String title = titleArea.getText();
        int target = 1;

        LocalDate deadline = dueDatePicker.getValue();

        if (deadline == null) {
            showError("Please choose a deadline.");
            return;
        }

        if (deadline.isBefore(LocalDate.now())) {
            showError("Deadline must not be in the past.");
            return;
        }

        if (selectedCompletionType == CompletionType.PROGRESSIVE) {
            Integer enteredTarget = parseTarget(targetField.getText());

            if (enteredTarget == null) {
                showError("Target must be a whole number, for example 600.");
                return;
            }

            target = enteredTarget;
        }

        try {
            if (goalToEdit == null) {
                // Creating: goals start today at zero progress and run until the deadline.
                Goal goal = new Goal(userId, title, selectedCategory,
                        LocalDate.now(), deadline,
                        0, target, selectedCompletionType, false);

                goalDAO.addGoal(goal);
            } else {
                // Editing: progress, start date and id are all left as they were.
                goalToEdit.setTitle(title);
                goalToEdit.setCategory(selectedCategory);
                goalToEdit.setCompletionType(selectedCompletionType);
                goalToEdit.setThreshold(target);
                goalToEdit.setDueDate(deadline);

                goalDAO.updateGoal(goalToEdit);
            }

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

    /**
     * Records the chosen category and highlights its button.
     * @param category The category the user picked.
     */
    private void selectCategory(Category category) {
        selectedCategory = category;

        highlight(mindButton, category == Category.MIND);
        highlight(bodyButton, category == Category.BODY);
        highlight(worldButton, category == Category.WORLD);
        showTemplates();
    }
    /**
     * Shows the starter templates matching the current category and
     * completion type on the three template buttons.
     */
    private void showTemplates() {
        if (selectedCategory == null || selectedCompletionType == null) {
            return;
        }

        shownTemplates = GoalTemplate.getTemplatesFor(selectedCategory, selectedCompletionType);

        template1Button.setText(shownTemplates.get(0).getTitle());
        template2Button.setText(shownTemplates.get(1).getTitle());
        template3Button.setText(shownTemplates.get(2).getTitle());
    }
    /**
     * Fills the form in from a template. Everything stays editable.
     * @param template The template the user picked.
     */
    private void applyTemplate(GoalTemplate template) {
        hideError();

        titleArea.setText(template.getTitle());

        if (template.getCompletionType() == CompletionType.PROGRESSIVE) {
            targetField.setText(String.valueOf(template.getTarget()));
        }
    }

    private void highlight(Button button, boolean selected) {
        button.getStyleClass().remove(SELECTED_STYLE_CLASS);

        if (selected) {
            button.getStyleClass().add(SELECTED_STYLE_CLASS);
        }
    }
    private void selectCompletionType(CompletionType completionType) {
        selectedCompletionType = completionType;

        highlight(workOverTimeButton, completionType == CompletionType.PROGRESSIVE);
        highlight(oneAndDoneButton, completionType == CompletionType.BINARY);

        boolean needsTarget = completionType == CompletionType.PROGRESSIVE;

        targetField.setDisable(!needsTarget);

        if (!needsTarget) {
            targetField.clear();
        }
        showTemplates();
    }

}