package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

/**
 * A suggested starting point for a new goal. Templates prefill the goal
 * creation form; the user can change anything before saving.
 */
public class HabitTemplate {
    private String title;
    private Category category;
    private TaskType taskType;
    private int repeatFrequency;
    private RepeatFrequencyType repeatFrequencyType;
    private int target;

    /**
     * Creates a goal template.
     * @param title What the goal suggests doing.
     * @param category The wellbeing area this template belongs to.
     * @param taskType Whether it is worked towards or one and done.
     * @param target The value that must be reached to complete it.
     * @throws IllegalArgumentException if any of the details are invalid.
     */
    public HabitTemplate(String title, Category category,
                         TaskType taskType, int target, RepeatFrequencyType repeatFrequencyType, int repeatFrequency) {
        validateTitle(title);
        validateTarget(target);

        this.title = title;
        this.category = category;
        this.taskType = taskType;
        this.target = target;
        this.repeatFrequencyType = repeatFrequencyType;
        this.repeatFrequency = repeatFrequency;
    }

    private static void validateTitle(String templateTitle) {
        if (templateTitle == null || templateTitle.isBlank()) {
            throw new IllegalArgumentException(
                    "Goal template title must not be blank.");
        }
    }

    private static void validateTarget(int templateTarget) {
        if (templateTarget < 1) {
            throw new IllegalArgumentException(
                    "Goal template target must be at least one.");
        }
    }

    /**
     * Returns the suggested templates for one category and completion type.
     * @param category The category to find templates for.
     * @param taskType Whether the goal is worked towards or one and done.
     * @return The matching templates, or an empty list if there are none.
     */
    public static List<HabitTemplate> getTemplatesFor(Category category,
                                                      TaskType taskType) {
        List<HabitTemplate> matching = new ArrayList<>();

        for (HabitTemplate template : allTemplates()) {
            if (template.getCategory() == category
                    && template.getTaskType() == taskType) {
                matching.add(template);
            }
        }

        return matching;
    }

    /**
     * The full set of starter templates the app offers.
     */
    private static List<HabitTemplate> allTemplates() {
        List<HabitTemplate> templates = new ArrayList<>();

        // Mind - worked towards over time
        templates.add(new HabitTemplate("Meditate for 15 minutes each day",
                Category.MIND, TaskType.PROGRESSIVE, 15, RepeatFrequencyType.DAILY, 1));
        templates.add(new HabitTemplate("Read 2 books each week",
                Category.MIND, TaskType.PROGRESSIVE, 2, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Journal twice each day",
                Category.MIND, TaskType.PROGRESSIVE, 2, RepeatFrequencyType.DAILY,1));

        // Mind - one and done
        templates.add(new HabitTemplate("Complete a random act of kindness each month",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY,1));
        templates.add(new HabitTemplate("Spend spend a day offline each week",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Practice music every three days",
                Category.MIND, TaskType.BINARY, 1, RepeatFrequencyType.DAILY, 2));

        // Body - worked towards over time
        templates.add(new HabitTemplate("Walk 10, 000 steps each day",
                Category.BODY, TaskType.PROGRESSIVE, 10000, RepeatFrequencyType.DAILY,1));
        templates.add(new HabitTemplate("Cook 5 meals yourself each week",
                Category.BODY, TaskType.PROGRESSIVE, 5, RepeatFrequencyType.WEEKLY,1));
        templates.add(new HabitTemplate("Swim 7 kilometres each week",
                Category.BODY, TaskType.PROGRESSIVE, 6,RepeatFrequencyType.WEEKLY,1));

        // Body - one and done
        templates.add(new HabitTemplate("Go to the gym every two days",
                Category.BODY, TaskType.BINARY, 1, RepeatFrequencyType.DAILY, 2));
        templates.add(new HabitTemplate("Go for a walk outside each day",
                Category.BODY, TaskType.BINARY, 1,RepeatFrequencyType.DAILY,1));
        templates.add(new HabitTemplate("Try a new sport every three months",
                Category.BODY, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY,3));

        // World - worked towards over time
        templates.add(new HabitTemplate("Swap 5 car trips each week for walking or public transport",
                Category.WORLD, TaskType.PROGRESSIVE, 5, RepeatFrequencyType.WEEKLY, 1));
        templates.add(new HabitTemplate("Avoid 100 single-use plastic items each year",
                Category.WORLD, TaskType.PROGRESSIVE, 100, RepeatFrequencyType.YEARLY,1));
        templates.add(new HabitTemplate("Compost at least 4 kilograms of food waste each month",
                Category.WORLD, TaskType.PROGRESSIVE, 4, RepeatFrequencyType.MONTHLY,1));

        // World - one and done
        templates.add(new HabitTemplate("Compost all your food waste each month",
                Category.WORLD, TaskType.BINARY, 1, RepeatFrequencyType.MONTHLY, 1));
        templates.add(new HabitTemplate("Join a local clean-up day twice a year",
                Category.WORLD, TaskType.BINARY, 1,RepeatFrequencyType.MONTHLY, 6));
        templates.add(new HabitTemplate("Tend to your garden each week",
                Category.WORLD, TaskType.BINARY, 1,RepeatFrequencyType.WEEKLY,1));

        return templates;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getTarget() {
        return target;
    }

    public int getRepeatFrequency() {
        return repeatFrequency;
    }

    public RepeatFrequencyType getRepeatFrequencyType() {
        return repeatFrequencyType;
    }
}