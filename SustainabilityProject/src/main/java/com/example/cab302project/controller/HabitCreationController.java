package com.example.cab302project.controller;

import com.example.cab302project.ModelConnection;
import com.example.cab302project.model.*;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import java.time.LocalDate;
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


    private IGoalDAO goalDAO;
    private IHabitDAO habitDAO;
    private IUserDAO userDAO;
    private int userId;
    private Runnable onFinished;
    private static final String SELECTED_STYLE_CLASS = "option-selected";
    private Category selectedGoalCategory;
    private CompletionType selectedGoalCompletionType;

    private Goal selectedGoal;
    private RepeatFrequencyType selectedRepeatFrequencyType;
    private int selectedRepeatFrequency;
    private boolean selectedaiTemplate = false;
    private boolean aiIsLoading = false;
    private boolean hasFetchedAIForThisGoal = false;
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
        selectRepeatFrequencyType(RepeatFrequencyType.DAILY);
    }

    @FXML
    private void onNoGoals(){
        selectGoal(null);
    }

    private void setupGoalList(){
        List<Goal> goals = goalDAO.getGoalsForUser(userId);

        goalSelectDropDown.getItems().addAll(goals);

        selectGoal(goals.getFirst());
        goalSelectDropDown.setValue(goals.getFirst());

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
            selectedaiTemplate = true;
            hideError();
            showTemplates();
            goalSelectDropDown.setValue(null);
            selectedGoal = goal;
            return;
        }
        selectedGoal = goal;
        selectedGoalCompletionType = goal.getCompletionType();
        if (selectedGoalCompletionType == CompletionType.BINARY) {selectedActivityType = TaskType.BINARY;}
        else {selectedActivityType = TaskType.PROGRESSIVE;}
        selectedGoalCategory = goal.getCategory();
        hasFetchedAIForThisGoal = false;
        selectTemplateVariant(true);
    }
    private void selectTemplateVariant(boolean aiTemplate){
        if (aiTemplate){
            if(selectedGoal == null) {
                selectedaiTemplate = false;
                showError("Please select a goal to generate Habits from");
                showTemplates();
                return;
            }
            selectedaiTemplate = true;
            hideError();
            showTemplates();
        } else {
            selectedaiTemplate = false;
            hideError();
            showTemplates();
        }
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
    private Button template1Button;
    @FXML
    private Button template2Button;
    @FXML
    private Button template3Button;


    @FXML
    private void onTemplate1() {
        if (aiIsLoading){
            showError("Please wait for loading to finish");
        } else {
            applyTemplate(shownTemplates.get(0));
        }

    }

    @FXML
    private void onTemplate2() {
        if (aiIsLoading){
            showError("Please wait for loading to finish");
        } else {
            applyTemplate(shownTemplates.get(1));
        }
    }

    @FXML
    private void onTemplate3() {
        if (aiIsLoading){
            showError("Please wait for loading to finish");
        } else {
            applyTemplate(shownTemplates.get(2));
        }
    }


    private List<HabitTemplate> shownTemplates;
    private List<HabitTemplate> storedAITemplates;

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
        LocalDate endDate = LocalDate.now();
        switch (selectedRepeatFrequencyType){
            case DAILY -> endDate = endDate.plusDays(100);
            case WEEKLY -> endDate = endDate.plusDays(100);
            case MONTHLY -> endDate = endDate.plusMonths(48);
            case YEARLY -> endDate = endDate.plusYears(2);
        }
        try {
            if (selectedGoal != null) {
                Habit habit = new Habit(selectedGoal.getId(), userId,title,selectedGoalCategory,selectedActivityType,repeatFreq,selectedRepeatFrequencyType, LocalDate.now(),endDate,target,10, false);
                habitDAO.createHabit(habit);
            } else {
                Habit habit = new Habit(null, userId,title,selectedGoalCategory,selectedActivityType,repeatFreq,selectedRepeatFrequencyType, LocalDate.now(),endDate,target,10, false);
                habitDAO.createHabit(habit);
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
        if (selectedGoal == null) {
            return;
        }
        if (selectedaiTemplate){
            if (!ModelConnection.getInstance().isAvailable()){
                showError("AI model is unavailable");
                return;
            }
            if (!hasFetchedAIForThisGoal){
                template1Button.setText("Loading ...");
                template2Button.setText("Loading ...");
                template3Button.setText("Loading ...");
                aiIsLoading = true;
                new Thread(()->{
                    StringBuilder previous = new StringBuilder();
                    HabitTemplate template1 = HabitTemplate.generateHabitFromPartialGoal(selectedGoal.getTitle(),selectedGoal.getCategory());
                    Platform.runLater(()->{
                        template1Button.setText(template1.getTitle());
                    });
                    previous.append(template1.toString());
                    HabitTemplate template2 = HabitTemplate.generateHabitFromPartialGoal(selectedGoal.getTitle(),selectedGoal.getCategory(),previous.toString());
                    Platform.runLater(()->{
                        template2Button.setText(template2.getTitle());
                    });
                    previous.append("\n"+template2.toString());
                    HabitTemplate template3 = HabitTemplate.generateHabitFromPartialGoal(selectedGoal.getTitle(),selectedGoal.getCategory(),previous.toString());
                    Platform.runLater(()->{
                        template3Button.setText(template3.getTitle());
                        storedAITemplates = List.of(template1,template2,template3);
                        shownTemplates = storedAITemplates;
                        aiIsLoading=false;
                        hasFetchedAIForThisGoal = true;
                    });
                }).start();
            } else {
                shownTemplates = storedAITemplates;
                template1Button.setText(shownTemplates.get(0).getTitle());
                template2Button.setText(shownTemplates.get(1).getTitle());
                template3Button.setText(shownTemplates.get(2).getTitle());
            }

        }
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
    }

    private void highlight(Button button, boolean selected) {
        button.getStyleClass().remove(SELECTED_STYLE_CLASS);

        if (selected) {
            button.getStyleClass().add(SELECTED_STYLE_CLASS);
        }
    }

}