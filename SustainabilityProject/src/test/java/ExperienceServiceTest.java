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
}