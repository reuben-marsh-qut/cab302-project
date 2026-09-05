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
    // NOTE: If an acitivty has a goalId associated, it will contrivute directly to the goal's progress.


    public Activity(Integer goalId, Integer habitId, Integer userId, String title, Category category,
                    CompletionType activityType, LocalDateTime startDateTime, LocalDateTime dueDateTime,
                    Integer progress, Integer completionThreshold, Integer baseXpReward, Integer awardedXpReward) {
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

    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
    }

    public Integer getHabitId() {
        return habitId;
    }

    public void setHabitId(Integer habitId) {
        this.habitId = habitId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CompletionType getActivityType() {
        return activityType;
    }

    public void setActivityType(CompletionType activityType) {
        this.activityType = activityType;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(LocalDateTime dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(Integer completionThreshold) {
        this.completionThreshold = completionThreshold;
    }

    public Integer getBaseXpReward() {
        return baseXpReward;
    }

    public void setBaseXpReward(Integer baseXpReward) {
        this.baseXpReward = baseXpReward;
    }

    public Integer getAwardedXpReward() {
        return awardedXpReward;
    }

    public void setAwardedXpReward(Integer awardedXpReward) {
        this.awardedXpReward = awardedXpReward;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

