package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDateTime;

/**
 * Represents an activity belonging to a user.
 *
 * <p>An activity is complete when its progress reaches or exceeds its
 * completion threshold. It can optionally belong to a goal or habit.</p>
 */
public class Activity {

    private Integer id;
    private Integer goalId;
    private Integer habitId;
    private Integer userId;
    private String title;
    private Category category;
    private TaskType activityType;
    private LocalDateTime startDateTime;
    private LocalDateTime dueDateTime;
    private LocalDateTime completedAt;
    private Integer progress;
    private Integer completionThreshold;
    private Integer baseXpReward;
    private Integer awardedXpReward;

    // If true, activity progress contributes directly to its associated goal.
    // TODO: handle this in updateGoalDAO.
    private boolean doesContributeDirectlyToGoal;

    /**
     * Creates an activity with its supplied state.
     *
     * <p>A completion time is not inferred from existing progress.
     * Historical activities may have an unknown completion time.</p>
     *
     * @param goalId the associated goal ID, if present
     * @param habitId the associated habit ID, if present
     * @param userId the owning user's ID
     * @param title the activity title
     * @param category the wellbeing category
     * @param activityType the activity's task type
     * @param startDateTime the activity start time
     * @param dueDateTime the due time, or null when there is no deadline
     * @param progress the current progress
     * @param completionThreshold the progress required for completion
     * @param baseXpReward the potential XP reward
     * @param awardedXpReward the XP already awarded
     * @param doesContributeDirectlyToGoal whether progress contributes to a goal
     * @throws IllegalArgumentException if the title, threshold or dates
     *                                  are invalid
     */
    public Activity(
            Integer goalId,
            Integer habitId,
            Integer userId,
            String title,
            Category category,
            TaskType activityType,
            LocalDateTime startDateTime,
            LocalDateTime dueDateTime,
            Integer progress,
            Integer completionThreshold,
            Integer baseXpReward,
            Integer awardedXpReward,
            boolean doesContributeDirectlyToGoal
    ) {
        validateTitle(title);
        validateCompletionThreshold(completionThreshold);
        validateDates(startDateTime, dueDateTime);

        this.goalId = goalId;
        this.habitId = habitId;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.activityType = activityType;
        this.startDateTime = startDateTime;
        this.dueDateTime = dueDateTime;
        this.progress = progress;
        this.completionThreshold = completionThreshold;
        this.baseXpReward = baseXpReward;
        this.awardedXpReward = awardedXpReward;
        this.doesContributeDirectlyToGoal = doesContributeDirectlyToGoal;
    }

    /**
     * Validates that the activity has a non-blank title.
     */
    private static void validateTitle(String activityTitle) {
        if (activityTitle == null || activityTitle.isBlank()) {
            throw new IllegalArgumentException(
                    "Activity title must not be blank."
            );
        }
    }

    /**
     * Validates that completion requires positive progress.
     */
    private static void validateCompletionThreshold(int completionThreshold) {
        if (completionThreshold <= 0) {
            throw new IllegalArgumentException(
                    "Activity completion threshold must be greater than zero."
            );
        }
    }

    /**
     * Validates the start time and optional due time.
     *
     * <p>Past dates are permitted so historical activities can be loaded.</p>
     */
    private static void validateDates(
            LocalDateTime startsAt,
            LocalDateTime dueDate
    ) {
        if (startsAt == null) {
            throw new IllegalArgumentException(
                    "Activity start date must not be null."
            );
        }

        if (dueDate != null && dueDate.isBefore(startsAt)) {
            throw new IllegalArgumentException(
                    "Activity due date must not be before its start date."
            );
        }
    }

    /** @return the activity's database ID */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the activity's database ID.
     *
     * @param id the database ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the associated goal ID */
    public Integer getGoalId() {
        return goalId;
    }

    /** @return the associated habit ID */
    public Integer getHabitId() {
        return habitId;
    }

    /** @return the owning user's ID */
    public Integer getUserId() {
        return userId;
    }

    /** @return the activity title */
    public String getTitle() {
        return title;
    }

    /** @return the activity's wellbeing category */
    public Category getCategory() {
        return category;
    }

    /** @return the activity's task type */
    public TaskType getActivityType() {
        return activityType;
    }

    /** @return the activity start time */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /** @return the due time, or null when there is no deadline */
    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    /** @return the current progress */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Updates progress using the current local time.
     *
     * @param progress the new progress value
     */
    public void setProgress(Integer progress) {
        setProgress(progress, LocalDateTime.now());
    }

    /**
     * Updates progress and records the first transition to completion.
     *
     * <p>An existing completion time is preserved. Updating an activity
     * that was already complete with an unknown completion time does
     * not invent a historical timestamp.</p>
     *
     * @param progress the new progress value
     * @param updatedAt the time of the progress update
     */
    public void setProgress(Integer progress, LocalDateTime updatedAt) {
        boolean wasComplete = isComplete();
        this.progress = progress;

        if (!wasComplete && isComplete() && completedAt == null) {
            completedAt = updatedAt;
        }
    }

    /**
     * Returns the first recorded completion time.
     *
     * @return the completion time, or null when no time is recorded
     */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    /**
     * Restores the completion time read from persistent storage.
     *
     * <p>Normal progress updates should use {@link #setProgress(Integer)}
     * or {@link #setProgress(Integer, LocalDateTime)}.</p>
     *
     * @param completedAt the stored completion time, or null if unknown
     */
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    /** @return the progress required for completion */
    public Integer getCompletionThreshold() {
        return completionThreshold;
    }

    /** @return the potential XP reward */
    public Integer getBaseXpReward() {
        return baseXpReward;
    }

    /** @return the XP already awarded for this activity */
    public Integer getAwardedXpReward() {
        return awardedXpReward;
    }

    /**
     * Records the XP awarded for this activity.
     *
     * @param awardedXpReward the awarded XP amount
     */
    public void setAwardedXpReward(Integer awardedXpReward) {
        this.awardedXpReward = awardedXpReward;
    }

    /** @return whether progress has reached the completion threshold */
    public boolean isComplete() {
        return progress >= completionThreshold;
    }

    /**
     * Returns completion status using the existing accessor name.
     *
     * @return whether the activity is complete
     */
    public boolean getIsComplete() {
        return isComplete();
    }

    /** @return whether progress contributes directly to the associated goal */
    public boolean isDoesContributeDirectlyToGoal() {
        return doesContributeDirectlyToGoal;
    }
}