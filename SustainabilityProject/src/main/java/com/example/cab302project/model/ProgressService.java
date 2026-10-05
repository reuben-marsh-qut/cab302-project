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
     * Groups supplied activity completion timestamps into daily counts.
     *
     * <p>This initial implementation expects completed activities with known
     * completion timestamps. User and date filtering will be introduced
     * through subsequent test-driven development steps.</p>
     *
     * @param activities the completed activities to examine
     * @param userId the intended reporting user; filtering is not yet implemented
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
            LocalDate completionDate =
                    activity.getCompletedAt().toLocalDate();

            dailyCompletions.merge(completionDate, 1, Integer::sum);
        }

        return dailyCompletions;
    }
}