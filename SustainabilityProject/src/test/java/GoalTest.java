import com.example.cab302project.model.Goal;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTest {
    private Goal goal;

    @BeforeEach
    public void setUp() {
        goal = new Goal(1, "Tester Goal", Category.BODY, LocalDate.now(),
                LocalDate.now().plusDays(100), 1, 10,
                CompletionType.BINARY, false);
    }

    @Test
    public void testGetId() {
        goal.setId(1);
        assertEquals(1, goal.getId());
    }

    @Test
    public void testGetUserId() {
        assertEquals(1, goal.getUserId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Tester Goal", goal.getTitle());
    }

    @Test
    public void testGetCategory() {
        assertEquals(Category.BODY, goal.getCategory());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(LocalDate.now(), goal.getStartDate());
    }

    @Test
    public void testGetDueDate() {
        assertEquals(LocalDate.now().plusDays(100), goal.getDueDate());
    }

    @Test
    public void testGetProgress() {
        assertEquals(1, goal.getProgress());
    }

    @Test
    public void testGetThreshold() {
        assertEquals(10, goal.getThreshold());
    }

    @Test
    public void testGetCompletionType() {
        assertEquals(CompletionType.BINARY, goal.getCompletionType());
    }

    @Test
    public void testGetIsComplete() {
        assertFalse(goal.getIsComplete());
    }

    @Test
    void testCreateGoalWithNoDueDateShouldSucceed() {
        Goal goal = new Goal(1, "Tester Goal", Category.BODY, LocalDate.of(2026, 4, 1),
                null, 1, 10, CompletionType.BINARY, false);

        assertNull(goal.getDueDate(), "A goal with no due date should never expire.");
    }

    @Test
    void newGoalShouldNotBeComplete() {
        Goal goal = new Goal(1, "Tester Goal", Category.BODY, LocalDate.now(),
                LocalDate.now().plusDays(7), 0, 10, CompletionType.BINARY, false);
        assertTrue(goal.getProgress() < goal.getThreshold());
        assertFalse(goal.getIsComplete(), "A goal with no progress should not be complete.");
    }

    @Test
    void createGoalWithBlankTitleShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(1, "", Category.BODY, LocalDate.now(),
                        LocalDate.now().plusDays(7), 0, 10, CompletionType.BINARY, false),
                "A goal with a blank title should throw an exception.");
    }

    @Test
    void createGoalWithZeroThresholdShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(1, "Tester Goal", Category.BODY, LocalDate.now(),
                        LocalDate.now().plusDays(7), 0, 0, CompletionType.BINARY, false),
                "A goal with a completion threshold of zero should throw an exception.");
    }

    @Test
    void createGoalWithDueDateBeforeStartShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(1, "Tester Goal", Category.BODY, LocalDate.now(),
                        LocalDate.now().minusDays(7), 0, 10, CompletionType.BINARY, false),
                "A goal that is due before it starts should throw an exception.");
    }
    @Test
    public void testGoalWithPassedDeadlineCanBeCreated() {
        Goal pastGoal = new Goal(1, "Old goal", Category.BODY,
                LocalDate.now().minusDays(60), LocalDate.now().minusDays(30),
                2, 10, CompletionType.PROGRESSIVE, false);

        assertEquals("Old goal", pastGoal.getTitle());
    }

    @Test
    public void testGoalPastDeadlineWithoutMeetingTargetIsNotAchieved() {
        Goal missedGoal = new Goal(1, "Missed goal", Category.BODY,
                LocalDate.now().minusDays(60), LocalDate.now().minusDays(30),
                2, 10, CompletionType.PROGRESSIVE, false);

        assertTrue(missedGoal.isNotAchieved());
    }

    @Test
    public void testGoalPastDeadlineThatMetTargetIsNotFlagged() {
        Goal metGoal = new Goal(1, "Met goal", Category.BODY,
                LocalDate.now().minusDays(60), LocalDate.now().minusDays(30),
                10, 10, CompletionType.PROGRESSIVE, true);

        assertFalse(metGoal.isNotAchieved());
    }

    @Test
    public void testGoalStillWithinDeadlineIsNotFlagged() {
        Goal activeGoal = new Goal(1, "Active goal", Category.BODY,
                LocalDate.now(), LocalDate.now().plusDays(30),
                2, 10, CompletionType.PROGRESSIVE, false);

        assertFalse(activeGoal.isNotAchieved());
    }

    @Test
    public void testGoalWithNoDeadlineIsNeverNotAchieved() {
        Goal openGoal = new Goal(1, "Open goal", Category.BODY,
                LocalDate.now(), null,
                0, 10, CompletionType.PROGRESSIVE, false);

        assertFalse(openGoal.isNotAchieved());
    }
}
