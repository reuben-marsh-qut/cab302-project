package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.util.ArrayList;
import java.util.List;

/**
 * A suggested starting point for a new goal. Templates prefill the goal
 * creation form; the user can change anything before saving.
 */
public class GoalTemplate {
    private String title;
    private Category category;
    private CompletionType completionType;
    private int target;

    /**
     * Creates a goal template.
     * @param title What the goal suggests doing.
     * @param category The wellbeing area this template belongs to.
     * @param completionType Whether it is worked towards or one and done.
     * @param target The value that must be reached to complete it.
     * @throws IllegalArgumentException if any of the details are invalid.
     */
    public GoalTemplate(String title, Category category,
                        CompletionType completionType, int target) {
        validateTitle(title);
        validateTarget(target);

        this.title = title;
        this.category = category;
        this.completionType = completionType;
        this.target = target;
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
     * @param completionType Whether the goal is worked towards or one and done.
     * @return The matching templates, or an empty list if there are none.
     */
    public static List<GoalTemplate> getTemplatesFor(Category category,
                                                     CompletionType completionType) {
        List<GoalTemplate> matching = new ArrayList<>();

        for (GoalTemplate template : allTemplates()) {
            if (template.getCategory() == category
                    && template.getCompletionType() == completionType) {
                matching.add(template);
            }
        }

        return matching;
    }

    /**
     * The full set of starter templates the app offers.
     */
    private static List<GoalTemplate> allTemplates() {
        List<GoalTemplate> templates = new ArrayList<>();

        // Mind - worked towards over time
        templates.add(new GoalTemplate("Meditate for 600 minutes",
                Category.MIND, CompletionType.PROGRESSIVE, 600));
        templates.add(new GoalTemplate("Read 12 books",
                Category.MIND, CompletionType.PROGRESSIVE, 12));
        templates.add(new GoalTemplate("Journal for 100 days",
                Category.MIND, CompletionType.PROGRESSIVE, 100));

        // Mind - one and done
        templates.add(new GoalTemplate("Complete an 8-week mindfulness course",
                Category.MIND, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Spend a full weekend offline",
                Category.MIND, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Learn to play one song on an instrument",
                Category.MIND, CompletionType.BINARY, 1));

        // Body - worked towards over time
        templates.add(new GoalTemplate("Walk 100,000 steps",
                Category.BODY, CompletionType.PROGRESSIVE, 100000));
        templates.add(new GoalTemplate("Cook 50 meals from scratch",
                Category.BODY, CompletionType.PROGRESSIVE, 50));
        templates.add(new GoalTemplate("Swim 20 kilometres",
                Category.BODY, CompletionType.PROGRESSIVE, 20));

        // Body - one and done
        templates.add(new GoalTemplate("Run a 10 kilometre race",
                Category.BODY, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Finish a multi-day hike",
                Category.BODY, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Try a new sport at a local club",
                Category.BODY, CompletionType.BINARY, 1));

        // World - worked towards over time
        templates.add(new GoalTemplate("Swap 30 car trips for walking or public transport",
                Category.WORLD, CompletionType.PROGRESSIVE, 30));
        templates.add(new GoalTemplate("Avoid 100 single-use plastic items",
                Category.WORLD, CompletionType.PROGRESSIVE, 100));
        templates.add(new GoalTemplate("Compost 50 kilograms of food waste",
                Category.WORLD, CompletionType.PROGRESSIVE, 50));

        // World - one and done
        templates.add(new GoalTemplate("Plant a tree",
                Category.WORLD, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Join a local clean-up day",
                Category.WORLD, CompletionType.BINARY, 1));
        templates.add(new GoalTemplate("Switch your home to a green energy plan",
                Category.WORLD, CompletionType.BINARY, 1));

        return templates;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public CompletionType getCompletionType() {
        return completionType;
    }

    public int getTarget() {
        return target;
    }
}