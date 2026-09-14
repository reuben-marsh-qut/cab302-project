package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class HabitDAO implements IHabitDAO{
    @Override
    public boolean addHabit(Habit habit) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement insertInto = conn.prepareStatement("INSERT INTO habits (goalId , userId , baseXpReward , habitTitle, taskType, catagory, repeatFrequencyType, repeatFrequency, startsAtUnixTime, dueUnixTime, completionThreshold, doesContributeDirectlyToGoal) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            insertInto.setInt(1,habit.getGoalId());
            insertInto.setInt(2,habit.getUserId());
            insertInto.setInt(3,habit.getBaseXpReward());
            insertInto.setString(4,habit.getTitle());
            insertInto.setInt(5, habit.getHabitType().ordinal());
            insertInto.setInt(6, habit.getCategory().ordinal());
            insertInto.setInt(7, habit.getRepeatFrequencyType().ordinal());
            insertInto.setInt(8, habit.getRepeatFrequency());
            ZoneId timezone = ZoneId.systemDefault();
            insertInto.setInt(9, habit.getStartDateTime().atStartOfDay(timezone).getSecond());
            insertInto.setInt(10, habit.getDueDateTime().atStartOfDay(timezone).getSecond());
            insertInto.setInt(11, habit.getCompletionThreshold());
            insertInto.setInt(12, habit.getDoesContributeDirectlyToGoal() ? 1:0);

            int rowsAffected = insertInto.executeUpdate();
            System.out.println(rowsAffected);
            if (rowsAffected!=1){
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("UNIQUE")){
                return false;
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateHabit(Habit habit) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement getHabit = conn.prepareStatement("SELECT * FROM habits WHERE habitId=?");
            getHabit.setInt(1, habit.getId());
            ResultSet doesOtherExist = getHabit.executeQuery();
            if (!doesOtherExist.next()) {
                return false;
            }
            if (doesOtherExist.getInt("habitId")!=habit.getId()){
                return false;
            }
            PreparedStatement update = conn.prepareStatement("UPDATE habits SET goalId=?, userId=?, baseXpReward=?, habitTitle=?, taskType=?, catagory=?, repeatFrequencyType=?, repeatFrequency=?, startsAtUnixTime=?, dueUnixTime=?, completionThreshold=?, doesContributeDirectlyToGoal=? WHERE habitId=?");

            update.setInt(1,habit.getGoalId());
            update.setInt(2,habit.getUserId());
            update.setInt(3,habit.getBaseXpReward());
            update.setString(4,habit.getTitle());
            update.setInt(5, habit.getHabitType().ordinal());
            update.setInt(6, habit.getCategory().ordinal());
            update.setInt(7, habit.getRepeatFrequencyType().ordinal());
            update.setInt(8, habit.getRepeatFrequency());
            ZoneId timezone = ZoneId.systemDefault();
            update.setInt(9, habit.getStartDateTime().atStartOfDay(timezone).getSecond());
            update.setInt(10, habit.getDueDateTime().atStartOfDay(timezone).getSecond());
            update.setInt(11, habit.getCompletionThreshold());
            update.setInt(12, habit.getDoesContributeDirectlyToGoal() ? 1:0);
            update.setInt(13,habit.getId());
            if (update.executeUpdate()!=1){
                throw new RuntimeException("update did not work"); // should just return null instead idk?
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteHabit(Habit habit) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM habits WHERE habitId=?");
            deleteUser.setInt(1,habit.getId());
            deleteUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Habit getHabitById(int id) {
        Connection conn = DatabaseConnection.getInstance();

        try {
            PreparedStatement getHabit = conn.prepareStatement("SELECT * FROM habits WHERE habitId=?");
            getHabit.setInt(1,id);
            ResultSet habitSet = getHabit.executeQuery();
            while (habitSet.next()){
                return habitFromResultSet(habitSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Habit> getAllHabits() {
        Connection conn = DatabaseConnection.getInstance();
        List<Habit> habits = new ArrayList<Habit>();

        try {
            PreparedStatement getHabits = conn.prepareStatement("SELECT * FROM habits");
            ResultSet habitSet = getHabits.executeQuery();
            while (habitSet.next()){
                habits.add(habitFromResultSet(habitSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return habits;
    }

    
    @Override
    public int getHabitCompletionStreak(Habit habit) {
        return -1;
    }

    @Override
    public Activity getCurrentAssociatedTask(Habit habit) {
        return null;
    }

    @Override
    public List<Activity> getAllAssociatedTasks(Habit habit) {
        return List.of();
    }

    private Habit habitFromResultSet(ResultSet res) throws SQLException {
        ZoneId timezone = ZoneId.systemDefault();
        return new Habit(
                res.getInt("habitId"),
                res.getInt("goalId"),
                res.getInt("userId"),
                res.getString("habitTitle"),
                Category.values()[res.getInt("catagory")],
                TaskType.values()[res.getInt("taskType")],
                res.getInt("repeatFrequency"),
                RepeatFrequencyType.values()[res.getInt("repeatFrequencyType")],
                Instant.ofEpochSecond(res.getInt("startsAtUnixTime")).atZone(timezone).toLocalDate(),
                Instant.ofEpochSecond(res.getInt("endsAtUnixTime")).atZone(timezone).toLocalDate(),
                res.getInt("completionThreshold"),
                res.getInt("baseXpReward"),
                res.getBoolean("doesContributeDirectlyToGoal")
            );
    }

    @Override
    public boolean createHabit(Goal goal, User user, String title, Category category, TaskType taskType, int completionThreshold, int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType,LocalDate startDate, LocalDate endDate, boolean doesContributeDirectlyToGoal) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement insertInto = conn.prepareStatement("INSERT INTO habits (goalId , userId , baseXpReward , habitTitle, taskType, catagory, repeatFrequencyType, repeatFrequency, startsAtUnixTime, dueUnixTime, completionThreshold, doesContributeDirectlyToGoal) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            insertInto.setInt(1,goal.getId());
            insertInto.setInt(2,user.getUserId());
            insertInto.setInt(3,baseXpReward);
            insertInto.setString(4,title);
            insertInto.setInt(5, taskType.ordinal());
            insertInto.setInt(6, category.ordinal());
            insertInto.setInt(7, repeatFrequencyType.ordinal());
            insertInto.setInt(8, repeatFrequency);
            ZoneId timezone = ZoneId.systemDefault();
            insertInto.setInt(9, startDate.atStartOfDay(timezone).getSecond());
            insertInto.setInt(10, endDate.atStartOfDay(timezone).getSecond());
            insertInto.setInt(11, completionThreshold);
            insertInto.setInt(12, doesContributeDirectlyToGoal ? 1:0);

            int rowsAffected = insertInto.executeUpdate();
            System.out.println(rowsAffected);
            if (rowsAffected!=1){
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("UNIQUE")){
                return false;
            }
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public boolean createHabit(User user, String title, Category category, CompletionType completionType, int completionThreshold, int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType, LocalDate endDate) {
//        return createHabit(null, user,title,category,completionType,completionThreshold,baseXpReward,repeatFrequency,repeatFrequencyType,endDate);
//    }
}
