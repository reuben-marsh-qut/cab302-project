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