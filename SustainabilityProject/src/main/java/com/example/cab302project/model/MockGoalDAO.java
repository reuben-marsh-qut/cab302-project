package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.CompletionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockGoalDAO implements IGoalDAO {
    public final ArrayList<Goal> goals = new ArrayList<>();
    private int autoIncrementedId = 0;

    @Override
    public void addGoal(Goal goal) {
        goal.setId(autoIncrementedId);
        autoIncrementedId++;
        goals.add(goal);
    }

    @Override
    public void updateGoal(Goal goal) {
        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i).getId() == goal.getId()) {
                goals.set(i, goal);
                break;
            }
        }
    }

    @Override
    public void deleteGoal(Goal goal) {
        goals.remove(goal);
    }

    @Override
    public Goal getGoalById(int id) {
        for (Goal goal : goals) {
            if (goal.getId() == id) {
                return goal;
            }
        }
        return null;
    }

    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    @Override
    public List<Goal> getGoalsForUser(int userId) {
        List<Goal> results = new ArrayList<>();
        for (Goal goal : goals) {
            if (goal.getUserId() == userId) {
                results.add(goal);
            }
        }
        return results;
    }
}
