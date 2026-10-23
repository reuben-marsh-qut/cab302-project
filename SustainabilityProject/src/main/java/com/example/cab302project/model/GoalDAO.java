package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


//table is called goals
///
///     goalId PRIMARY KEY INTEGER,
///     userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
///
///     goalTitle NOT NULL TEXT,
///     catagory NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- mind, body, social, planet
///     startsAtUnixTime NOT NULL INTEGER,
///     dueUnixTime INTEGER,-- if null does not expire
///
///     progress NOT NULL INTEGER,
///     completionThreshold NOT NULL INTEGER, -- if binary this is 1
///     completionType NOT NULL INTEGER, -- binary (yes/no)=0, progression (0->1m steps by eoy)=1 . No constraint as this might be extended
///     isComplete NOT NULL INT GENERATED ALWAYS AS (progress>=completionThreshold)

public class GoalDAO implements IGoalDAO
{

    private final Connection suppliedConnection;

    /**
     * Creates a DAO that uses the shared application connection.
     *
     * <p>The connection is obtained only when a database operation runs.</p>
     */
    public GoalDAO() {
        suppliedConnection = null;
    }

    /**
     * Creates a DAO using a supplied connection.
     *
     * @param connection the database connection
     * @throws NullPointerException if the connection is null
     */
    public GoalDAO(Connection connection) {
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
     * Converts the current result row into an goal.
     *
     * @param results the result set positioned at an goal row
     * @return the goal represented by that row
     */
    protected Goal goalFromDatabaseRequest(ResultSet results) {
        try {
            Goal goal = new Goal(
                    results.getInt("userId"),
                    results.getString("goalTitle"),
                    Category.values()[results.getInt("catagory")],
                    readDate(results, "startsAtUnixTime"),
                    readDate(results, "dueUnixTime"),
                    results.getInt("progress"),
                    results.getInt("completionThreshold"),
                    CompletionType.values()[results.getInt("completionType")],
                    results.getBoolean("isComplete")
            );

            goal.setId(results.getInt("goalId"));
            goal.setIsPaused(results.getBoolean("isPaused"));

            return goal;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not read goal.", exception);
        }
    }

    /**
     * Reads a nullable Unix timestamp as a local date.
     *
     * @param results the current result row
     * @param column the timestamp column
     * @return the local date, or null for SQL NULL
     * @throws SQLException if the column cannot be read
     */
    private LocalDate readDate(
            ResultSet results,
            String column
    ) throws SQLException {
        long seconds = results.getLong(column);

        if (results.wasNull()) {
            return null;
        }

        return Instant.ofEpochSecond(seconds)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Binds the goal fields shared by insert and update statements.
     *
     * @param statement the statement to populate
     * @param goal the goal whose values are written
     * @throws SQLException if parameter binding fails
     */
    private void bindGoal(
            PreparedStatement statement,
            Goal goal
    ) throws SQLException {
        setOptionalId(statement, 1, goal.getUserId());
        statement.setString(2, goal.getTitle());
        statement.setInt(3, goal.getCategory().ordinal());
        setDate(statement, 4, goal.getStartDate());
        setDate(statement, 5, goal.getDueDate());
        statement.setInt(6, goal.getProgress());
        statement.setInt(7, goal.getThreshold());
        statement.setInt(8, goal.getCompletionType().ordinal());
        statement.setInt(
                9,
                goal.getIsPaused() ? 1 : 0
        );
    }

    /**
     * Binds a nullable date and time as Unix seconds.
     *
     * @param statement the statement to populate
     * @param index the parameter index
     * @param value the date and time, or null
     * @throws SQLException if parameter binding fails
     */
    private void setDate(
            PreparedStatement statement,
            int index,
            LocalDate value
    ) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setLong(
                    index,
                    value.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
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
     * Adds a goal to the database.
     * @param goal the goal to add
     */
    @Override
    public void addGoal(Goal goal)
    {
        String sql = """
                INSERT INTO goals (
                    userId, goalTitle, catagory, startsAtUnixTime,
                    dueUnixTime, progress, completionThreshold,
                    completionType, isPaused
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;


        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            bindGoal(statement, goal);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not create goal.", exception);
        }
    }

    /**
     * Retrieves a goal from the database by its ID.
     * @param id the ID of the goal to retrieve
     * @return the goal with the specified ID, or null if not found
     */
    @Override
    public Goal getGoalById(int id)
    {
        String sql = "SELECT * FROM goals WHERE goalId = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet results = statement.executeQuery()) {
                return results.next()
                        ? goalFromDatabaseRequest(results)
                        : null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load goal.", exception);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates a goal in the database.
     * @param goal the goal to update
     */
    @Override
    public void updateGoal(Goal goal)
    {
        String sql = """
                UPDATE goals
                SET userId = ?,
                    goalTitle = ?,
                    catagory = ?,
                    startsAtUnixTime = ?,
                    dueUnixTime = ?,
                    progress = ?,
                    completionThreshold = ?,
                    completionType = ?,
                    isPaused = ?
                WHERE goalId = ?
                """;

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            bindGoal(statement, goal);
            statement.setInt(10, goal.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not update goal.", exception);
        }
    }

    /**
     * Deletes a goal from the database.
     * @param goal the goal to delete
     */
    public void deleteGoal(Goal goal)
    {
        String sql = "DELETE FROM goals WHERE goalId = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            statement.setInt(1, goal.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not delete goal.", exception);
        }
    }

    /**
     * Retrieves all goals from the database.
     * @return a list of all goals
     */
    public List<Goal> getAllGoals()
    {
        return queryGoals(
                "SELECT * FROM goals",
                null
        );
    }

    /**
     * Retrieves all goals for a specific user from the database.
     * @param userId the ID of the user
     * @return a list of goals for the specified user
     */
    public List<Goal> getGoalsForUser(int userId)
    {
        return queryGoals(
                "SELECT * FROM goals WHERE userId = ?",
                userId
        );
    }

    /**
     * Retrieves all completed goals for a specific user from the database.
     * @param userId the ID of the user
     * @return a list of completed goals for the specified user
     */
    public List<Goal> getCompletedGoalsForUser(int userId)
    {
        return queryGoals(
                """
                SELECT * FROM goals
                WHERE userId = ? AND isComplete = 1
                """,
                userId
        );
    }

    /**
     * Retrieves all incomplete goals for a specific user from the database.
     * @param userId the ID of the user
     * @return a list of incomplete goals for the specified user
     */
    public List<Goal> getIncompletedGoalsForUser(int userId)
    {
        return queryGoals(
                """
                SELECT * FROM goals
                WHERE userId = ? AND isComplete = 0
                """,
                userId
        );
    }

    /**
     * Runs a goal query with an optional user ID parameter.
     *
     * @param sql the query to run
     * @param userId the user filter, or null for an unfiltered query
     * @return the matching goals
     */
    private List<Goal> queryGoals(String sql, Integer userId) {
        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            if (userId != null) {
                statement.setInt(1, userId);
            }

            List<Goal> goals = new ArrayList<>();

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    goals.add(goalFromDatabaseRequest(results));
                }
            }

            return goals;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load goals.", exception);
        }
    }
}
