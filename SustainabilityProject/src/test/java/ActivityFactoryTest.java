import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityFactory;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActivityFactoryTest {

    @Test
    void newActivityStartsWithNoAwardedXp() {
        // Arrange.
        ActivityFactory factory = new ActivityFactory();

        // Act: use the same factory as the creation screen.
        Activity activity = factory.createActivity(
                0,
                0,
                1,
                "Take a walk",
                Category.BODY,
                TaskType.BINARY,
                1
        );

        // Assert: a new activity has not earned its reward yet.
        assertEquals(0, activity.getAwardedXpReward().intValue());
    }
}