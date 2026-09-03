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

    /**
     * Creates a new goal.
     *
     * @param title    a description of what the user wants to achieve
     * @param category the wellbeing area this goal belongs to
     * @param target   the value that must be reached to complete the goal
     * @param deadline the date by which the target should be reached
     * @throws IllegalArgumentException if any of the details are invalid
     */
    public Goal(String title, Category category, int target, LocalDate deadline) {
        validateTitle(title);
        validateTarget(target);
        validateDeadline(deadline);

        this.title = title;
        this.category = category;
        this.target = target;
        this.deadline = deadline;
        this.progress = 0;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Goal title must not be blank.");
        }
    }

    private static void validateTarget(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("Goal target must be greater than zero.");
        }
    }

    private static void validateDeadline(LocalDate deadline) {
        if (deadline == null || deadline.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Goal deadline must not be in the past.");
        }
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public Category getCategory() { return category; }

    public int getTarget() { return target; }

    public LocalDate getDeadline() { return deadline; }

    public int getProgress() { return progress; }
}