package com.example.cab302project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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