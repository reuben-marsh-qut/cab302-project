package com.example.cab302project.model;

import java.time.LocalDate;

/**
 * A long-term achievement the user is working towards.
 */
public class Goal {
    private int goalId;
    private final int userId;
    private final String goalTitle;
    private final Category category;
    private final int completionThreshold;
    private final LocalDate startsAt;
    private final LocalDate dueDate;
    private int progress;

    /**
     * Creates a new goal.
     *
     * @param userId              the owner of this goal
     * @param goalTitle           what the user wants to achieve
     * @param category            the wellbeing area this goal belongs to
     * @param completionThreshold the value that must be reached to complete it
     * @param startsAt            the date the goal begins
     * @param dueDate             the date the goal must be met by, or null if it never expires
     * @throws IllegalArgumentException if any of the details are invalid
     */
    public Goal(int userId, String goalTitle, Category category,
                int completionThreshold, LocalDate startsAt, LocalDate dueDate) {
        validateTitle(goalTitle);
        validateCompletionThreshold(completionThreshold);
        validateDates(startsAt, dueDate);

        this.userId = userId;
        this.goalTitle = goalTitle;
        this.category = category;
        this.completionThreshold = completionThreshold;
        this.startsAt = startsAt;
        this.dueDate = dueDate;
        this.progress = 0;
    }

    private static void validateTitle(String goalTitle) {
        if (goalTitle == null || goalTitle.isBlank()) {
            throw new IllegalArgumentException("Goal title must not be blank.");
        }
    }

    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Goal completion threshold must be greater than zero.");
        }
    }

    private static void validateDates(LocalDate startsAt, LocalDate dueDate) {
        if (startsAt == null) {
            throw new IllegalArgumentException("Goal start date must not be null.");
        }
        if (dueDate == null) {
            return; // A goal with no due date never expires.
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Goal due date must not be in the past.");
        }
        if (dueDate.isBefore(startsAt)) {
            throw new IllegalArgumentException(
                    "Goal due date must not be before its start date.");
        }
    }

    /**
     * Whether this goal has been achieved. Derived from progress rather than
     * stored, matching the generated isComplete column in the database design.
     */
    public boolean isComplete() {
        return progress >= completionThreshold;
    }

    public int getGoalId() { return goalId; }

    public void setGoalId(int goalId) { this.goalId = goalId; }

    public int getUserId() { return userId; }

    public String getGoalTitle() { return goalTitle; }

    public Category getCategory() { return category; }

    public int getCompletionThreshold() { return completionThreshold; }

    public LocalDate getStartsAt() { return startsAt; }

    public LocalDate getDueDate() { return dueDate; }

    public int getProgress() { return progress; }
}