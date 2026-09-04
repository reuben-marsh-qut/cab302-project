import com.example.cab302project.model.Goal;
import com.example.cab302project.model.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoalTest {
    private Goal goal;

    @BeforeEach
    public void setUp() {
        goal = new Goal(1, "Tester Goal", Category.BODY, LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 12, 1), 1, 10,
                Goal.CompletionType.BINARY, false);
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
        assertEquals(LocalDate.of(2026, 4, 1), goal.getStartDate());
    }

    @Test
    public void testGetDueDate() {
        assertEquals(LocalDate.of(2026, 12, 1), goal.getDueDate());
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
        assertEquals(Goal.CompletionType.BINARY, goal.getCompletionType());
    }

    @Test
    public void testGetIsComplete() {
        assertFalse(goal.getIsComplete());
    }

}
