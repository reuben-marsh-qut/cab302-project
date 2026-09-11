package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;
import com.example.cab302project.model.enums.CompletionType;


///tasks
///     taskId INTEGER PRIMARY KEY,
///     goalId INTEGER REFERENCES goals(goalId),
///     habitId INTEGER REFERENCES habits(habitId),
///     userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
///     taskTitle TEXT NOT NULL,
///     catagory INTEGER NOT NULL CHECK (catagory IN (0, 1, 2, 3)),
///     taskType INTEGER NOT NULL,
///     startsAtUnixTime INTEGER NOT NULL,
///     dueUnixTime INTEGER,
///     progress INTEGER NOT NULL,
///     completionThreshold INTEGER NOT NULL,
///     baseXpReward INTEGER NOT NULL,
///     awardedXpReward INTEGER NOT NULL,
///     doesContributeDirectlyToGoal INTEGER NOT NULL CHECK (doesContributeDirectlyToGoal IN (0, 1))


/// PreparedStatement getGoal = getGoalconnection.prepareStatement("SELECT * FROM goals WHERE goalId = ?");
///             getGoal.setInt(1, id);
///             ResultSet goalGetResults = getGoal.executeQuery();
///             int goalId = goalGetResults.getInt("goalId");
///             int userId = goalGetResults.getInt("userId");
///             String goalTitle = goalGetResults.getString("goalTitle");
///             int category = goalGetResults.getInt("catagory");
///             Category enumCat = Category.values()[category];
///             int startTime = goalGetResults.getInt("startsAtUnixTime");
///             LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
///             String dueDate = goalGetResults.getString("dueUnixTime");
///             int dueDateInt;
///             LocalDate localDueDate;
///             if (dueDate == null)
///             {
///                 localDueDate = null;
///             }
///             else
///             {
///
///                 dueDateInt = Integer.parseInt(dueDate);
///                 localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
///             }
///             int progress = goalGetResults.getInt("progress");
///             int completionThreshold = goalGetResults.getInt("completionThreshold");
///             int completionType = goalGetResults.getInt("completionType");
///             CompletionType enumComp = CompletionType.values()[completionType];
///             int isComplete = goalGetResults.getInt("isComplete");
///             boolean boolComplete = (isComplete != 0);



public class ActivityDAO
{
    private Activity activityFromDatabaseRequest(ResultSet results)
    {

        try
        {
            int taskId = results.getInt("taskId");
            int goalId = results.getInt("goalId");
            int habitId = results.getInt("habitId");
            int userId = results.getInt("userId");
            String taskTitle = results.getString("taskTitle");
            int category = results.getInt("category");
            Category enumCategory = Category.values()[category];
            int type = results.getInt("taskType");
            CompletionType enumTaskType = CompletionType.values()[type];
            int startTime = results.getInt("startsAtUnixTime");
            LocalDateTime localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDateTime();
            String dueDate = results.getString("dueUnixTime");
            int dueDateInt;
            LocalDateTime localDueDate;
            if (dueDate == null)
            {
                localDueDate = null;
            }
            else
            {
                dueDateInt = Integer.parseInt(dueDate);
                localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDateTime();
            }
            int progress = results.getInt("progress");
            int completionThreshold = results.getInt("completionThreshold");
            int xpReward = results.getInt("baseXpReward");
            int awardedXpReward = results.getInt("rewardedXpReward");
            int contributeToGoal = results.getInt("doesContributeDirectlyToGoal");
            boolean boolContribute = (contributeToGoal != 0);

            Activity activity = new Activity(goalId, habitId, userId, taskTitle, enumCategory, enumTaskType, localStartDate, localDueDate, progress, completionThreshold, xpReward, awardedXpReward, boolContribute);
            activity.setId(taskId);

            return activity;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }


    }

