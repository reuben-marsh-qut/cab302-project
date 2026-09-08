import com.example.cab302project.model.Habit;
import com.example.cab302project.model.HabitManager;
import com.example.cab302project.model.MockHabitDAO;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HabitManagerTest {
    private HabitManager habitManager;

    private Habit[] habits = {
            new Habit(1, 1, "Mow the lawn weekly", Category.BODY,
                    CompletionType.BINARY, 7, LocalDate.now(),
                    LocalDate.now().plusDays(30), 21, 31, 41,
                    21, false),
            new Habit(1, 1, "Water the garden twice a week", Category.BODY,
                    CompletionType.BINARY, 3, LocalDate.now(),
                    LocalDate.now().plusDays(30), 21, 31, 41,
                    21, false),
            new Habit(2, 2, "Read 5 pages a day", Category.MIND,
                    CompletionType.BINARY, 1, LocalDate.now().plusDays(30),
                    LocalDate.now().plusDays(100), 21, 31, 41,
                    21, false),
            new Habit(3, 2, "Walk 30 minutes a day", Category.BODY,
                    CompletionType.BINARY, 1, LocalDate.now().plusDays(30),
                    LocalDate.now().plusDays(60), 21, 31, 41,
                    21, false),
            new Habit(4, 3, "Eat three meals a day", Category.BODY,
                    CompletionType.BINARY, 1, LocalDate.now().plusMonths(2),
                    LocalDate.now().plusMonths(3), 21, 31, 41,
                    21, false)
    };

    @BeforeEach
    public void setUp() {habitManager = new HabitManager(new MockHabitDAO());}

    @Test
    public void testSearchByTitleInOneHabit() {
        habitManager.addHabit(habits[0]); // add just the first habit
        List<Habit> habits = habitManager.searchHabitsByTitle("Mow the lawn weekly"); // search for habit based on firstname
        assertEquals(1, habits.size()); // assertion - correct quantity
        assertEquals(this.habits[0], habits.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByCategoryInOneHabit() {
        habitManager.addHabit(habits[0]); // add just the first habit
        List<Habit> habits = habitManager.searchHabitsByCategory(Category.BODY); // search for habit based on firstname
        assertEquals(1, habits.size()); // assertion - correct quantity
        assertEquals(this.habits[0], habits.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByTitleInMultipleHabits() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle("Read 5 pages a day"); // search for habit based on firstname
        assertEquals(1, habits.size()); // assertion - correct quantity
        for (Habit habit : habits) { // assertion
            assertTrue(habit.getTitle().equals("Read 5 pages a day"));
        }
    }

    @Test
    public void testSearchByCategoryInMultipleHabits() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByCategory(Category.BODY); // search for habit based on firstname
        assertEquals(4, habits.size()); // assertion - correct quantity
        for (Habit habit : habits) { // assertion
            assertTrue(habit.getCategory().equals(Category.BODY));
        }
    }

    @Test
    public void testSearchNoResults() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle("Joseph's Habit");
        assertEquals(0, habits.size());
    }

    @Test
    public void testSearchEmptyQuery() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle("");
        assertEquals(5, habits.size());
    }

    @Test
    public void testSearchNullQuery() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle(null);
        assertEquals(5, habits.size());
    }

    @Test
    public void testSearchByTitleCaseInsensitive() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle("eat three meals a day");
        assertEquals(1, habits.size());
        for (Habit habit : habits) {
            assertTrue(habit.getTitle().equalsIgnoreCase("Eat three meals a day"));
        }
    }

    @Test
    public void testSearchPartialQuery() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByTitle("the");
        assertEquals(2, habits.size());
        assertTrue(habits.get(0).getTitle().equals("Mow the lawn weekly"));
        assertTrue(habits.get(1).getTitle().equals("Water the garden twice a week"));
    }

    @Test
    public void testSearchEmptyHabits() {
        List<Habit> habits = habitManager.searchHabitsByTitle("Water the garden twice a week");
        assertEquals(0, habits.size());
    }

    @Test
    public void testSearchByUserId() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List<Habit> habits = habitManager.searchHabitsByUserId(2);
        assertEquals(2, habits.size());
        assertEquals("Read 5 pages a day", habits.get(0).getTitle());
    }

    @Test
    public void testSearchByDate() {
        for (Habit habit : habits) { // add all habits
            habitManager.addHabit(habit);
        }
        List <Habit> habits = habitManager.getHabitsBeforeDate(LocalDate.now().plusDays(50));
        assertEquals(2, habits.size());
        assertEquals("Mow the lawn weekly", habits.get(0).getTitle());
    }
}
