package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HabitDAO implements IHabitDAO{

    private final Connection suppliedConnection;

    /**
     * Creates a DAO that uses the shared application connection.
     *
     * <p>The connection is obtained only when a database operation runs.</p>
     */
    public HabitDAO() {
        suppliedConnection = null;
    }

    /**
     * Creates a DAO using a supplied connection.
     *
     * @param connection the database connection
     * @throws NullPointerException if the connection is null
     */
    public HabitDAO(Connection connection) {
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
     * Converts the current result row into a habit.
     *
     * <p>For compatibility with existing callers, absent goal and habit
     * IDs are represented as zero. Stored timestamps have whole-second
     * precision and are converted into the system's local time.</p>
     *
     * @param results the result set positioned at a habit row
     * @return the habit represented by that row
     */
    protected Habit habitFromDatabaseRequest(ResultSet results) {
        try {
            Habit habit = new Habit(
                    results.getInt("goalId"),
                    results.getInt("userId"),
                    results.getString("habitTitle"),
                    Category.values()[results.getInt("catagory")],
                    TaskType.values()[results.getInt("taskType")],
                    results.getInt("repeatFrequency"),
                    RepeatFrequencyType.values()[results.getInt("repeatFrequencyType")],
                    readDate(results, "startsAtUnixTime"),
                    readDate(results, "dueUnixTime"),
                    results.getInt("completionThreshold"),
                    results.getInt("baseXpReward"),
                    results.getBoolean("doesContributeDirectlyToGoal")
            );

            habit.setId(results.getInt("habitId"));

            return habit;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not read habit.", exception);
        }
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
     * Binds the habit fields shared by insert and update statements.
     *
     * @param statement the statement to populate
     * @param habit the habit whose values are written
     * @throws SQLException if parameter binding fails
     */
    private void bindHabit(
            PreparedStatement statement,
            Habit habit
    ) throws SQLException {
        setOptionalId(statement, 1, habit.getGoalId());

        statement.setInt(2, habit.getUserId());
        statement.setInt(3, habit.getBaseXpReward());
        statement.setString(4, habit.getTitle());
        statement.setInt(5, habit.getCategory().ordinal());
        statement.setInt(6, habit.getHabitType().ordinal());
        statement.setInt(7, habit.getRepeatFrequencyType().ordinal());
        statement.setInt(8, habit.getRepeatFrequency());

        setDate(statement, 9, habit.getStartDateTime());
        setDate(statement, 10, habit.getDueDateTime());

        statement.setInt(11, habit.getCompletionThreshold());
        statement.setInt(
                12,
                habit.getDoesContributeDirectlyToGoal() ? 1 : 0
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



    /////////

    @Override
    public void updateHabit(Habit habit) {

        String sql = "UPDATE habits SET goalId=?, userId=?, baseXpReward=?, habitTitle=?, taskType=?, catagory=?, repeatFrequencyType=?, " +
                "repeatFrequency=?, startsAtUnixTime=?, dueUnixTime=?, completionThreshold=?, doesContributeDirectlyToGoal=? WHERE habitId=?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            bindHabit(statement, habit);
            statement.setInt(10, habit.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not update habit.", exception);
        }
    }

    @Override
    public void deleteHabit(Habit habit) {
        String sql = "DELETE FROM habits WHERE goalId = ?";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            statement.setInt(1, habit.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Could not delete habit.", exception);
        }
    }

    @Override
    public Habit getHabitById(int id) {
        List<Habit> habits = queryHabits(
                "SELECT * FROM habits WHERE habitId=?",
                id
        );

        if (habits.isEmpty()) {
            return null;
        }

        return habits.getFirst();
    }

    @Override
    public List<Habit> getHabitsByUserId(int id) {
        return queryHabits(
                "SELECT * FROM habits WHERE userId=? ORDER BY habitId ASC",
                id
        );
    }

    @Override
    public List<Habit> getAllHabits() {
        return queryHabits(
                "SELECT * FROM habits ORDER BY habitId ASC",
                null
        );
    }


    @Override
    public int getHabitCompletionStreak(Habit habit) {
        List<Activity> tasks = getAllAssociatedTasks(habit);

        int streak = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Activity task = tasks.get(i);
            if (task.isComplete() && LocalDateTime.now().isAfter(task.getStartDateTime())) {
                streak++;
            } else if(task.isComplete() && !LocalDateTime.now().isAfter(task.getStartDateTime())){
              // do nothing if task is in future
            } else {
                streak = 0;
            }
        }
        return streak;
    }

    @Override
    public Activity getCurrentAssociatedTask(Habit habit) {
        List<Activity> tasks = queryActivities(
                "SELECT * FROM tasks WHERE habitId=? AND startsAtUnixTime <= ? AND dueUnixTime >= ? ORDER BY habitId DESC",
                habit.getId(),
                habit.getStartDateTime(),
                habit.getDueDateTime()
        );

        try {
            return tasks.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    // TODO: document that this returns tasks oldest to youngest in docstring
    @Override
    public List<Activity> getAllAssociatedTasks(Habit habit) {
        return queryActivities(
                "SELECT * FROM tasks WHERE habitId=? ORDER BY startsAtUnixTime ASC",
                habit.getId()
        );
    }

    private void createActivityFromHabit(Habit habit) {
        ActivityDAO dao = new ActivityDAO();

        LocalDate endUnix;
        LocalDate startDate = habit.getStartDateTime();
        Integer repeatFrequency = habit.getRepeatFrequency();

        switch (habit.getRepeatFrequencyType()){
            case DAILY -> {
                endUnix = startDate.plusDays(repeatFrequency);
            }
            case WEEKLY -> {
                endUnix = startDate.plusDays(repeatFrequency*7);
            }
            case MONTHLY -> {
                endUnix = startDate.plusMonths(repeatFrequency);
            }
            case YEARLY -> {
                endUnix = startDate.plusYears(repeatFrequency);
            }
            default -> {
                endUnix = startDate.plusDays(repeatFrequency);
            }
        }

        try {
            dao.addActivity(new Activity((habit.getGoalId()==null) ? 0 : habit.getGoalId(), habit.getId(),habit.getUserId(),
                    habit.getTitle(),habit.getCategory(), habit.getHabitType(), startDate.atStartOfDay(),
                    endUnix.atStartOfDay().minusSeconds(1),0,habit.getCompletionThreshold(),habit.getBaseXpReward(),
                    0,habit.getDoesContributeDirectlyToGoal()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a habit to the database.
     * @param habit the habit to add
     */
    @Override
    public void createHabit(Habit habit)
    {
        String sql = "INSERT INTO habits (goalId , userId , baseXpReward , habitTitle, catagory, " +
                "taskType, repeatFrequencyType, repeatFrequency, startsAtUnixTime, " +
                "dueUnixTime, completionThreshold, doesContributeDirectlyToGoal) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {

            if (habit.getGoalId() == null){
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1,habit.getGoalId());
            }

            bindHabit(statement, habit);
            statement.executeUpdate();
            habit.setId(statement.getGeneratedKeys().getInt(1));
        } catch (SQLException exception) {
            throw new RuntimeException("Could not create goal.", exception);
        }

        // Now we have created the goal, we have to create the activity

        createActivityFromHabit(habit);
    }


    public void debugCreateAssociatedTasks(Habit habit, int completionProgress, LocalDate startTime, LocalDate endTime){
        ActivityDAO dao = new ActivityDAO();
        int awardedXPReward = 0;
        if (completionProgress >= habit.getCompletionThreshold()){
            awardedXPReward = habit.getBaseXpReward();
        }
        dao.addActivity(new Activity(habit.getGoalId(), habit.getId(),habit.getUserId(), habit.getTitle(),habit.getCategory(), habit.getHabitType(), startTime.atStartOfDay(), endTime.atStartOfDay().minusSeconds(1),completionProgress,habit.getCompletionThreshold(),habit.getBaseXpReward(),awardedXPReward,habit.getDoesContributeDirectlyToGoal()));

    }


    /**
     * Runs a habit query with an optional user ID parameter.
     *
     * @param sql the query to run
     * @param id the user filter, or null for an unfiltered query
     * @return the matching habits
     */
    private List<Habit> queryHabits(String sql, Integer id) {
        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            if (id != null) {
                statement.setInt(1, id);
            }

            List<Habit> habits = new ArrayList<>();

            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    habits.add(habitFromDatabaseRequest(results));
                }
            }

            return habits;
        } catch (SQLException exception) {
            throw new RuntimeException("Could not load habits.", exception);
        }
    }

    /**
     * Runs an activity query with an optional habit ID parameter.
     *
     * @param sql the query to run
     * @param habitId the habit filter, or null for an unfiltered query
     * @return the matching activities
     */
    private List<Activity> queryActivities(String sql, Integer habitId, LocalDate startDateTime, LocalDate dueDateTime) {
        try (PreparedStatement statement =
                     getConnection().prepareStatement(sql)) {
            if (habitId != null) {
                statement.setInt(1, habitId);
            }
            if (startDateTime != null) {
                setDate(statement, 2, startDateTime);
            }
            if (dueDateTime != null) {
                setDate(statement, 3, startDateTime);
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

    private List<Activity> queryActivities(String sql, Integer habitId) {
        return queryActivities(sql, habitId, null, null);
    }
}
