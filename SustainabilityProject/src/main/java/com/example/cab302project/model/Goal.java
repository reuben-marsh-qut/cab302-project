package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;

public class Goal {
    private Integer id;
    private Integer userId;
    private String title;
    private Category category;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Integer progress;
    private Integer threshold;
    private CompletionType completionType;
    private boolean isComplete;

    // NOTE: I've put this here since it only relates to goal, unlike category
    public enum CompletionType {
        CONSTRAINT,
        BINARY,
        PROGRESSIVE
    }

    public Goal(Integer userId, String title, Category category, LocalDate startDate,
                LocalDate dueDate, Integer progress, Integer threshold,
                CompletionType completionType, boolean isComplete) {
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.progress = progress;
        this.threshold = threshold;
        this.completionType = completionType;
        this.isComplete = isComplete;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    // I've included setters for everything, which might be redundant for mutable things?

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

    public void setCategory(Category category) { this.category = category; }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public void setCompletionType(CompletionType completionType) {
        this.completionType = completionType;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean complete) {
        isComplete = complete;
    }



}
