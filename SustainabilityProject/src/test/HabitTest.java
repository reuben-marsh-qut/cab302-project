import com.example.cab302project.model.Activity;
import com.example.cab302project.model.Habit;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HabitTest {
    private Habit habit;

    @BeforeEach
    public void setUp() {
        habit = new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                CompletionType.BINARY, 1, LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1), 21, 31, 41,
                21);
    }

    @Test
    public void testGetId() {
        habit.setId(1);
        assertEquals(1, habit.getId());
    }

    @Test
    public void testGetGoalId() {
        assertEquals(1, habit.getGoalId());
    }

    @Test
    public void testGetUserId() {
        assertEquals(1, habit.getUserId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Read 5 pages a day", habit.getTitle());
    }

    @Test
    public void testGetCategory() {
        assertEquals(Category.MIND, habit.getCategory());
    }

    @Test
    public void testGetCompletionType() {
        assertEquals(CompletionType.BINARY, habit.gethabitType());
    }

    @Test
    public void testGetRepeatFrequency() {
        assertEquals(1, habit.getRepeatFrequencyDays());
    }

    @Test
    public void testGetStartDateTime() {
        assertEquals(LocalDate.of(2025, 1, 1), habit.getStartDateTime());
    }

    @Test
    public void testGetDueDateTime() {
        assertEquals(LocalDate.of(2025, 2, 1), habit.getDueDateTime());
    }

    @Test
    public void testGetProgress() {
        assertEquals(21, habit.getProgress());
    }

    @Test
    public void testGetCompletionThreshold() {
        assertEquals(31, habit.getCompletionThreshold());
    }

    @Test
    public void testGetBaseXpReward() {
        assertEquals(41, habit.getBaseXpReward());
    }

    @Test
    public void testGetAwardedXp() {
        assertEquals(21, habit.getAwardedXpReward());
    }
}
