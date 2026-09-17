package com.example.cab302project;

import com.example.cab302project.model.*;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class databasetesting {
    static void run(){
        Connection connection = DatabaseConnection.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT sqlite_version()");
            statement.execute();
            var rs = statement.getResultSet();
            System.out.println(rs.getString(1));



            UserDAO dao = new UserDAO();
//            dao.deleteAllUsers();
            dao.createUser("anne@anne.com", "chickens",4000);
//            dao.createUser("bob@anne.com", "squares",4200);
//            dao.createUser("jerryob@anne.com", "circles",4200);

            User anne = dao.loginUser("anne@anne.com", "chickens");
//            System.out.println(anne.getUserId());
//            System.out.println(dao.getUserByEmail("bob@anne.com").toString());
//            System.out.println(dao.getUserById(anne.getUserId()).toString());
//            System.out.println(dao.getAllUsers());
//            dao.deleteUser(anne);
//            System.out.println(dao.getAllUsers());
            GoalDAO goalDAO = new GoalDAO();
            goalDAO.addGoal(new Goal(anne.getUserId(),"Get fit", Category.BODY, LocalDate.now(), LocalDate.now().plusYears(1),0,1, CompletionType.BINARY,false));
            Goal jail = goalDAO.getIncompletedGoalsForUser(anne.getUserId()).getFirst();
            HabitDAO habitDAO = new HabitDAO();
            LocalDate start = LocalDate.of(2026,9,1);
            LocalDate habitEnd = LocalDate.of(2026,9,17);
            habitDAO.createHabit(jail, anne, "Walk 10,000 steps", Category.BODY, TaskType.PROGRESSIVE,10000,10,1, RepeatFrequencyType.DAILY,start,habitEnd,false);
            Habit habit = habitDAO.getHabitsByUserId(anne.getUserId()).getFirst();
            habitDAO.debugCreateAssociatedTasks(habit,10000,start.plusDays(1),start.plusDays(2));
            habitDAO.debugCreateAssociatedTasks(habit,10000,start.plusDays(2),start.plusDays(3));
            habitDAO.debugCreateAssociatedTasks(habit,0,start.plusDays(3),start.plusDays(4));
            habitDAO.debugCreateAssociatedTasks(habit,10000,start.plusDays(4),start.plusDays(5));
            List<Activity> tasks = habitDAO.getAllAssociatedTasks(habit);
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(tasks.get(i).getStartDateTime() + " " + tasks.get(i).getDueDateTime());
            }
            habitDAO.debugCreateAssociatedTasks(habit,10000,start.plusDays(5),start.plusDays(6));
            System.out.println(habitDAO.getHabitCompletionStreak(habit));
            //            ActivityDAO activityDAO = new ActivityDAO();
//            System.out.println(activityDAO.getActivityById(habitDAO.getAllAssociatedTasks()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
