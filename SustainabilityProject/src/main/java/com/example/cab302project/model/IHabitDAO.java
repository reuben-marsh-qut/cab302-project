package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDate;
import java.util.List;

public interface IHabitDAO {
    public boolean addHabit(Habit habit); // Create
    public boolean updateHabit(Habit habit); // Update
    public void deleteHabit(Habit habit); // Delete
    public Habit getHabitById(int id); // Read
    public List<Habit> getAllHabits(); // Read
   public int getHabitCompletionStreak(Habit habit);
   public Activity getCurrentAssociatedTask(Habit habit);
   public List<Activity> getAllAssociatedTasks(Habit habit);
   public boolean createHabit (Goal goal, User user, String title, Category category,
                               TaskType taskType, int completionThreshold, int baseXpReward,
                               int repeatFrequency, RepeatFrequencyType repeatFrequencyType,
                               LocalDate startDate, LocalDate endDate, boolean doesContributeDirectlyToGoal);
//   public boolean createHabit (User user, String title,
//                               Category category, CompletionType completionType, int completionThreshold,
//                               int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType,
//                               LocalDate endDate);

}
