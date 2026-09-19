package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;
import javafx.concurrent.Task;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
//            System.out.println(rowsAffected);
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
            if (!habitSet.next()) {
                return null;
            }
            return habitFromResultSet(habitSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Habit> getHabitsByUserId(int id) {
        Connection conn = DatabaseConnection.getInstance();
        List<Habit> habits = new ArrayList<Habit>();

        try {
            // TODO: document that this returns tasks oldest to youngest in docstring
            PreparedStatement getHabits = conn.prepareStatement("SELECT * FROM habits WHERE userId=? ORDER BY habitId ASC");
            getHabits.setInt(1,id);
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
    public List<Habit> getAllHabits() {
        Connection conn = DatabaseConnection.getInstance();
        List<Habit> habits = new ArrayList<Habit>();

        try {
            // TODO: document that this returns tasks oldest to youngest in docstring kinda
            PreparedStatement getHabits = conn.prepareStatement("SELECT * FROM habits ORDER BY habitId ASC");
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
        List<Activity> tasks = getAllAssociatedTasks(habit);
        // get all tasks
        // what is the interval?
        // assume complete tasks align with the repeat interval
        // if stretch broken return zero else
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
        Connection conn = DatabaseConnection.getInstance();
        List<Activity> tasks = new ArrayList<Activity>();
        Activity task = null;
        ActivityDAO dao = new ActivityDAO();
        ZoneId timezone = ZoneId.systemDefault();
        try {
            // DESC grabs the latest task first
            PreparedStatement getTasks = conn.prepareStatement("SELECT * FROM tasks WHERE habitId=? AND startsAtUnixTime <= ? AND dueUnixTime >= ? ORDER BY habitId DESC");
            getTasks.setInt(1,habit.getId());
            long now = Instant.now().getEpochSecond();
            getTasks.setLong(2,now);
            getTasks.setLong(3,now);
//            getTasks.setLong(2,habit.getStartDateTime().atStartOfDay(timezone).toEpochSecond());
//            getTasks.setLong(3,habit.getDueDateTime().atStartOfDay(timezone).toEpochSecond());
            ResultSet taskSet = getTasks.executeQuery();
            while (taskSet.next()){
                tasks.add(dao.activityFromDatabaseRequest(taskSet));

            }
//            System.out.println("sz"+ tasks.size());
            task = tasks.getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e){
            return null;
        }
//        LocalDate dateOfTask = LocalDate.now();
//        List<Activity> tasks = getAllAssociatedTasks(habit);
        return task;
    }

    // TODO: document that this returns tasks oldest to youngest in docstring
    @Override
    public List<Activity> getAllAssociatedTasks(Habit habit) {
        Connection conn = DatabaseConnection.getInstance();
        List<Activity> tasks = new ArrayList<Activity>();
        ActivityDAO dao = new ActivityDAO();
        try {
            PreparedStatement getTasks = conn.prepareStatement("SELECT * FROM tasks WHERE habitId=? ORDER BY startsAtUnixTime ASC");
            getTasks.setInt(1,habit.getId());
//
            ResultSet taskSet = getTasks.executeQuery();
            while (taskSet.next()){
                tasks.add(dao.activityFromDatabaseRequest(taskSet));
//                System.out.println(tasks.getFirst().getTitle() + " eee");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tasks;
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
                Instant.ofEpochSecond(res.getInt("dueUnixTime")).atZone(timezone).toLocalDate(),
                res.getInt("completionThreshold"),
                res.getInt("baseXpReward"),
                res.getBoolean("doesContributeDirectlyToGoal")
            );
    }

    @Override
    public boolean createHabit(Goal goal, User user, String title, Category category, TaskType taskType, int completionThreshold, int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType,LocalDate startDate, LocalDate endDate, boolean doesContributeDirectlyToGoal) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement insertInto = conn.prepareStatement("INSERT INTO habits (goalId , userId , baseXpReward , habitTitle, taskType, catagory, repeatFrequencyType, repeatFrequency, startsAtUnixTime, dueUnixTime, completionThreshold, doesContributeDirectlyToGoal) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) RETURNING habitId");
            if (goal == null){
                insertInto.setNull(1, Types.INTEGER);
            } else {
                insertInto.setInt(1,goal.getId());
            }

            insertInto.setInt(2,user.getUserId());
            insertInto.setInt(3,baseXpReward);
            insertInto.setString(4,title);
            insertInto.setInt(5, taskType.ordinal());
            insertInto.setInt(6, category.ordinal());
            insertInto.setInt(7, repeatFrequencyType.ordinal());
            insertInto.setInt(8, repeatFrequency);
            ZoneId timezone = ZoneId.systemDefault();
            long startUnix = startDate.atStartOfDay(timezone).toEpochSecond();
            long endHabitUnix = endDate.atStartOfDay(timezone).toEpochSecond();

//            System.out.println(startUnix+" " +endUnix);
            insertInto.setLong(9,startUnix);
            insertInto.setLong(10, endHabitUnix);
            insertInto.setInt(11, completionThreshold);
            insertInto.setInt(12, doesContributeDirectlyToGoal ? 1:0);

            ResultSet rs = insertInto.executeQuery();
//            System.out.println(rowsAffected);
            int habitId = -1;
            while (rs.next()){
                habitId = rs.getInt("habitId");
            }

            ActivityDAO dao = new ActivityDAO();
            LocalDate endUnix;
//            int numberOfHabits;
            switch (repeatFrequencyType){
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
//            for
            dao.addActivity(new Activity((goal==null) ? 0 : goal.getId(), habitId,user.getUserId(), title,category, taskType, startDate.atStartOfDay(), endUnix.atStartOfDay().minusSeconds(1),0,completionThreshold,baseXpReward,0,doesContributeDirectlyToGoal));

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("UNIQUE")){
                return false;
            }
            throw new RuntimeException(e);
        }
    }

    public void debugCreateAssociatedTasks(Habit habit, int completionProgress, LocalDate startTime, LocalDate endTime){
        ActivityDAO dao = new ActivityDAO();
        int awardedXPReward = 0;
        if (completionProgress >= habit.getCompletionThreshold()){
            awardedXPReward = habit.getBaseXpReward();
        }
        dao.addActivity(new Activity(habit.getGoalId(), habit.getId(),habit.getUserId(), habit.getTitle(),habit.getCategory(), habit.getHabitType(), startTime.atStartOfDay(), endTime.atStartOfDay().minusSeconds(1),completionProgress,habit.getCompletionThreshold(),habit.getBaseXpReward(),awardedXPReward,habit.getDoesContributeDirectlyToGoal()));

    }

//    @Override
//    public boolean createHabit(User user, String title, Category category, CompletionType completionType, int completionThreshold, int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType, LocalDate endDate) {
//        return createHabit(null, user,title,category,completionType,completionThreshold,baseXpReward,repeatFrequency,repeatFrequencyType,endDate);
//    }
}
