package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Habit {
    private Integer id;
    private Integer goalId; // inherently nullable
    private Integer userId;
    private String title;
    private Category category;
    private CompletionType habitType;
    private Integer repeatFrequencyDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer progress;
    private Integer completionThreshold;
    private Integer baseXpReward;
    private Integer awardedXpReward;
    // if true this progress in this activity directly adds contribution to the associated goal
    private boolean doesContributeDirectlyToGoal;


    public Habit(Integer goalId, Integer userId, String title, Category category,
                 CompletionType habitType, Integer repeatFrequencyDays, LocalDate startDate,
                 LocalDate endDate, Integer progress, Integer completionThreshold, Integer baseXpReward,
                 Integer awardedXpReward, boolean doesContributeDirectlyToGoal) {
        validateTitle(title);
        validateCompletionThreshold(completionThreshold);
        validateDates(startDate, endDate);
        
        this.goalId = goalId;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.habitType = habitType;
        this.repeatFrequencyDays = repeatFrequencyDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.progress = progress;
        this.completionThreshold = completionThreshold;
        this.baseXpReward = baseXpReward;
        this.awardedXpReward = awardedXpReward;
        this.doesContributeDirectlyToGoal = doesContributeDirectlyToGoal;
    }

    private static void validateTitle(String habitTitle) {
        if (habitTitle == null || habitTitle.isBlank()) {
            throw new IllegalArgumentException("Habit title must not be blank.");
        }
    }

    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Habit completion threshold must be greater than zero.");
        }
    }

    private static void validateDates(LocalDate startsAt, LocalDate dueDate) {
        if (startsAt == null) {
            throw new IllegalArgumentException("Habit start date must not be null.");
        }
        if (dueDate == null) {
            return; // An habit with no due date never expires.
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Habit due date must not be in the past.");
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
        return habitType;
    }

    public Integer getRepeatFrequencyDays() {
        return repeatFrequencyDays;
    }

    public LocalDate getStartDateTime() {
        return startDate;
    }

    public LocalDate getDueDateTime() {
        return endDate;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) { this.progress = progress;}

    public Integer getCompletionThreshold() {
        return completionThreshold;
    }

    public Integer getBaseXpReward() {
        return baseXpReward;
    }

    public Integer getAwardedXpReward() {
        return awardedXpReward;
    }

    public void setAwardedXpReward(Integer awardedXpReward) { this.awardedXpReward = awardedXpReward;}

    public boolean isDoesContributeDirectlyToGoal() {
        return doesContributeDirectlyToGoal;
    }
}
