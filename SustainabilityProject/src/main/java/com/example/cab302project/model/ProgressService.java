package com.example.cab302project.model;

import java.time.LocalDate;
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
     * <p>This implementation expects the user's supplied activities to be
     * completed and have known completion timestamps.</p>
     *
     * @param activities the completed activities to examine
     * @param userId the user whose completions should be counted
     * @param fromDate the first date to include
     * @param toDate the last date to include
     * @return completion counts grouped by date in chronological order
     */
    public Map<LocalDate, Integer> getDailyCompletions(
            List<Activity> activities,
            int userId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        Map<LocalDate, Integer> dailyCompletions = new TreeMap<>();

        for (Activity activity : activities) {
            if (!Integer.valueOf(userId).equals(activity.getUserId())) {
                continue;
            }

            LocalDate completionDate =
                    activity.getCompletedAt().toLocalDate();

            if (completionDate.isBefore(fromDate)
                    || completionDate.isAfter(toDate)) {
                continue;
            }

            dailyCompletions.merge(completionDate, 1, Integer::sum);
        }

        return dailyCompletions;
    }
}