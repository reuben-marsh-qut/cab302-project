package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stores and retrieves activities from the tasks table.
 *
 * <p>The table stores activity ownership, optional goal and habit links,
 * title, category, task type, start and due times, completion time,
 * progress, completion threshold, XP rewards and goal-contribution
 * status.</p>
 *
 * <p>Use the default constructor for the application database or supply
 * a connection for an isolated test database. This DAO does not close
 * the connection.</p>
 */
public class ActivityDAO implements IActivityDAO {

    private final Connection suppliedConnection;

    /**
     * Creates a DAO that uses the shared application connection.
     *
     * <p>The connection is obtained only when a database operation runs.</p>
     */
    public ActivityDAO() {
        suppliedConnection = null;
    }

    /**
     * Creates a DAO using a supplied connection.
     *
     * @param connection the database connection
     * @throws NullPointerException if the connection is null
     */
    public ActivityDAO(Connection connection) {
        suppliedConnection = Objects.requireNonNull(connection);
    }

    /**
     * Returns the supplied connection or the application connection.
     *
     * @return the connection used for database operations
     */
    private Connection getConnection() {
        return suppliedConnection != null
                ? suppliedConnection
                : DatabaseConnection.getInstance();
    }

    /**
     * Converts the current result row into an activity.
     *
     * <p>For compatibility with existing callers, absent goal and habit
     * IDs are represented as zero. Stored timestamps have whole-second
     * precision and are converted into the system's local time.</p>
     *
     * @param results the result set positioned at an activity row
     * @return the activity represented by that row
     */
    protected Activity activityFromDatabaseRequest(ResultSet results) {
        try {
            Activity activity = new Activity(
                    results.getInt("goalId"),
                    results.getInt("habitId"),
                    results.getInt("userId"),
                    results.getString("taskTitle"),
                    Category.values()[results.getInt("catagory")],
                    TaskType.values()[results.getInt("taskType")],
                    readDateTime(results, "startsAtUnixTime"),
                    readDateTime(results, "dueUnixTime"),
                    results.getInt("progress"),
                    results.getInt("completionThreshold"),
                    results.getInt("baseXpReward"),
                    results.getInt("awardedXpReward"),
                    results.getBoolean("doesContributeDirectlyToGoal")
            );

            activity.setId(results.getInt("taskId"));
            activity.setCompletedAt(
                    readDateTime(results, "completedAtUnixTime")
            );

            return activity;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not read activity.", exception);
        }
    }

    /**
     * Reads a nullable Unix timestamp as a local date and time.
     *
     * @param results the current result row
     * @param column the timestamp column
     * @return the local date and time, or null for SQL NULL
     * @throws SQLException if the column cannot be read
     */
    private LocalDateTime readDateTime(
            ResultSet results,
            String column
    ) throws SQLException {
        long seconds = results.getLong(column);

        if (results.wasNull()) {
            return null;
        }

        return Instant.ofEpochSecond(seconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Binds the activity fields shared by insert and update statements.
     *
     * @param statement the statement to populate
     * @param activity the activity whose values are written
     * @throws SQLException if parameter binding fails
     */
    private void bindActivity(
            PreparedStatement statement,
            Activity activity
    ) throws SQLException {
        setOptionalId(statement, 1, activity.getGoalId());
        setOptionalId(statement, 2, activity.getHabitId());

        statement.setInt(3, activity.getUserId());
        statement.setString(4, activity.getTitle());
        statement.setInt(5, activity.getCategory().ordinal());
        statement.setInt(6, activity.getActivityType().ordinal());

        setDateTime(statement, 7, activity.getStartDateTime());
        setDateTime(statement, 8, activity.getDueDateTime());

        statement.setInt(9, activity.getProgress());
        statement.setInt(10, activity.getCompletionThreshold());
        statement.setInt(11, activity.getBaseXpReward());
        statement.setInt(12, activity.getAwardedXpReward());
        statement.setInt(
                13,
                activity.isDoesContributeDirectlyToGoal() ? 1 : 0
        );

        setDateTime(statement, 14, activity.getCompletedAt());
    }

    /**
     * Binds a nullable date and time as Unix seconds.
     *
     * @param statement the statement to populate
     * @param index the parameter index
     * @param value the date and time, or null
     * @throws SQLException if parameter binding fails
     */
    private void setDateTime(
            PreparedStatement statement,
            int index,
            LocalDateTime value
    ) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setLong(
                    index,
                    value.atZone(ZoneId.systemDefault()).toEpochSecond()
            );
        }
    }

