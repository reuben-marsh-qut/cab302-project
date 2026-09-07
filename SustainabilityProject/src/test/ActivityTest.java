import com.example.cab302project.model.Activity;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActivityTest {
    private Activity activity;

    @BeforeEach
    public void setUp() {
        activity = new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                CompletionType.BINARY, LocalDateTime.of(2026, 9, 4, 12, 0),
                LocalDateTime.of(2026, 9, 11, 12, 0),
                0, 1, 10, 0, false);
    }

    @Test
    public void testGetId() {
        activity.setId(1);
        assertEquals(1, activity.getId());
    }

    @Test
    public void testGetGoalId() {
        assertEquals(1, activity.getGoalId());
    }

    @Test
    public void testGetHabitId() {
        assertEquals(1, activity.getHabitId());
    }

    @Test
    public void testGetUserId() {
        assertEquals(1, activity.getUserId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Mow the lawn", activity.getTitle());
    }

    @Test
    public void testGetCategory() {
        assertEquals(Category.BODY, activity.getCategory());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(LocalDateTime.of(2026, 9, 4, 12, 0), activity.getStartDateTime());
    }

    @Test
    public void testGetDueDate() {
        assertEquals(LocalDateTime.of(2026, 9, 11, 12, 0), activity.getDueDateTime());
    }

    @Test
    public void testGetProgress() {
        assertEquals(0, activity.getProgress());
    }

    @Test
    public void testGetThreshold() {
        assertEquals(1, activity.getCompletionThreshold());
    }

    @Test
    public void testGetCompletionType() {
        assertEquals(CompletionType.BINARY, activity.getActivityType());
    }

    @Test
    public void testGetBaseXp() {
        assertEquals(10, activity.getBaseXpReward());
    }

    @Test
    public void testGetAwardedXp() {
        assertEquals(0, activity.getAwardedXpReward());
    }

    @Test
    void testCreateActivityWithNoDueDateShouldSucceed() {
        Activity activity = new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                CompletionType.BINARY, LocalDateTime.of(2026, 9, 4, 12, 0),
                null, 0, 1, 10, 0, false);

        assertNull(activity.getDueDateTime(), "A goal with no due date should never expire.");
    }

    @Test
    void newActivityShouldNotBeComplete() {
        Activity activity = new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                CompletionType.BINARY, LocalDateTime.now(),
                LocalDateTime.of(2026, 9, 11, 12, 0),
                0, 1, 10, 0, false);
        assertTrue(activity.getProgress() < activity.getCompletionThreshold());
    }

    @Test
    void createActivityWithBlankTitleShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Activity(1, 1, 1, "", Category.BODY,
                        CompletionType.BINARY, LocalDateTime.of(2026, 9, 4, 12, 0),
                        LocalDateTime.of(2026, 9, 11, 12, 0),
                        0, 1, 10, 0, false),
                "A habit with a blank title should throw an exception.");
    }

    @Test
    void createActivityWithZeroThresholdShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                        CompletionType.BINARY, LocalDateTime.of(2026, 9, 4, 12, 0),
                        LocalDateTime.of(2026, 9, 11, 12, 0),
                        0, 0, 10, 0, false),
                "A goal with a completion threshold of zero should throw an exception.");
    }

    @Test
    void createActivityWithPastDueDateShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                        CompletionType.BINARY, LocalDateTime.of(2025, 9, 4, 12, 0),
                        LocalDateTime.of(2025, 9, 11, 12, 0),
                        0, 1, 10, 0, false),
                "A habit with a due date in the past should throw an exception.");
    }

    @Test
    void createActivityWithDueDateBeforeStartShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                        CompletionType.BINARY, LocalDateTime.of(2027, 9, 4, 12, 0),
                        LocalDateTime.of(2026, 9, 11, 12, 0),
                        0, 1, 10, 0, false),
                "A habit that is due before it starts should throw an exception.");
    }
}
