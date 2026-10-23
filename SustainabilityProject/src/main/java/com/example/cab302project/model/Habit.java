package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDate;

public class Habit {
    private Integer id;
    private Integer goalId; // inherently nullable
    private Integer userId;
    private String title;
    private Category category;
    private TaskType habitType;
    private Integer repeatFrequency;
    private RepeatFrequencyType repeatFrequencyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer completionThreshold;
    private Integer baseXpReward;
    private boolean doesContributeDirectlyToGoal;

    public Habit(Integer goalId, Integer userId, String title, Category category,
                 TaskType taskType, Integer repeatFrequency, RepeatFrequencyType repeatFrequencyType, LocalDate startDate,
                 LocalDate endDate, Integer completionThreshold, Integer baseXpReward,
                 boolean doesContributeDirectlyToGoal) {
        validateTitle(title);
        validateCompletionThreshold(completionThreshold);
        validateDates(startDate, endDate);

        this.goalId = goalId;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.habitType = taskType;
        this.repeatFrequency = repeatFrequency;
        this.repeatFrequencyType = repeatFrequencyType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completionThreshold = completionThreshold;
        this.baseXpReward = baseXpReward;
        this.doesContributeDirectlyToGoal = doesContributeDirectlyToGoal;
    }

    /**
     * Validates that the habit tite is not null or blank
     * @param habitTitle the habit title
     */
    private static void validateTitle(String habitTitle) {
        if (habitTitle == null || habitTitle.isBlank()) {
            throw new IllegalArgumentException("Habit title must not be blank.");
        }
    }

    /**
     * Validates that the habit completion threshold is greater than zero
     * @param completionThreshold the input completion threshold
     */
    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Habit completion threshold must be greater than zero.");
        }
    }

    /**
     * Validates that the habit start and due dates are valid and that the due date is after the start date
     * @param startsAt the habit start date
     * @param dueDate the habit due date
     */
    private static void validateDates(LocalDate startsAt, LocalDate dueDate) {
        if (startsAt == null) {
            throw new IllegalArgumentException("Habit start date must not be null.");
        }
        if (dueDate == null) {
            return; // A habit with no due date never expires.
        }
        if (dueDate.isBefore(startsAt)) {
            throw new IllegalArgumentException(
                    "Habit due date must not be before its start date.");
        }
    }


    public Integer getGoalId() {
        return goalId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }
    
    public CompletionType gethabitType() {
        return null; // TODO: fix
    }

    // TODO:fix depreciated
    public Integer getRepeatFrequencyDays() {
        return repeatFrequency;
    }
    public Integer getRepeatFrequency() {
        return repeatFrequency;
    }
    public RepeatFrequencyType getRepeatFrequencyType() {
        return repeatFrequencyType;
    }

    public LocalDate getStartDateTime() {
        return startDate;
    }

    public LocalDate getDueDateTime() {
        return endDate;
    }

    public Integer getProgress() {
//        return progress;
        return null;// TODO:Fix
    }

    public void setProgress(Integer progress) { } // TODO:Fix

    public Integer getCompletionThreshold() {
        return completionThreshold;
    }

    public Integer getBaseXpReward() {
        return baseXpReward;
    }

    public Integer getAwardedXpReward() {
        return null; // TODO:Fix
    }

    public void setAwardedXpReward(Integer awardedXpReward) { }// TODO:Fix

    public boolean getDoesContributeDirectlyToGoal() {
        return doesContributeDirectlyToGoal;
    }

    public TaskType getHabitType() {
        return habitType;
    }
}
