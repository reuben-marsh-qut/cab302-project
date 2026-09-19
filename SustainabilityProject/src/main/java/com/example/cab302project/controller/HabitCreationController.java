package com.example.cab302project.controller;

import com.example.cab302project.model.*;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for the habit creation page. Collects the details of a new habit
 * and stores it using the DAO supplied by the calling screen.
 */
public class HabitCreationController {
    @FXML
    private TextArea titleArea;
    @FXML
    private TextField targetField;
    @FXML
    private TextField repeatFrequencyField;
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
    private Button noGoalsButton;
    @FXML
    private ChoiceBox<Goal> goalSelectDropDown;
    @FXML
    private HBox templatesContainer;
    @FXML
    private Label errorLabel;

    @FXML
    private Button dailyButton;
    @FXML
    private Button weeklyButton;
    @FXML
    private Button monthlyButton;
    @FXML
    private Button yearlyButton;

    @FXML
    private Button manualHabitTemplateButton;
    @FXML
    private Button aiHabitTemplateButton;


    private IGoalDAO goalDAO;
    private IHabitDAO habitDAO;
    private IUserDAO userDAO;
    private int userId;
    private Runnable onFinished;
    private static final String SELECTED_STYLE_CLASS = "option-selected";

    private Category selectedCategory;
    private Goal selectedGoal;
    private RepeatFrequencyType selectedRepeatFrequencyType;
    private int selectedRepeatFrequency;
    private boolean selectedaiTemplate = false;
    private TaskType selectedActivityType;

    public void setGoalDAO(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
        setupGoalList();
    }
    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public void setHabitDAO(IHabitDAO habitDAO) {
        this.habitDAO = habitDAO;
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
        selectActivityType(TaskType.PROGRESSIVE);
        selectGoal(null);
        selectRepeatFrequencyType(RepeatFrequencyType.DAILY);
        selectTemplateVariant(false);
    }

    @FXML
    private void onNoGoals(){
        selectGoal(null);
    }

    private void setupGoalList(){
        List<Goal> goals = goalDAO.getGoalsForUser(userId);
//        System.out.println("size: " + goals.size());

        goalSelectDropDown.getItems().addAll(goals);

        goalSelectDropDown.setConverter(new StringConverter<Goal>() {
            @Override
            public String toString(Goal object) {
                return (object == null) ? "" : object.getTitle();
            }

            @Override
            public Goal fromString(String string) {
                return null;
            }
        });
        goalSelectDropDown.setOnAction(event -> {
            selectGoal(goalSelectDropDown.getValue());
        });
    }

    private void selectGoal(Goal goal){
        hideError();
        if (goal == null){
            highlight(noGoalsButton,true);
            goalSelectDropDown.setValue(null);
            selectedGoal = goal;
            return;
        }
        highlight(noGoalsButton,false);
        selectedGoal = goal;

    }
    private void selectTemplateVariant(boolean aiTemplate){
        if (aiTemplate){
            if(selectedGoal == null) {
                selectedaiTemplate = false;
                showError("Please select a goal to generate Habits from");
                highlight(aiHabitTemplateButton,false);
                highlight(manualHabitTemplateButton,true);
                showTemplates();
                return;
            }
            highlight(aiHabitTemplateButton,true);
            highlight(manualHabitTemplateButton,false);
            selectedaiTemplate = true;
            System.err.println("AI NOT IMPLEMENTED");
            hideError();
            showTemplates();
        } else {
            highlight(aiHabitTemplateButton,false);
            highlight(manualHabitTemplateButton,true);
            selectedaiTemplate = false;
            hideError();
            showTemplates();
        }

    }


    @FXML
    private void onSelectManualHabitTemplates() {
        selectTemplateVariant(false);
    }
    @FXML
    private void onSelectAITemplates() {
        selectTemplateVariant(true);
    }

