import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ProgressService;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests daily activity completion counts used by the progress report.
 */
public class ProgressServiceTest {

    @Test
    void twoActivitiesCompletedOnTheSameDayProduceCountOfTwo() {
        // Arrange: user 1 completes two separate activities on October 5.
        LocalDate day = LocalDate.of(2026, 10, 5);

        Activity first = completedActivity(
                1,
                1,
                day.atTime(9, 0)
        );

        Activity second = completedActivity(
                2,
                1,
                day.atTime(15, 0)
        );

        ProgressService service = new ProgressService();

        // Act: calculate the daily counts for that day.
        Map<LocalDate, Integer> result = service.getDailyCompletions(
                List.of(first, second),
                1,
                day,
                day
        );

        // Assert: both activities contribute to the same day's count.
        assertEquals(
                Map.of(day, 2),
                result,
                "Two activities completed on the same day should count as two"
        );
    }

    @Test
    void activitiesBelongingToAnotherUserAreExcluded() {
        // Arrange: two different users complete an activity on the same day.
        LocalDate day = LocalDate.of(2026, 10, 5);

        Activity ownActivity = completedActivity(
                1,
                1,
                day.atTime(9, 0)
        );

        Activity anotherUsersActivity = completedActivity(
                2,
                2,
                day.atTime(15, 0)
        );

        ProgressService service = new ProgressService();

        // Act: request the report for user 1 only.
        Map<LocalDate, Integer> result = service.getDailyCompletions(
                List.of(ownActivity, anotherUsersActivity),
                1,
                day,
                day
        );

        // Assert: user 2's activity must not contribute to user 1's report.
        assertEquals(
                Map.of(day, 1),
                result,
                "Only the requested user's activities should be counted"
        );
    }

    @Test
    void selectedDateRangeIncludesBoundaryDatesAndExcludesOutsideDates() {
        // Arrange: completions before, within and after the selected range.
        LocalDate fromDate = LocalDate.of(2026, 10, 5);
        LocalDate middleDate = fromDate.plusDays(1);
        LocalDate toDate = fromDate.plusDays(2);

        Activity beforeRange = completedActivity(
                1,
                1,
                fromDate.minusDays(1).atTime(23, 59, 59)
        );

        Activity onFirstDate = completedActivity(
                2,
                1,
                fromDate.atStartOfDay()
        );

        Activity inMiddle = completedActivity(
                3,
                1,
                middleDate.atTime(12, 0)
        );

        Activity onLastDate = completedActivity(
                4,
                1,
                toDate.atTime(23, 59, 59)
        );

        Activity afterRange = completedActivity(
                5,
                1,
                toDate.plusDays(1).atStartOfDay()
        );

        ProgressService service = new ProgressService();

        // Act: request October 5 through October 7, inclusive.
        Map<LocalDate, Integer> result = service.getDailyCompletions(
                List.of(
                        beforeRange,
                        onFirstDate,
                        inMiddle,
                        onLastDate,
                        afterRange
                ),
                1,
                fromDate,
                toDate
        );

        // Assert: only dates inside the inclusive range are counted.
        assertEquals(
                Map.of(
                        fromDate, 1,
                        middleDate, 1,
                        toDate, 1
                ),
                result,
                "The report should include both boundary dates "
                        + "and exclude dates outside the selected range"
        );
    }

    @Test
    void completedActivitiesWithoutKnownCompletionTimeAreExcluded() {
        LocalDate day = LocalDate.of(2026, 10, 5);

        Activity datedActivity = completedActivity(
                1,
                1,
                day.atTime(9, 0)
        );

        Activity historicalActivity = completedActivity(
                2,
                1,
                day.atTime(15, 0)
        );

        // Simulate a completed record loaded from a migrated database:
        // its progress is complete, but its completion timestamp is unknown.
        historicalActivity.setCompletedAt(null);

        ProgressService service = new ProgressService();

        // Act: include the historical record in the supplied activities.
        Map<LocalDate, Integer> result = service.getDailyCompletions(
                List.of(historicalActivity, datedActivity),
                1,
                day,
                day
        );

        // Assert: the report counts only the activity with a known date.
        assertEquals(
                Map.of(day, 1),
                result,
                "An unknown completion time should be skipped "
                        + "without preventing dated activities from being counted"
        );
    }

    /**
     * Creates an activity with a known completion timestamp.
     *
     * @param activityId the activity's identifier
     * @param userId the activity owner's identifier
     * @param completedAt the time the activity reached its target
     * @return the completed activity
     */
    private Activity completedActivity(
            int activityId,
            int userId,
            LocalDateTime completedAt
    ) {
        Activity activity = new Activity(
                null,
                null,
                userId,
                "Test activity " + activityId,
                Category.values()[0],
                TaskType.values()[0],
                completedAt.minusDays(1),
                null,
                0,
                1,
                20,
                0,
                false
        );

        activity.setId(activityId);
        activity.setProgress(1, completedAt);

        return activity;
    }
}