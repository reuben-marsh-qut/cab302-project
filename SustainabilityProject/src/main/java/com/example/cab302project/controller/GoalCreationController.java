package com.example.cab302project.controller;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.GoalTemplate;
import com.example.cab302project.model.IGoalDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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


    private IGoalDAO goalDAO;
    private int userId;
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

        if (selectedCompletionType == CompletionType.PROGRESSIVE) {
            Integer enteredTarget = parseTarget(targetField.getText());

            if (enteredTarget == null) {
                showError("Target must be a whole number, for example 600.");
                return;
            }

            target = enteredTarget;
        }

        try {
            // Goals start at zero progress, and this screen has no dates,
            // so the goal starts today and never expires.
            Goal goal = new Goal(userId, title, selectedCategory,
                    LocalDate.now(), null,
                    0, target, selectedCompletionType, false);

            goalDAO.addGoal(goal);
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
        showTemplates(category);
    }
    /**
     * Shows the starter templates for a category on the three buttons.
     * @param category The category to show suggestions for.
     */
    private void showTemplates(Category category) {
        shownTemplates = GoalTemplate.getTemplatesForCategory(category);

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
        selectCompletionType(template.getCompletionType());

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
    }
}