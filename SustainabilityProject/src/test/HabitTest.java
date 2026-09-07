import com.example.cab302project.model.Activity;
import com.example.cab302project.model.Goal;
import com.example.cab302project.model.Habit;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HabitTest {
    private Habit habit;

    @BeforeEach
    public void setUp() {
        habit = new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                CompletionType.BINARY, 1, LocalDate.now(),
                LocalDate.now().plusDays(30), 21, 31, 41,
                21, false);
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
        assertEquals(LocalDate.now(), habit.getStartDateTime());
    }

    @Test
    public void testGetDueDateTime() {
        assertEquals(LocalDate.now().plusDays(30), habit.getDueDateTime());
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

    @Test
    void testCreateHabitWithNoDueDateShouldSucceed() {
        Habit habit = new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                CompletionType.BINARY, 1, LocalDate.now(),
                null, 0, 31, 41,
                21, false);

        assertNull(habit.getDueDateTime(), "A goal with no due date should never expire.");
    }

    @Test
    void newHabitShouldNotBeComplete() {
        Habit habit = new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                CompletionType.BINARY, 1, LocalDate.now(),
                LocalDate.now().plusDays(30), 0, 31, 41,
                21, false);
        assertTrue(habit.getProgress() < habit.getCompletionThreshold());
    }

    @Test
    void createHabitWithBlankTitleShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Habit(1, 1, "", Category.MIND,
                        CompletionType.BINARY, 1, LocalDate.now(),
                        LocalDate.now().plusDays(30), 21, 31, 41,
                        21, false),
                "A habit with a blank title should throw an exception.");
    }

    @Test
    void createHabitWithZeroThresholdShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                        CompletionType.BINARY, 1, LocalDate.now(),
                        LocalDate.now().plusDays(30), 0, 0, 41,
                        21, false),
                "A goal with a completion threshold of zero should throw an exception.");
    }

    @Test
    void createHabitWithPastDueDateShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                        CompletionType.BINARY, 1, LocalDate.now().minusDays(60),
                        LocalDate.now().minusDays(30), 21, 31, 41,
                        21, false),
                "A habit with a due date in the past should throw an exception.");
    }

    @Test
    void createHabitWithDueDateBeforeStartShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Habit(1, 1, "Read 5 pages a day", Category.MIND,
                        CompletionType.BINARY, 1, LocalDate.now(),
                        LocalDate.now().minusDays(30), 21, 31, 41,
                        21, false),
                "A habit that is due before it starts should throw an exception.");
    }
}
