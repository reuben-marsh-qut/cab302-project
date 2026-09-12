package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

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

    /**
     * Creates a new goal.
     *
     * @param userId    the owner of this goal
     * @param title     what the user wants to achieve
     * @param category  the wellbeing area this goal belongs to
     * @param threshold the value that must be reached to complete it
     * @param startDate the date the goal begins
     * @param dueDate   the date the goal must be met by, or null if it never expires
     * @throws IllegalArgumentException if any of the details are invalid
    **/
    public Goal(Integer userId, String title, Category category, LocalDate startDate,
                LocalDate dueDate, Integer progress, Integer threshold,
                CompletionType completionType, boolean isComplete) {
        validateTitle(title);
        validateCompletionThreshold(threshold);
        validateDates(startDate, dueDate);

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

    /**
     *
     * @param goalTitle the title of the goal, which must not be blank
     */
    private static void validateTitle(String goalTitle) {
        if (goalTitle == null || goalTitle.isBlank()) {
            throw new IllegalArgumentException("Goal title must not be blank.");
        }
    }

    /**
     *
     * @param completionThreshold The threshold at which point the goal is considered complete, must be greater than zero
     */
    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Goal completion threshold must be greater than zero.");
        }
    }

    /**
     *
     * @param startsAt Goal start date, must not be null
     * @param dueDate Goal due date, may be null
     */
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
     **/

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
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

    public CompletionType getCompletionType() {
        return completionType;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean complete) {
        isComplete = complete;
    }
}
