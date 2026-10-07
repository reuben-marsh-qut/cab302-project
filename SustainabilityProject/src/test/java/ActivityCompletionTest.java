import com.example.cab302project.model.Activity;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests recording the time at which an activity is completed.
 */
public class ActivityCompletionTest {

    private Activity activity;

    /**
     * Creates an incomplete activity requiring five units of progress.
     */
    @BeforeEach
    void setUp() {
        activity = new Activity(
                0,
                0,
                1,
                "Take five walks",
                Category.BODY,
                TaskType.PROGRESSIVE,
                LocalDateTime.of(2026, 10, 1, 9, 0),
                null,
                0,
                5,
                100,
                0,
                false
        );
    }

    /**
     * An incomplete activity has no recorded completion time.
     */
    @Test
    void incompleteActivityHasNoCompletionTime() {
        assertNull(activity.getCompletedAt());
    }

    /**
     * Progress below the target must not record a completion.
     */
    @Test
    void progressBelowThresholdDoesNotRecordCompletionTime() {
        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 10, 5, 15, 0);

        activity.setProgress(4, updatedAt);

        assertFalse(activity.isComplete());
        assertNull(activity.getCompletedAt());
    }

    /**
     * Reaching the target records the time of that progress update.
     */
    @Test
    void reachingThresholdRecordsCompletionTime() {
        LocalDateTime completedAt =
                LocalDateTime.of(2026, 10, 5, 15, 0);

        activity.setProgress(5, completedAt);

        assertTrue(activity.isComplete());
        assertEquals(completedAt, activity.getCompletedAt());
    }
}