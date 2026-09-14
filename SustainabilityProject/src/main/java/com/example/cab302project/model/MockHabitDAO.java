package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockHabitDAO implements IHabitDAO {
    public final ArrayList<Habit> habits = new ArrayList<>();
    private int autoIncrementedId = 0;

    @Override
    public boolean addHabit(Habit activity) {
        activity.setId(autoIncrementedId);
        autoIncrementedId++;
        habits.add(activity);
        return true;
    }

    @Override
    public boolean updateHabit(Habit activity) {
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getId() == activity.getId()) {
                habits.set(i, activity);
                break;
            }
        }
        return false;
    }

    @Override
    public void deleteHabit(Habit activity) {
        habits.remove(activity);
    }

    @Override
    public Habit getHabitById(int id) {
        for (Habit activity : habits) {
            if (activity.getId() == id) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public List<Habit> getAllHabits() {
        return new ArrayList<>(habits);
    }

    @Override
    public int getHabitCompletionStreak(Habit habit) {
        return -1; // TODO: fix me
    }

    @Override
    public Activity getCurrentAssociatedTask(Habit habit) {
        return null; // TODO: fix me
    }

    @Override
    public List<Activity> getAllAssociatedTasks(Habit habit) {
        return List.of(); // TODO: fix me
    }

    @Override
    public boolean createHabit(Goal goal, User user, String title, Category category, TaskType taskType, int completionThreshold, int baseXpReward, int repeatFrequency, RepeatFrequencyType repeatFrequencyType, LocalDate startDate, LocalDate endDate, boolean doesContributeDirectlyToGoal) {
        return false;
    }
}
