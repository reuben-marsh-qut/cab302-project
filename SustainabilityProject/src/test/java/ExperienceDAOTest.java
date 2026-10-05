import com.example.cab302project.model.ExperienceDAO;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ExperienceDAOTest {

    @Test
    void awardingXpPersistsUserTotalAndActivityReward() throws Exception {
        // Locate the schema when running from the module or repository root.
        Path schemaPath = Path.of(
                "src", "main", "resources", "database", "createDB.sql"
        );

        if (!Files.isRegularFile(schemaPath)) {
            schemaPath = Path.of("SustainabilityProject").resolve(schemaPath);
        }

        assertTrue(
                Files.isRegularFile(schemaPath),
                "Cannot find database schema. Checked: "
                        + schemaPath.toAbsolutePath()
        );

        String schema = Files.readString(schemaPath);

        // Create an isolated database; do not use the application's database.
        try (Connection connection =
                     DriverManager.getConnection("jdbc:sqlite::memory:")) {

            // Arrange: create the application's tables.
            try (Statement statement = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    if (!sql.isBlank()) {
                        statement.execute(sql);
                    }
                }
            }

            // Arrange: user has 100 XP.
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        INSERT INTO users
                            (userId, email, passwordHash,
                             userExperience, postcode)
                        VALUES
                            (1, 'test@example.com', 'unused-test-hash',
                             100, 4000)
                        """);

                // Completed activity belongs to user 1 and is worth 20 XP.
                statement.executeUpdate("""
                        INSERT INTO tasks
                            (taskId, userId, taskTitle, catagory,
                             taskType, startsAtUnixTime, progress,
                             completionThreshold, baseXpReward,
                             awardedXpReward, doesContributeDirectlyToGoal)
                        VALUES
                            (1, 1, 'Take a walk', 0,
                             0, 0, 1,
                             1, 20,
                             0, 0)
                        """);
            }

            // Act.
            ExperienceDAO dao = new ExperienceDAO(connection);
            dao.awardXp(1, 1);

            // Assert: verify the stored user total.
            try (Statement statement = connection.createStatement()) {
                try (ResultSet result = statement.executeQuery(
                        "SELECT userExperience FROM users WHERE userId = 1")) {

                    assertTrue(result.next(), "User should exist");
                    assertEquals(120, result.getInt("userExperience"));
                }

                // Assert: verify the stored activity reward.
                try (ResultSet result = statement.executeQuery(
                        "SELECT awardedXpReward FROM tasks WHERE taskId = 1")) {

                    assertTrue(result.next(), "Activity should exist");
                    assertEquals(20, result.getInt("awardedXpReward"));
                }
            }
        }
    }
}