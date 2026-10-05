import com.example.cab302project.model.ExperienceDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ExperienceDAOTest {

    private Connection connection;
    private ExperienceDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        // Locate the schema from the module or repository root.
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

        // Each test receives its own isolated database.
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        try (Statement statement = connection.createStatement()) {
            for (String sql : schema.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql);
                }
            }

            // User starts with 100 XP.
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

        dao = new ExperienceDAO(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void awardingXpPersistsUserTotalAndActivityReward() throws SQLException {
        // Act.
        dao.awardXp(1, 1);

        // Assert.
        assertStoredXp(120, 20);
    }

    @Test
    void failedUserXpUpdateRollsBackActivityAward() throws SQLException {
        // Arrange: allow the activity update, but reject the user XP update.
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TEMP TRIGGER reject_user_xp_update
                    BEFORE UPDATE OF userExperience ON users
                    BEGIN
                        SELECT RAISE(ABORT, 'Simulated XP save failure');
                    END;
                    """);
        }

        // Act: attempt the award and capture the expected failure.
        SQLException exception = assertThrows(
                SQLException.class,
                () -> dao.awardXp(1, 1)
        );

        assertTrue(
                exception.getMessage().contains("Simulated XP save failure"),
                "The failure should come from our test trigger"
        );

        // Assert: both stored values remain unchanged.
        assertStoredXp(100, 0);

        assertTrue(
                connection.getAutoCommit(),
                "Auto-commit should be restored after rollback"
        );
    }

    @Test
    void previouslySavedActivityDoesNotAwardXpAgain() throws SQLException {
        // Arrange: award and save the first reward.
        dao.awardXp(1, 1);
        assertStoredXp(120, 20);

        // Act: a new DAO reads the saved activity and attempts another award.
        ExperienceDAO anotherDao = new ExperienceDAO(connection);
        anotherDao.awardXp(1, 1);

        // Assert: the user receives no additional XP.
        assertStoredXp(120, 20);
    }

    private void assertStoredXp(int expectedUserXp, int expectedActivityXp)
            throws SQLException {

        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(
                    "SELECT userExperience FROM users WHERE userId = 1")) {

                assertTrue(result.next(), "User should exist");
                assertEquals(
                        expectedUserXp,
                        result.getInt("userExperience"),
                        "Stored user XP should match"
                );
            }

            try (ResultSet result = statement.executeQuery(
                    "SELECT awardedXpReward FROM tasks WHERE taskId = 1")) {

                assertTrue(result.next(), "Activity should exist");
                assertEquals(
                        expectedActivityXp,
                        result.getInt("awardedXpReward"),
                        "Stored activity reward should match"
                );
            }
        }
    }
}