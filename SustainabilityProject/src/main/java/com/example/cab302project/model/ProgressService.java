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
     * Groups a user's supplied activity completion timestamps into daily counts.
     *
     * <p>Activities belonging to other users are excluded. This implementation
     * expects the user's activities to be completed and have known completion
     * timestamps. Date filtering will be introduced through subsequent
     * test-driven development steps.</p>
     *
     * @param activities the completed activities to examine
     * @param userId the user whose completions should be counted
     * @param fromDate the intended first date; filtering is not yet implemented
     * @param toDate the intended last date; filtering is not yet implemented
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

            dailyCompletions.merge(completionDate, 1, Integer::sum);
        }

        return dailyCompletions;
    }
}