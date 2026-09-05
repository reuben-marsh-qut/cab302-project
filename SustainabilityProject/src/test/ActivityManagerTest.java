import com.example.cab302project.model.*;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityManagerTest {
    private ActivityManager activityManager;

    private Activity[] activities = {
            new Activity(1, 1, 1, "Mow the lawn", Category.BODY,
                    CompletionType.BINARY, LocalDateTime.of(2027, 9, 4, 12, 0),
                    LocalDateTime.of(2027, 9, 11, 12, 0),
                    0, 1, 10, 0),
            new Activity(1, 2, 1, "Water the garden", Category.BODY,
                    CompletionType.BINARY, LocalDateTime.of(2027, 9, 4, 12, 0),
                    LocalDateTime.of(2027, 9, 11, 12, 0),
                    0, 1, 5, 0),
            new Activity(2, 3, 2, "Meditate for 30 minutes", Category.MIND,
                    CompletionType.BINARY, LocalDateTime.of(2025, 6, 4, 12, 0),
                    LocalDateTime.of(2027, 6, 11, 12, 0),
                    0, 1, 10, 0),
            new Activity(3, 4, 3, "Mow the lawn", Category.BODY,
                    CompletionType.BINARY, LocalDateTime.of(2025, 9, 4, 12, 0),
                    LocalDateTime.of(2026, 12, 11, 12, 0),
                    0, 1, 10, 0),
            new Activity(4, 5, 3, "Talk to five people", Category.SOCIAL,
                    CompletionType.PROGRESSIVE, LocalDateTime.of(2029, 8, 4, 12, 0),
                    LocalDateTime.of(2029, 8, 11, 12, 0),
                    0, 1, 10, 0)
    };

    @BeforeEach
    public void setUp() {
        activityManager = new ActivityManager(new MockActivityDAO());
    }

    @Test
    public void testSearchByTitleInOneActivity() {
        activityManager.addActivity(activities[0]); // add just the first activity
        List<Activity> activities = activityManager.searchActivitiesByTitle("Mow the lawn"); // search for activity based on firstname
        assertEquals(1, activities.size()); // assertion - correct quantity
        assertEquals(this.activities[0], activities.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByCategoryInOneActivity() {
        activityManager.addActivity(activities[0]); // add just the first activity
        List<Activity> activities = activityManager.searchActivitiesByCategory(Category.BODY); // search for activity based on firstname
        assertEquals(1, activities.size()); // assertion - correct quantity
        assertEquals(this.activities[0], activities.get(0)); // assertion - correct item
    }

    @Test
    public void testSearchByTitleInMultipleActivities() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle("Meditate for 30 minutes"); // search for activity based on firstname
        assertEquals(1, activities.size()); // assertion - correct quantity
        for (Activity activity : activities) { // assertion
            assertTrue(activity.getTitle().equals("Meditate for 30 minutes"));
        }
    }

    @Test
    public void testSearchByCategoryInMultipleActivities() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByCategory(Category.BODY); // search for activity based on firstname
        assertEquals(3, activities.size()); // assertion - correct quantity
        for (Activity activity : activities) { // assertion
            assertTrue(activity.getCategory().equals(Category.BODY));
        }
    }

    @Test
    public void testSearchNoResults() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle("Drink 2L of water");
        assertEquals(0, activities.size());
    }

    @Test
    public void testSearchEmptyQuery() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle("");
        assertEquals(5, activities.size());
    }

    @Test
    public void testSearchNullQuery() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle(null);
        assertEquals(5, activities.size());
    }

    @Test
    public void testSearchByTitleCaseInsensitive() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle("water the garden");
        assertEquals(1, activities.size());
        for (Activity activity : activities) {
            assertTrue(activity.getTitle().equalsIgnoreCase("Water the garden"));
        }
    }

    @Test
    public void testSearchPartialQuery() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByTitle("the");
        assertEquals(3, activities.size());
        assertTrue(activities.get(0).getTitle().equals("Mow the lawn"));
        assertTrue(activities.get(1).getTitle().equals("Water the garden"));
        assertTrue(activities.get(2).getTitle().equals("Mow the lawn"));
    }

    @Test
    public void testSearchEmptyActivities() {
        List<Activity> activities = activityManager.searchActivitiesByTitle("Mow the lawn");
        assertEquals(0, activities.size());
    }

    @Test
    public void testSearchByUserId() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List<Activity> activities = activityManager.searchActivitiesByUserId(2);
        assertEquals(1, activities.size());
        assertEquals("Meditate for 30 minutes", activities.get(0).getTitle());
    }

    @Test
    public void testSearchByDate() {
        for (Activity activity : activities) { // add all activities
            activityManager.addActivity(activity);
        }
        List <Activity> activities = activityManager.getActivitiesBeforeDate(LocalDateTime.of(2026, 12, 31, 0, 0));
        assertEquals(1, activities.size());
        assertEquals("Mow the lawn", activities.get(0).getTitle());
    }
}