    /**
     * Binds an optional foreign key, treating zero as absent.
     *
     * @param statement the statement to populate
     * @param index the parameter index
     * @param id the optional ID
     * @throws SQLException if parameter binding fails
     */
    private void setOptionalId(
            PreparedStatement statement,
            int index,
            Integer id
    ) throws SQLException {
        if (id == null || id == 0) {
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setInt(index, id);
        }
    }

    /**
     * Retrieves an activity by its database ID.
     *
     * @param id the activity ID
     * @return the activity, or null if it does not exist
     */
    @Override
    public Activity getActivityById(int id) {
        String sql = "SELECT * FROM tasks WHERE taskId = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet results = statement.executeQuery()) {
                return results.next()
                        ? activityFromDatabaseRequest(results)
                        : null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load activity.", exception);
        }
    }

    /**
     * Retrieves all activities.
     *
     * @return the stored activities
     */
    @Override
    public List<Activity> getAllActivities() {
        return queryActivities("SELECT * FROM tasks", null);
    }

    /**
     * Deletes an activity.
     *
     * @param activity the activity to delete
     */
    @Override
    public void deleteActivity(Activity activity) {
        String sql = "DELETE FROM tasks WHERE taskId = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            statement.setInt(1, activity.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not delete activity.", exception);
        }
    }

    /**
     * Inserts an activity, including its optional completion time.
     *
     * @param activity the activity to insert
     */
    @Override
    public void addActivity(Activity activity) {
        String sql = """
                INSERT INTO tasks (
                    goalId, habitId, userId, taskTitle, catagory,
                    taskType, startsAtUnixTime, dueUnixTime,
                    progress, completionThreshold, baseXpReward,
                    awardedXpReward, doesContributeDirectlyToGoal,
                    completedAtUnixTime
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            bindActivity(statement, activity);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not create activity.", exception);
        }
    }

    /**
     * Saves an activity's state and completion time in one update.
     *
     * @param activity the activity to update
     */
    @Override
    public void updateActivity(Activity activity) {
        String sql = """
                UPDATE tasks
                SET goalId = ?,
                    habitId = ?,
                    userId = ?,
                    taskTitle = ?,
                    catagory = ?,
                    taskType = ?,
                    startsAtUnixTime = ?,
                    dueUnixTime = ?,
                    progress = ?,
                    completionThreshold = ?,
                    baseXpReward = ?,
                    awardedXpReward = ?,
                    doesContributeDirectlyToGoal = ?,
                    completedAtUnixTime = ?
                WHERE taskId = ?
                """;

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            bindActivity(statement, activity);
            statement.setInt(15, activity.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not update activity.", exception);
        }
    }

    /**
     * Retrieves a user's completed activities.
     *
     * @param userId the owning user's ID
     * @return activities whose progress meets the completion threshold
     */
    @Override
    public List<Activity> getCompletedActivitiesForUser(int userId) {
        return queryActivities(
                """
                SELECT * FROM tasks
                WHERE userId = ? AND progress >= completionThreshold
                """,
                userId
        );
    }

    /**
     * Retrieves a user's incomplete activities.
     *
     * @param userId the owning user's ID
     * @return activities whose progress is below the completion threshold
     */
    @Override
    public List<Activity> getIncompletedActivitiesForUser(int userId) {
        return queryActivities(
                """
                SELECT * FROM tasks
                WHERE userId = ? AND progress < completionThreshold
                """,
                userId
        );
    }

    /**
     * Runs an activity query with an optional user ID parameter.
     *
     * @param sql the query to run
     * @param userId the user filter, or null for an unfiltered query
     * @return the matching activities
     */
    private List<Activity> queryActivities(String sql, Integer userId) {
        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            if (userId != null) {
                statement.setInt(1, userId);
            }

            List<Activity> activities = new ArrayList<>();

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    activities.add(activityFromDatabaseRequest(results));
                }
            }

            return activities;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load activities.", exception);
        }
    }
}