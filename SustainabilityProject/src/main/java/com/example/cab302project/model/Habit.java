package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.time.LocalDate;

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
    // NOTE: If an acitivity has a goalId associated, it will contrivute directly to the goal's progress.


    public Habit(Integer goalId, Integer userId, String title, Category category,
                 CompletionType habitType, Integer repeatFrequencyDays, LocalDate startDate,
                 LocalDate endDate, Integer progress, Integer completionThreshold, Integer baseXpReward,
                 Integer awardedXpReward) {
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
    }


    public Integer getGoalId() {
        return goalId;
    }

    public void setGoalId(Integer goalId) {
        this.goalId = goalId;
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

    public CompletionType gethabitType() {
        return habitType;
    }

    public void sethabitType(CompletionType habitType) {
        this.habitType = habitType;
    }

    public Integer getRepeatFrequencyDays() {
        return repeatFrequencyDays;
    }

    public void setRepeatFrequencyDays(Integer repeatFrequencyDays) {
        this.repeatFrequencyDays = repeatFrequencyDays;
    }

    public LocalDate getStartDateTime() {
        return startDate;
    }

    public void setStartDateTime(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDateTime() {
        return endDate;
    }

    public void setDueDateTime(LocalDate endDate) {
        this.endDate = endDate;
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
}
