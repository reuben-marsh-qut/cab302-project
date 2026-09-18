package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.util.ArrayList;
import java.util.List;

/**
 * A suggested starting point for a new activity. Templates prefill the activity
 * creation form; the user can change anything before saving.
 */
public class ActivityTemplate {
    private String title;
    private Category category;
    private CompletionType completionType;
    private int target;

    /**
     * Creates an activity template.
     * @param title What the activity suggests doing.
     * @param category The wellbeing area this template belongs to.
     * @param completionType Whether it is worked towards or one and done.
     * @param target The value that must be reached to complete it.
     * @throws IllegalArgumentException if any of the details are invalid.
     */
    public ActivityTemplate(String title, Category category,
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
                    "Activity template title must not be blank.");
        }
    }

    private static void validateTarget(int templateTarget) {
        if (templateTarget < 1) {
            throw new IllegalArgumentException(
                    "Activity template target must be at least one.");
        }
    }

    /**
     * Returns the suggested templates for one category and completion type.
     * @param category The category to find templates for.
     * @param completionType Whether the activity is worked towards or one and done.
     * @return The matching templates, or an empty list if there are none.
     */
    public static List<ActivityTemplate> getTemplatesFor(Category category,
                                                     CompletionType completionType) {
        List<ActivityTemplate> matching = new ArrayList<>();

        for (ActivityTemplate template : allTemplates()) {
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
    private static List<ActivityTemplate> allTemplates() {
        List<ActivityTemplate> templates = new ArrayList<>();

        // Mind - worked towards over time
        templates.add(new ActivityTemplate("Meditate for 30 minutes",
                Category.MIND, CompletionType.PROGRESSIVE, 30));
        templates.add(new ActivityTemplate("Read 1 book",
                Category.MIND, CompletionType.PROGRESSIVE, 12));
        templates.add(new ActivityTemplate("Write in your journal 3 times this week",
                Category.MIND, CompletionType.PROGRESSIVE, 100));

        // Mind - one and done
        templates.add(new ActivityTemplate("Write in your journal today",
                Category.MIND, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Spend today without technology",
                Category.MIND, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Practice your instrument today",
                Category.MIND, CompletionType.BINARY, 1));

        // Body - worked towards over time
        templates.add(new ActivityTemplate("Walk 8,000 steps",
                Category.BODY, CompletionType.PROGRESSIVE, 8000));
        templates.add(new ActivityTemplate("Cook for yourself 5 times this week",
                Category.BODY, CompletionType.PROGRESSIVE, 5));
        templates.add(new ActivityTemplate("Run a total of 30k",
                Category.BODY, CompletionType.PROGRESSIVE, 30));

        // Body - one and done
        templates.add(new ActivityTemplate("Try a new exercise",
                Category.BODY, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Go on a bush walk",
                Category.BODY, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Pick up a sport you used to play",
                Category.BODY, CompletionType.BINARY, 1));

        // World - worked towards over time
        templates.add(new ActivityTemplate("Commute using a bike for 3 days",
                Category.WORLD, CompletionType.PROGRESSIVE, 3));
        templates.add(new ActivityTemplate("PLACEHOLDER",
                Category.WORLD, CompletionType.PROGRESSIVE, 100));
        templates.add(new ActivityTemplate("PLACEHOLDER",
                Category.WORLD, CompletionType.PROGRESSIVE, 50));

        // World - one and done
        templates.add(new ActivityTemplate("Plant a tree",
                Category.WORLD, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Mow your lawn",
                Category.WORLD, CompletionType.BINARY, 1));
        templates.add(new ActivityTemplate("Water your plants",
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