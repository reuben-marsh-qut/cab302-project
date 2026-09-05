package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Activity {
    private Integer id;
    private Integer goalId; // inherently nullable
    private Integer habitId;
    private Integer userId;
    private String title;
    private Category category;
    private CompletionType activityType;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    private Integer progress;
    private Integer completionThreshold;
    private Integer baseXpReward;
    private Integer awardedXpReward;
    // NOTE: If an acitivty has a goalId associated, it will contrivute directly to the goalId's progress.

    public Activity(Integer goalId, Integer habitId, Integer userId, String title, Category category,
                    CompletionType activityType, LocalDateTime startDateTime, LocalDateTime dueDateTime,
                    Integer progress, Integer completionThreshold, Integer baseXpReward, Integer awardedXpReward) {
        validateTitle(title);
        validateCompletionThreshold(completionThreshold);
        validateDates(startDateTime, dueDateTime);

        this.goalId = goalId;
        this.activityType = activityType;
        this.habitId = habitId;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.startDateTime = startDateTime;
        this.dueDateTime = dueDateTime;
        this.progress = progress;
        this.completionThreshold = completionThreshold;
        this.baseXpReward = baseXpReward;
        this.awardedXpReward = awardedXpReward;
    }

    private static void validateTitle(String activityTitle) {
        if (activityTitle == null || activityTitle.isBlank()) {
            throw new IllegalArgumentException("Activity title must not be blank.");
        }
    }

    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Activity completion threshold must be greater than zero.");
        }
    }

    private static void validateDates(LocalDateTime startsAt, LocalDateTime dueDate) {
        if (startsAt == null) {
            throw new IllegalArgumentException("Activity start date must not be null.");
        }
        if (dueDate == null) {
            return; // An activity with no due date never expires.
        }
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Activity due date must not be in the past.");
        }
        if (dueDate.isBefore(startsAt)) {
            throw new IllegalArgumentException(
                    "Activity due date must not be before its start date.");
        }
    }

    public Integer getGoalId() {
        return goalId;
    }

    public Integer getHabitId() {
        return habitId;
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

    public CompletionType getActivityType() {
        return activityType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

