package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Calculates activity completion statistics for progress reports.
 *
 * <p>Calculations use supplied activities without accessing the database
 * or JavaFX controls.</p>
 */
public class ProgressService {

    /**
     * Counts completed Mind, Body and Social activities in an inclusive period.
     * Uses the same user and completion-date rules as the daily report.
     * The supplied activities are expected to be completed.
     */
    public Map<Category, Integer> getCategoryCompletions(
            List<Activity> activities, int userId,
            LocalDate fromDate, LocalDate toDate
    ) {
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException(
                    "End date must be on or after start date."
            );
        }

        Map<Category, Integer> counts = new EnumMap<>(Category.class);
        counts.put(Category.MIND, 0);
        counts.put(Category.BODY, 0);
        counts.put(Category.SOCIAL, 0);

        for (Activity activity : activities) {
            if (!Integer.valueOf(userId).equals(activity.getUserId())
                    || activity.getCompletedAt() == null
                    || !counts.containsKey(activity.getCategory())) {
                continue;
            }

            LocalDate date = activity.getCompletedAt().toLocalDate();
            if (!date.isBefore(fromDate) && !date.isAfter(toDate)) {
                counts.merge(activity.getCategory(), 1, Integer::sum);
            }
        }

        return counts;
    }

    /**
     * Groups a user's activity completions into daily counts within an
     * inclusive date range.
     *
     * <p>Activities belonging to other users or completed outside the
     * selected range are excluded. Both boundary dates are included,
     * regardless of the time of day the activity was completed.</p>
     *
     * <p>Activities with unknown completion timestamps are excluded because
     * their completion date cannot be determined accurately. This
     * implementation expects the supplied activities to be completed.</p>
     *
     * <p>Every date in the selected range appears in the result.
     * Dates without completions have a count of zero.</p>
     *
     * @param activities the completed activities to examine
     * @param userId the user whose completions should be counted
     * @param fromDate the first date to include
     * @param toDate the last date to include
     * @return completion counts for every selected date in chronological order
     * @throws IllegalArgumentException if toDate is before fromDate
     */
    public Map<LocalDate, Integer> getDailyCompletions(
            List<Activity> activities,
            int userId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        if (toDate.isBefore(fromDate)) {
            throw new IllegalArgumentException(
                    "End date must be on or after start date."
            );
        }

        Map<LocalDate, Integer> dailyCompletions = new TreeMap<>();

        LocalDate date = fromDate;

        while (!date.isAfter(toDate)) {
            dailyCompletions.put(date, 0);

            if (date.equals(toDate)) {
                break;
            }

            date = date.plusDays(1);
        }

        for (Activity activity : activities) {
            if (!Integer.valueOf(userId).equals(activity.getUserId())) {
                continue;
            }

            LocalDateTime completedAt = activity.getCompletedAt();

            if (completedAt == null) {
                continue;
            }

            LocalDate completionDate = completedAt.toLocalDate();

            if (completionDate.isBefore(fromDate)
                    || completionDate.isAfter(toDate)) {
                continue;
            }

            dailyCompletions.merge(completionDate, 1, Integer::sum);
        }

        return dailyCompletions;
    }
}
