import com.example.cab302project.model.Activity;
import com.example.cab302project.model.ActivityDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests activity persistence using an isolated SQLite database.
 */
public class ActivityPersistenceTest {

    private Connection connection;
    private ActivityDAO activityDAO;

    /**
     * Creates the application schema and an incomplete test activity.
     *
     * @throws Exception if database setup fails
     */
    @BeforeEach
    void setUp() throws Exception {
        Path schemaPath = Path.of(
                "src", "main", "resources", "database", "createDB.sql"
        );

        if (!Files.isRegularFile(schemaPath)) {
            schemaPath = Path.of("SustainabilityProject").resolve(schemaPath);
        }

        assertTrue(
                Files.isRegularFile(schemaPath),
                "Cannot find database schema: " + schemaPath.toAbsolutePath()
        );

        String schema = Files.readString(schemaPath);

        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        try (Statement statement = connection.createStatement()) {
            for (String sql : schema.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql);
                }
            }

            statement.executeUpdate("""
                    INSERT INTO users (
                        userId, email, passwordHash, userExperience, postcode
                    )
                    VALUES (
                        1, 'test@example.com', 'unused-test-hash', 0, 4000
                    )
                    """);

            statement.executeUpdate("""
                    INSERT INTO tasks (
                        taskId, userId, taskTitle, catagory, taskType,
                        startsAtUnixTime, progress, completionThreshold,
                        baseXpReward, awardedXpReward,
                        doesContributeDirectlyToGoal
                    )
                    VALUES (
                        1, 1, 'Take five walks', 0, 0,
                        0, 0, 5,
                        100, 0, 0
                    )
                    """);
        }

        activityDAO = new ActivityDAO(connection);
    }

    /**
     * Closes the isolated database after each test.
     *
     * @throws SQLException if the connection cannot be closed
     */
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Verifies that saving and reloading an activity preserves
     * its recorded completion time.
     */
    @Test
    void completionTimeSurvivesSavingAndReloading() {
        Activity activity = activityDAO.getActivityById(1);
        assertNotNull(activity);

        LocalDateTime completedAt =
                LocalDateTime.of(2026, 10, 5, 15, 0);

        activity.setProgress(5, completedAt);

        // Confirm the model recorded the time before saving.
        assertEquals(completedAt, activity.getCompletedAt());

        activityDAO.updateActivity(activity);

        // Reload through a different DAO using the same test database.
        ActivityDAO anotherDAO = new ActivityDAO(connection);
        Activity reloaded = anotherDAO.getActivityById(1);

        assertNotNull(reloaded);
        assertNotSame(activity, reloaded);
        assertTrue(reloaded.isComplete());
        assertEquals(completedAt, reloaded.getCompletedAt());
    }
}