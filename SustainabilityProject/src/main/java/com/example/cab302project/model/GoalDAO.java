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

import static java.lang.Integer.parseInt;

//table is called goals
///
///     goalId PRIMARY KEY INTEGER,
///     userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
///
///     goalTitle NOT NULL TEXT,
///     catagory NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- mind, body, social, planet
///     startsAtUnixTime NOT NULL INTEGER,
///     dueUnixTime INTEGER,  -- if null does not expire
///
///     progress NOT NULL INTEGER,
///     completionThreshold NOT NULL INTEGER, -- if binary this is 1
///     completionType NOT NULL INTEGER, -- binary (yes/no)=0, progression (0->1m steps by eoy)=1 . No constraint as this might be extended
///     isComplete NOT NULL INT GENERATED ALWAYS AS (progress>=completionThreshold)

public class GoalDAO implements IGoalDAO
{

    public void addGoal(Goal goal)
    {
        try {
            Connection addGoalConnect = DatabaseConnection.getInstance();

            int newGoalUserId = goal.getUserId();
            String newGoalTitle = goal.getTitle();
            int newGoalCategory = goal.getCategory().ordinal();
            long newGoalStartTime = goal.getStartDate().atStartOfDay(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            long newGoalEndTime;
            if (goal.getDueDate() == null) {
                newGoalEndTime = 0;
            } else {
                newGoalEndTime = goal.getDueDate().atStartOfDay(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            }

            int newGoalProgress = 0;
            int newGoalCompletionThreshold = goal.getThreshold();
            int newGoalCompletionType = goal.getCompletionType().ordinal();

            PreparedStatement statement = addGoalConnect.prepareStatement("INSERT INTO goals (userId, goalTitle, catagory, startsAtUnixTime, dueUnixTime, progress, completionThreshold, completionType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, newGoalUserId);
            statement.setString(2, newGoalTitle);
            statement.setInt(3, newGoalCategory);
            statement.setLong(4, newGoalStartTime);
            if (newGoalEndTime == 0)
            {
                statement.setNull(5, Types.INTEGER);
            }
            else
            {
                statement.setLong(5, newGoalEndTime);
            }
            statement.setInt(6, newGoalProgress);
            statement.setInt(7, newGoalCompletionThreshold);
            statement.setInt(8, newGoalCompletionType);

            int resultSet = statement.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }


    };// Create
    public Goal getGoalById(int id)
    {

        try
        {

            Connection getGoalconnection = DatabaseConnection.getInstance();
            PreparedStatement getGoal = getGoalconnection.prepareStatement("SELECT * FROM goals WHERE goalId = ?");
            getGoal.setInt(1, id);
            ResultSet goalGetResults = getGoal.executeQuery();
            int goalId = goalGetResults.getInt("goalId");
            int userId = goalGetResults.getInt("userId");
            String goalTitle = goalGetResults.getString("goalTitle");
            int category = goalGetResults.getInt("catagory");
            Category enumCat = Category.values()[category];
            int startTime = goalGetResults.getInt("startsAtUnixTime");
            LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
            String dueDate = goalGetResults.getString("dueUnixTime");
            int dueDateInt;
            LocalDate localDueDate;
            if (dueDate == null)
            {
                localDueDate = null;
            }
            else
            {
                dueDateInt = Integer.parseInt(dueDate);
                localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
            }
            int progress = goalGetResults.getInt("progress");
            int completionThreshold = goalGetResults.getInt("completionThreshold");
            int completionType = goalGetResults.getInt("completionType");
            CompletionType enumComp = CompletionType.values()[completionType];
            int isComplete = goalGetResults.getInt("isComplete");
            boolean boolComplete = (isComplete != 0);

            Goal goal = new Goal(userId, goalTitle, enumCat, localStartDate, localDueDate, progress, completionThreshold, enumComp, boolComplete);
            goal.setId(id);

            return goal;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }


    }; // Read
    public void updateGoal(Goal goal)
    {

        try
        {
            Connection updateGoalConnection = DatabaseConnection.getInstance();
            int goalId = goal.getId();
            getGoalById(goalId);

            int newGoalUserId = goal.getUserId();
            String newGoalTitle = goal.getTitle();
            int newGoalCategory = goal.getCategory().ordinal();
            long newGoalStartTime = goal.getStartDate().atStartOfDay(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            long newGoalEndTime;
            if (goal.getDueDate() == null) {
                newGoalEndTime = 0;
            } else {
                newGoalEndTime = goal.getDueDate().atStartOfDay(ZoneId.of("Australia/Brisbane")).toEpochSecond();
            }

            int newGoalProgress = 0;
            int newGoalCompletionThreshold = goal.getThreshold();
            int newGoalCompletionType = goal.getCompletionType().ordinal();

            PreparedStatement statement = updateGoalConnection.prepareStatement("UPDATE goals SET userId = ?, goalTitle = ?, catagory = ?, startsAtUnixTime = ?, dueUnixTime = ?, progress = ?, completionThreshold = ?, completionType = ? WHERE goalId = ?");
            statement.setInt(1, newGoalUserId);
            statement.setString(2, newGoalTitle);
            statement.setInt(3, newGoalCategory);
            statement.setLong(4, newGoalStartTime);
            if (newGoalEndTime == 0)
            {
                statement.setNull(5, Types.INTEGER);
            }
            else
            {
                statement.setLong(5, newGoalEndTime);
            }
            statement.setInt(6, newGoalProgress);
            statement.setInt(7, newGoalCompletionThreshold);
            statement.setInt(8, newGoalCompletionType);
            statement.setInt(9, goalId);

            int resultSet = statement.executeUpdate();


        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    };// Update
    public void deleteGoal(Goal goal)
    {

        try
        {
            Connection deleteConnection = DatabaseConnection.getInstance();

            int goalId = goal.getId();
            String query = "DELETE FROM goals WHERE goalId = ?";
            PreparedStatement statement = deleteConnection.prepareStatement(query);
            statement.setInt(1, goalId);
            int results = statement.executeUpdate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }; // Delete
    public List<Goal> getAllGoals()
    {
        try
        {
            Connection getAllGoalsConnection = DatabaseConnection.getInstance();
            List<Goal> goalsList = new ArrayList<>();

            String query = "SELECT * FROM goals";
            PreparedStatement preparedStatement = getAllGoalsConnection.prepareStatement(query);
            ResultSet goalGetResults = preparedStatement.executeQuery();

            while (goalGetResults.next())
            {
                int goalId = goalGetResults.getInt("goalId");
                int userId = goalGetResults.getInt("userId");
                String goalTitle = goalGetResults.getString("goalTitle");
                int category = goalGetResults.getInt("catagory");
                Category enumCat = Category.values()[category];
                int startTime = goalGetResults.getInt("startsAtUnixTime");
                LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                String dueDate = goalGetResults.getString("dueUnixTime");
                int dueDateInt;
                LocalDate localDueDate;
                if (dueDate == null)
                {
                    localDueDate = null;
                }
                else
                {
                    dueDateInt = Integer.parseInt(dueDate);
                    localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                }
                int progress = goalGetResults.getInt("progress");
                int completionThreshold = goalGetResults.getInt("completionThreshold");
                int completionType = goalGetResults.getInt("completionType");
                CompletionType enumComp = CompletionType.values()[completionType];
                int isComplete = goalGetResults.getInt("isComplete");
                boolean boolComplete = (isComplete != 0);

                Goal goal = new Goal(userId, goalTitle, enumCat, localStartDate, localDueDate, progress, completionThreshold, enumComp, boolComplete);
                goal.setId(goalId);

                goalsList.add(goal);
            }

            return goalsList;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }; // Read
    public List<Goal> getGoalsForUser(int userId)
    {
        try
        {
            Connection userGoals = DatabaseConnection.getInstance();
            String query = "SELECT * FROM goals WHERE userId = ?";
            PreparedStatement preparedStatement = userGoals.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet goalGetResults = preparedStatement.executeQuery();

            List<Goal> userGoalsList = new ArrayList<>();

            while (goalGetResults.next())
            {
                int goalId = goalGetResults.getInt("goalId");
                String goalTitle = goalGetResults.getString("goalTitle");
                int category = goalGetResults.getInt("catagory");
                Category enumCat = Category.values()[category];
                int startTime = goalGetResults.getInt("startsAtUnixTime");
                LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                String dueDate = goalGetResults.getString("dueUnixTime");
                int dueDateInt;
                LocalDate localDueDate;
                if (dueDate == null)
                {
                    localDueDate = null;
                }
                else
                {
                    dueDateInt = Integer.parseInt(dueDate);
                    localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                }
                int progress = goalGetResults.getInt("progress");
                int completionThreshold = goalGetResults.getInt("completionThreshold");
                int completionType = goalGetResults.getInt("completionType");
                CompletionType enumComp = CompletionType.values()[completionType];
                int isComplete = goalGetResults.getInt("isComplete");
                boolean boolComplete = (isComplete != 0);

                Goal goal = new Goal(userId, goalTitle, enumCat, localStartDate, localDueDate, progress, completionThreshold, enumComp, boolComplete);
                goal.setId(goalId);

                userGoalsList.add(goal);
            }

            return userGoalsList;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    };
    public List<Goal> getCompletedGoalsForUser(int userId)
    {
        try
        {
            Connection userGoals = DatabaseConnection.getInstance();
            String query = "SELECT * FROM goals WHERE userId = ? AND isComplete = 1";
            PreparedStatement preparedStatement = userGoals.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet goalGetResults = preparedStatement.executeQuery();

            List<Goal> userGoalsList = new ArrayList<>();

            while (goalGetResults.next())
            {
                int goalId = goalGetResults.getInt("goalId");
                String goalTitle = goalGetResults.getString("goalTitle");
                int category = goalGetResults.getInt("catagory");
                Category enumCat = Category.values()[category];
                int startTime = goalGetResults.getInt("startsAtUnixTime");
                LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                String dueDate = goalGetResults.getString("dueUnixTime");
                int dueDateInt;
                LocalDate localDueDate;
                if (dueDate == null)
                {
                    localDueDate = null;
                }
                else
                {
                    dueDateInt = Integer.parseInt(dueDate);
                    localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                }
                int progress = goalGetResults.getInt("progress");
                int completionThreshold = goalGetResults.getInt("completionThreshold");
                int completionType = goalGetResults.getInt("completionType");
                CompletionType enumComp = CompletionType.values()[completionType];
                int isComplete = goalGetResults.getInt("isComplete");
                boolean boolComplete = (isComplete != 0);

                Goal goal = new Goal(userId, goalTitle, enumCat, localStartDate, localDueDate, progress, completionThreshold, enumComp, boolComplete);
                goal.setId(goalId);

                userGoalsList.add(goal);
            }

            return userGoalsList;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    };
    public List<Goal> getIncompletedGoalsForUser(int userId)
    {
        try
        {
            Connection userGoals = DatabaseConnection.getInstance();
            String query = "SELECT * FROM goals WHERE userId = ? AND isComplete = 0";
            PreparedStatement preparedStatement = userGoals.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet goalGetResults = preparedStatement.executeQuery();

            List<Goal> userGoalsList = new ArrayList<>();

            while (goalGetResults.next())
            {
                int goalId = goalGetResults.getInt("goalId");
                String goalTitle = goalGetResults.getString("goalTitle");
                int category = goalGetResults.getInt("catagory");
                Category enumCat = Category.values()[category];
                int startTime = goalGetResults.getInt("startsAtUnixTime");
                LocalDate localStartDate = Instant.ofEpochSecond(startTime).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                String dueDate = goalGetResults.getString("dueUnixTime");
                int dueDateInt;
                LocalDate localDueDate;
                if (dueDate == null)
                {
                    localDueDate = null;
                }
                else
                {
                    dueDateInt = Integer.parseInt(dueDate);
                    localDueDate = Instant.ofEpochSecond(dueDateInt).atZone(ZoneId.of("Australia/Brisbane")).toLocalDate();
                }
                int progress = goalGetResults.getInt("progress");
                int completionThreshold = goalGetResults.getInt("completionThreshold");
                int completionType = goalGetResults.getInt("completionType");
                CompletionType enumComp = CompletionType.values()[completionType];
                int isComplete = goalGetResults.getInt("isComplete");
                boolean boolComplete = (isComplete != 0);

                Goal goal = new Goal(userId, goalTitle, enumCat, localStartDate, localDueDate, progress, completionThreshold, enumComp, boolComplete);
                goal.setId(goalId);

                userGoalsList.add(goal);
            }

            return userGoalsList;

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    };

    // Add GetIncompleteGoalsForUser

}