    @FXML
    private void onSelectDaily() {
        selectRepeatFrequencyType(RepeatFrequencyType.DAILY);
    }
    @FXML
    private void onSelectWeekly() {
        selectRepeatFrequencyType(RepeatFrequencyType.WEEKLY);
    }
    @FXML
    private void onSelectMonthly() {
        selectRepeatFrequencyType(RepeatFrequencyType.MONTHLY);
    }
    @FXML
    private void onSelectYearly() {
        selectRepeatFrequencyType(RepeatFrequencyType.YEARLY);
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
        selectActivityType(TaskType.PROGRESSIVE);
    }

    @FXML
    private void onOneAndDone() {
        selectActivityType(TaskType.BINARY);
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


    private List<HabitTemplate> shownTemplates;

    @FXML
    private void onCreateHabit() {
        hideError();

        String title = titleArea.getText();
        int target = 1;
        int repeatFreq = 1;
        Integer enteredRepeatFreq = parseInteger(repeatFrequencyField.getText());

        if (enteredRepeatFreq == null) {
            showError("Repeat frequency must be a whole number, for example 1.");
            return;
        }
        repeatFreq = enteredRepeatFreq;
        if (selectedActivityType == TaskType.PROGRESSIVE) {
            Integer enteredTarget = parseInteger(targetField.getText());

            if (enteredTarget == null) {
                showError("Target must be a whole number, for example 600.");
                return;
            }

            target = enteredTarget;
        }
        LocalDate endDate = LocalDate.now();
        switch (selectedRepeatFrequencyType){
            case DAILY -> endDate = endDate.plusDays(100);
            case WEEKLY -> endDate = endDate.plusDays(100);
            case MONTHLY -> endDate = endDate.plusMonths(48);
            case YEARLY -> endDate = endDate.plusYears(2);
        }
        try {
            habitDAO.createHabit(selectedGoal, userDAO.getUserById(userId),title,selectedCategory,selectedActivityType,target,10,repeatFreq,selectedRepeatFrequencyType, LocalDate.now(),endDate,false);
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
     * Converts the string into a whole number.
     * @param text The string to parse for a whole number.
     * @return The number, or null if the text is not a whole number.
     */
    private Integer parseInteger(String text) {
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
    private void selectRepeatFrequencyType(RepeatFrequencyType repeatFrequencyType) {
        this.selectedRepeatFrequencyType = repeatFrequencyType;

        highlight(dailyButton, repeatFrequencyType == repeatFrequencyType.DAILY);
        highlight(weeklyButton, repeatFrequencyType == repeatFrequencyType.WEEKLY);
        highlight(monthlyButton, repeatFrequencyType == repeatFrequencyType.MONTHLY);
        highlight(yearlyButton, repeatFrequencyType == repeatFrequencyType.YEARLY);

    }
    private void selectRepeatFrequency(int repeatFrequency) {
        this.selectedRepeatFrequency = repeatFrequency;
        repeatFrequencyField.setText(String.valueOf(repeatFrequency));
    }
        /**
         * Shows the starter templates matching the current category and
         * completion type on the three template buttons.
         */
    private void showTemplates() {
        if (selectedCategory == null || selectedActivityType == null) {
            return;
        }

        shownTemplates = HabitTemplate.getTemplatesFor(selectedCategory, selectedActivityType);

        template1Button.setText(shownTemplates.get(0).getTitle());
        template2Button.setText(shownTemplates.get(1).getTitle());
        template3Button.setText(shownTemplates.get(2).getTitle());
    }
    /**
     * Fills the form in from a template. Everything stays editable.
     * @param template The template the user picked.
     */
    private void applyTemplate(HabitTemplate template) {
        hideError();

        titleArea.setText(template.getTitle());
        selectRepeatFrequencyType(template.getRepeatFrequencyType());
        selectRepeatFrequency(template.getRepeatFrequency());
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
    private void selectActivityType(TaskType taskType) {
        selectedActivityType = taskType;

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