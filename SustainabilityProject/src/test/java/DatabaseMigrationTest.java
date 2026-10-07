import com.example.cab302project.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests migration of existing activity data to support completion timestamps.
 *
 * <p>Each test uses an isolated SQLite database with a minimal legacy tasks
 * table that does not yet contain the completion timestamp column.</p>
 */
public class DatabaseMigrationTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE tasks (
                        taskId INTEGER PRIMARY KEY,
                        taskTitle TEXT NOT NULL,
                        progress INTEGER NOT NULL,
                        completionThreshold INTEGER NOT NULL
                    ) STRICT
                    """);

            statement.executeUpdate("""
                    INSERT INTO tasks (
                        taskId,
                        taskTitle,
                        progress,
                        completionThreshold
                    )
                    VALUES (1, 'Existing completed activity', 5, 5)
                    """);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void migrationPreservesExistingActivityWithoutInventingCompletionTime()
            throws SQLException {

        // Act: upgrade a database created before timestamps were supported.
        DatabaseConnection.migrate(connection);

        // Assert: the new column exists and the original data is preserved.
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("""
                     SELECT taskId,
                            taskTitle,
                            progress,
                            completionThreshold,
                            completedAtUnixTime
                     FROM tasks
                     """)) {

            assertTrue(result.next(), "Existing activity should remain");

            assertEquals(1, result.getInt("taskId"));
            assertEquals(
                    "Existing completed activity",
                    result.getString("taskTitle")
            );
            assertEquals(5, result.getInt("progress"));
            assertEquals(5, result.getInt("completionThreshold"));

            assertNull(
                    result.getObject("completedAtUnixTime"),
                    "An unknown historical completion time must remain null"
            );

            assertFalse(result.next(), "Migration should not duplicate rows");
        }
    }

    @Test
    void repeatedMigrationPreservesAnExistingCompletionTimestamp()
            throws SQLException {

        // Arrange: migrate once and store a known completion timestamp.
        DatabaseConnection.migrate(connection);

        long completedAtUnixTime = 1_700_000_000L;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    UPDATE tasks
                    SET completedAtUnixTime = 1700000000
                    WHERE taskId = 1
                    """);
        }

        // Act: simulate migration running again on a later application start.
        DatabaseConnection.migrate(connection);

        // Assert: migration succeeds and preserves the timestamp.
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("""
                     SELECT completedAtUnixTime
                     FROM tasks
                     WHERE taskId = 1
                     """)) {

            assertTrue(result.next(), "Existing activity should remain");
            assertEquals(
                    completedAtUnixTime,
                    result.getLong("completedAtUnixTime"),
                    "Repeated migration must preserve the stored timestamp"
            );
            assertFalse(
                    result.wasNull(),
                    "Repeated migration must not clear the timestamp"
            );
        }
    }
}