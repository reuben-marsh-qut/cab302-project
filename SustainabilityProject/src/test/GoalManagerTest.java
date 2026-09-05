import com.example.cab302project.model.Goal;
import com.example.cab302project.model.GoalManager;
import com.example.cab302project.model.MockGoalDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoalManagerTest {
    private GoalManager goalManager;

    private Goal[] goals = {
            new Goal(1, "Reuben's Goal", Category.BODY, LocalDate.of(2021, 4, 1),
                    LocalDate.of(2023, 12, 1), 11, 10,
                    CompletionType.BINARY, true),
            new Goal(2, "Patrick's Goal", Category.MIND, LocalDate.of(2025, 10, 1),
                    LocalDate.of(2025, 11, 1), 8, 10,
                    CompletionType.CONSTRAINT, false),
            new Goal(3, "Sujhav's Goal", Category.SOCIAL, LocalDate.of(2026, 10, 1),
                    LocalDate.of(2026, 10, 7), 10, 12,
                    CompletionType.BINARY, false),
            new Goal(4, "Angus' Goal", Category.SOCIAL, LocalDate.of(2026, 4, 1),
                    LocalDate.of(2026, 12, 1), 11, 10,
                    CompletionType.BINARY, true),
            new Goal(5, "Seb's Goal", Category.BODY, LocalDate.of(2020, 12, 10),
                    LocalDate.of(2026, 12, 10), 1, 10,
                    CompletionType.PROGRESSIVE, false),
            new Goal(5, "Seb's Second Goal", Category.MIND, LocalDate.of(2020, 12, 10),
                    LocalDate.of(2026, 12, 10), 1, 10,
                    CompletionType.PROGRESSIVE, false)
    };

    @BeforeEach
    public void setUp() {
        goalManager = new GoalManager(new MockGoalDAO());
    }

    @Test
    public void testSearchByTitleInOneGoal() {
        goalManager.addGoal(goals[0]); // add just the first goal
        List<Goal> goals = goalManager.searchGoalsByTitle("Reuben's Goal"); // search for goal based on firstname
        assertEquals(1, goals.size()); // assertion - correct quantity
        assertEquals(this.goals[0], goals.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByCategoryInOneGoal() {
        goalManager.addGoal(goals[0]); // add just the first goal
        List<Goal> goals = goalManager.searchGoalsByCategory(Category.BODY); // search for goal based on firstname
        assertEquals(1, goals.size()); // assertion - correct quantity
        assertEquals(this.goals[0], goals.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByTitleInMultipleGoals() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle("Patrick's Goal"); // search for goal based on firstname
        assertEquals(1, goals.size()); // assertion - correct quantity
        for (Goal goal : goals) { // assertion
            assertTrue(goal.getTitle().equals("Patrick's Goal"));
        }
    }

    @Test
    public void testSearchByCategoryInMultipleGoals() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByCategory(Category.BODY); // search for goal based on firstname
        assertEquals(2, goals.size()); // assertion - correct quantity
        for (Goal goal : goals) { // assertion
            assertTrue(goal.getCategory().equals(Category.BODY));
        }
    }

    @Test
    public void testSearchNoResults() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle("Joseph's Goal");
        assertEquals(0, goals.size());
    }

    @Test
    public void testSearchEmptyQuery() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle("");
        assertEquals(6, goals.size());
    }

    @Test
    public void testSearchNullQuery() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle(null);
        assertEquals(6, goals.size());
    }

    @Test
    public void testSearchByTitleCaseInsensitive() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle("reuben's goal");
        assertEquals(1, goals.size());
        for (Goal goal : goals) {
            assertTrue(goal.getTitle().equalsIgnoreCase("Reuben's Goal"));
        }
    }

    @Test
    public void testSearchPartialQuery() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByTitle("b's");
        assertEquals(2, goals.size());
        assertTrue(goals.get(0).getTitle().equals("Seb's Goal"));
        assertTrue(goals.get(1).getTitle().equals("Seb's Second Goal"));
    }

    @Test
    public void testSearchEmptyGoals() {
        List<Goal> goals = goalManager.searchGoalsByTitle("Sujhav's Goal");
        assertEquals(0, goals.size());
    }

    @Test
    public void testSearchByUserId() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List<Goal> goals = goalManager.searchGoalsByUserId(2);
        assertEquals(1, goals.size());
        assertEquals("Patrick's Goal", goals.get(0).getTitle());
    }

    @Test
    public void testSearchByDate() {
        for (Goal goal : goals) { // add all goals
            goalManager.addGoal(goal);
        }
        List <Goal> goals = goalManager.getGoalsBeforeDate(LocalDate.of(2025, 12, 31));
        assertEquals(2, goals.size());
        assertEquals("Reuben's Goal", goals.get(0).getTitle());
    }
}
