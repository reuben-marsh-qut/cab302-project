package com.example.cab302project.model;

import java.time.LocalDate;

/**
 * A long-term goal the user is working towards.
 */
public class Goal {
    private int id;
    private final String title;
    private final Category category;
    private final int target;
    private final LocalDate deadline;
    private int progress;

    public Goal(String title, Category category, int target, LocalDate deadline) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Goal title must not be blank.");
        }
        if (target <= 0) {
            throw new IllegalArgumentException("Goal target must be greater than zero.");
        }
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Goal deadline must not be in the past.");
        }

        this.title = title;
        this.category = category;
        this.target = target;
        this.deadline = deadline;
        this.progress = 0;
    }

    public int getId() {return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public Category getCategory() { return category; }

    public int getTarget() { return target; }

    public LocalDate getDeadline() { return deadline; }

    public int getProgress() { return progress; }
}