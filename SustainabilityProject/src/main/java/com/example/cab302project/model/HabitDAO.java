package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class HabitDAO implements IHabitDAO{
    @Override
    public void addHabit(Habit habit) {

    }

    @Override
    public void updateHabit(Habit habit) {

    }

    @Override
    public void deleteHabit(Habit habit) {

    }

    @Override
    public Habit getHabitById(int id) {
        return null;
    }

    @Override
    public List<Habit> getAllHabits() {
        return List.of();
    }

    @Override
    public int getHabitCompletionStreak(Habit habit) {
        return 0;
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
