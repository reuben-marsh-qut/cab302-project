import com.example.cab302project.model.Activity;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActivityTest {
    private Activity activity;

    @BeforeEach
    public void setUp() {
        activity = new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                CompletionType.BINARY, LocalDateTime.of(2026, 9, 4, 12, 0),
                LocalDateTime.of(2026, 9, 11, 12, 0),
                0, 1, 10, 0);
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
}
