import com.example.cab302project.model.Category;
import com.example.cab302project.model.Goal;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoalTest {

    private static final int USER_ID = 1;

    @Test
    void createGoalWithValidDetailsShouldSucceed() {
        Goal goal = new Goal(USER_ID, "Meditate 600 minutes", Category.MIND, 600,
                LocalDate.now(), LocalDate.now().plusDays(30));

        assertEquals(USER_ID, goal.getUserId());
        assertEquals("Meditate 600 minutes", goal.getGoalTitle());
        assertEquals(Category.MIND, goal.getCategory());
        assertEquals(600, goal.getCompletionThreshold());
        assertEquals(0, goal.getProgress());
    }

    @Test
    void createGoalWithNoDueDateShouldSucceed() {
        Goal goal = new Goal(USER_ID, "Meditate regularly", Category.MIND, 600,
                LocalDate.now(), null);

        assertNull(goal.getDueDate(), "A goal with no due date should never expire.");
    }

    @Test
    void newGoalShouldNotBeComplete() {
        Goal goal = new Goal(USER_ID, "Walk 10000 steps", Category.BODY, 10000,
                LocalDate.now(), LocalDate.now().plusDays(7));

        assertFalse(goal.isComplete(), "A goal with no progress should not be complete.");
    }

    @Test
    void createGoalWithBlankTitleShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(USER_ID, "   ", Category.BODY, 100,
                        LocalDate.now(), LocalDate.now().plusDays(7)),
                "A goal with a blank title should throw an exception.");
    }

    @Test
    void createGoalWithZeroThresholdShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(USER_ID, "Call a friend", Category.SOCIAL, 0,
                        LocalDate.now(), LocalDate.now().plusDays(7)),
                "A goal with a completion threshold of zero should throw an exception.");
    }

    @Test
    void createGoalWithPastDueDateShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(USER_ID, "Walk more", Category.BODY, 100,
                        LocalDate.now().minusDays(10), LocalDate.now().minusDays(1)),
                "A goal with a due date in the past should throw an exception.");
    }

    @Test
    void createGoalWithDueDateBeforeStartShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(USER_ID, "Walk more", Category.BODY, 100,
                        LocalDate.now().plusDays(30), LocalDate.now().plusDays(7)),
                "A goal that is due before it starts should throw an exception.");
    }
}