import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ProgressService;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryProgressTest {
    private final ProgressService service = new ProgressService();
    private final LocalDate day = LocalDate.of(2026, 10, 5);

    @Test
    void countsEachActivityOnceRegardlessOfXpAndIncludesAllThreeCategories() {
        Map<Category, Integer> counts = service.getCategoryCompletions(List.of(
                completedActivity(Category.MIND, 1, day.atTime(9, 0), 10),
                completedActivity(Category.MIND, 1, day.atTime(10, 0), 100),
                completedActivity(Category.BODY, 1, day.atTime(11, 0), 20),
                completedActivity(Category.SOCIAL, 1, day.atTime(12, 0), 30),
                completedActivity(Category.WORLD, 1, day.atTime(13, 0), 40)
        ), 1, day, day);

        assertEquals(Map.of(Category.MIND, 2, Category.BODY, 1,
                Category.SOCIAL, 1), counts);
    }

    @Test
    void usesInclusiveDatesAndExcludesOtherUsersAndUndatedCompletions() {
        LocalDate end = day.plusDays(2);
        Activity undated = completedActivity(Category.SOCIAL, 1,
                day.atTime(12, 0), 20);
        undated.setCompletedAt(null);

        Map<Category, Integer> counts = service.getCategoryCompletions(List.of(
                completedActivity(Category.MIND, 1, day.atStartOfDay(), 20),
                completedActivity(Category.BODY, 1, end.atTime(23, 59, 59), 20),
                completedActivity(Category.SOCIAL, 1,
                        day.minusDays(1).atTime(23, 59, 59), 20),
                completedActivity(Category.SOCIAL, 1,
                        end.plusDays(1).atStartOfDay(), 20),
                completedActivity(Category.SOCIAL, 2, day.atTime(12, 0), 20),
                undated
        ), 1, day, end);

        assertEquals(Map.of(Category.MIND, 1, Category.BODY, 1,
                Category.SOCIAL, 0), counts);
    }

    @Test
    void emptyPeriodRetainsZeroTotals() {
        assertEquals(Map.of(Category.MIND, 0, Category.BODY, 0,
                Category.SOCIAL, 0),
                service.getCategoryCompletions(List.of(), 1, day, day));
    }

    @Test
    void rejectsReversedDates() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getCategoryCompletions(List.of(), 1, day, day.minusDays(1)));
    }

    private Activity completedActivity(Category category, int userId,
                                       LocalDateTime completedAt, int xp) {
        Activity activity = new Activity(null, null, userId, "Test activity",
                category, TaskType.values()[0], completedAt.minusDays(1),
                null, 0, 1, xp, 0, false);
        activity.setProgress(1, completedAt);
        return activity;
    }
}
