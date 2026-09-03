import com.example.cab302project.model.Category;
import com.example.cab302project.model.Goal;
import com.example.cab302project.model.Category;
import com.example.cab302project.model.Goal;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class GoalTest {

    @Test
    void createGoalWithValidDetailsShouldSucceed() {
        Goal goal = new Goal("Meditate 600 minutes", Category.MIND, 600,
                LocalDate.now().plusDays(30));

        assertEquals("Meditate 600 minutes", goal.getTitle());
        assertEquals(Category.MIND, goal.getCategory());
        assertEquals(600, goal.getTarget());
        assertEquals(0, goal.getProgress());
    }

    @Test
    void createGoalWithPastDeadlineShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal("Walk more", Category.BODY, 100,
                        LocalDate.now().minusDays(1)),
                "A goal with a deadline in the past should throw an exception.");
    }

    @Test
    void createGoalWithBlankTitleShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal("   ", Category.BODY, 100,
                        LocalDate.now().plusDays(7)),
                "A goal with a blank title should throw an exception.");
    }

    @Test
    void createGoalWithZeroTargetShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Goal("Call a friend", Category.SOCIAL, 0,
                        LocalDate.now().plusDays(7)),
                "A goal with a target of zero should throw an exception.");
    }
}