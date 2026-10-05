import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ExperienceService;
import com.example.cab302project.model.User;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExperienceServiceTest {

    @Test
    void completedActivityAddsRewardToExistingUserXp() {
        // Arrange: a user with 100 XP.
        User user = new User(
                1, "test@example.com", "unused-test-hash", 100, 4000
        );

        // Progress = 1, threshold = 1: the activity is complete.
        // Reward = 20 XP, previously awarded = 0 XP.
        Activity activity = new Activity(
                null, null, 1, "Take a walk",
                Category.BODY,
                TaskType.BINARY,
                LocalDateTime.of(2026, 10, 5, 9, 0),
                null,
                1, 1, 20, 0, false
        );

        ExperienceService service = new ExperienceService();

        // Act.
        service.awardXp(user, activity);

        // Assert.
        assertEquals(120, user.getUserExperience());
    }
    @Test
    void incompleteActivityDoesNotAwardXp() {
        // Arrange: user starts with 100 XP.
        User user = new User(
                1, "test@example.com", "unused-test-hash", 100, 4000
        );

        // Progress = 0, threshold = 1: activity is incomplete.
        Activity activity = new Activity(
                null, null, 1, "Take a walk",
                Category.BODY,
                TaskType.BINARY,
                LocalDateTime.of(2026, 10, 5, 9, 0),
                null,
                0, 1, 20, 0, false
        );

        ExperienceService service = new ExperienceService();

        // Act.
        service.awardXp(user, activity);

        // Assert: XP must remain unchanged.
        assertEquals(100, user.getUserExperience());
    }
    @Test
    void completedActivityDoesNotAwardXpTwice() {
        // Arrange.
        User user = new User(
                1, "test@example.com", "unused-test-hash", 100, 4000
        );

        Activity activity = new Activity(
                null, null, 1, "Take a walk",
                Category.BODY,
                TaskType.BINARY,
                LocalDateTime.of(2026, 10, 5, 9, 0),
                null,
                1, 1, 20, 0, false
        );

        ExperienceService service = new ExperienceService();

        // Act: attempt to award the same activity twice.
        service.awardXp(user, activity);
        service.awardXp(user, activity);

        // Assert: only one reward was added.
        assertEquals(120, user.getUserExperience());
    }
    @Test
    void activityDoesNotAwardXpToAnotherUser() {
        // Arrange: this user has ID 2.
        User user = new User(
                2, "other@example.com", "unused-test-hash", 100, 4000
        );

        // The completed activity belongs to user ID 1.
        Activity activity = new Activity(
                null, null, 1, "Take a walk",
                Category.BODY,
                TaskType.BINARY,
                LocalDateTime.of(2026, 10, 5, 9, 0),
                null,
                1, 1, 20, 0, false
        );

        ExperienceService service = new ExperienceService();

        // Act.
        service.awardXp(user, activity);

        // Assert: neither the user nor the award record changes.
        assertEquals(100, user.getUserExperience());
        assertEquals(0, activity.getAwardedXpReward().intValue());
    }
}