    private PreparedStatement queryFromActivity(Activity activity, String query, Connection connection)
    {

        try
        {
            int taskId = activity.getId();
            int goalId = activity.getGoalId();
            int habitId = activity.getHabitId();
            int userId = activity.getUserId();
            String taskTitle = activity.getTitle();
            int category = activity.getCategory().ordinal();
            int taskType = activity.getActivityType().ordinal();
            long activityStartTime = activity.getStartDateTime().atZone(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            long activityEndTime;
            if (activity.getDueDateTime() == null) {
                activityEndTime = 0;
            }
            else
            {
                activityEndTime = activity.getDueDateTime().atZone(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            }
            int progress= activity.getProgress();
            int completionThreshold = activity.getCompletionThreshold();
            int xpReward = activity.getBaseXpReward();
            int awardedXpReward = activity.getAwardedXpReward();
            int contributeToGoal = activity.isDoesContributeDirectlyToGoal() ? 1 : 0;

            PreparedStatement statement = connection.prepareStatement(query);
            //statement.setInt(1, taskId);
            statement.setInt(1, goalId);
            statement.setInt(2, habitId);
            statement.setInt(3, userId);
            statement.setString(4, taskTitle);
            statement.setInt(5, category);
            statement.setInt(6, taskType);
            statement.setLong(7, activityStartTime);
            if (activityEndTime == 0)
            {
                statement.setNull(8, Types.INTEGER);
            }
            else
            {
                statement.setLong(8, activityEndTime);
            }
            statement.setInt(9, progress);
            statement.setInt(10, completionThreshold);
            statement.setInt(11, xpReward);
            statement.setInt(12, awardedXpReward);
            statement.setInt(13, contributeToGoal);
            if (query.contains("UPDATE"))
            {
                statement.setInt(14, taskId);
            }

            return statement;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    public Activity getActivityById(int id)
    {
        try
        {

            Connection connection = DatabaseConnection.getInstance();

            String query = "SELECT * FROM tasks WHERE userId = ?";
            PreparedStatement request = connection.prepareStatement(query);
            request.setInt(1, id);
            ResultSet results = request.executeQuery();

            Activity activityById = activityFromDatabaseRequest(results);
            return activityById;

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Activity> getAllActivities()
    {
        try
        {
            Connection connection = DatabaseConnection.getInstance();

            String query = "SELECT * FROM tasks";
            PreparedStatement request = connection.prepareStatement(query);
            ResultSet results = request.executeQuery();
            List<Activity> activityList = new ArrayList<>();

            while (results.next())
            {
                Activity tempAct = activityFromDatabaseRequest(results);
                activityList.add(tempAct);
            }

            return activityList;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void deleteActivity(Activity activity)
    {

        try
        {
            Connection connection = DatabaseConnection.getInstance();

            String command = "DELETE FROM tasks WHERE taskId = ?";
            PreparedStatement deleteTask = connection.prepareStatement(command);
            int activityId = activity.getId();
            deleteTask.setInt(1, activityId);
            int results = deleteTask.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void createActivity(Activity activity)
    {
        try
        {

            Connection connection = DatabaseConnection.getInstance();

            String query = "INSERT INTO tasks (goalId, habitId, userId, taskTitle, catagory, taskType, startsAtUnixTime, dueUnixTime, progress, completionThreshold, baseXpReward, awardedXpReward, doesContributeDirectlyToGoal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = queryFromActivity(activity, query, connection);
            int results = statement.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void updateActivity(Activity activity)
    {

        try
        {
            Connection connection = DatabaseConnection.getInstance();
            String query = "UPDATE tasks SET goalId = ?, habitId = ?, userId = ?, taskTitle = ?, catagory = ?, taskType = ?, startsAtUnixTime = ?, dueUnixTime = ?, progress = ?, completionThreshold = ?, baseXpReward = ?, awardedXpReward = ?, doesContributeDirectlyToGoal = ?  WHERE taskId = ?";
            PreparedStatement statement = queryFromActivity(activity, query, connection);
            int result = statement.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }



}
