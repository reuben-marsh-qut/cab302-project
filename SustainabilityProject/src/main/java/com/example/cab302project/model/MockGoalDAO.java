package com.example.cab302project.model;

import java.util.ArrayList;
import java.util.List;


public class MockGoalDAO implements IGoalDAO {
    private final List<Goal> goals = new ArrayList<>();
    private int autoIncrementedId = 1;

    @Override
    public void addGoal(Goal goal) {
        goal.setGoalId(autoIncrementedId);
        autoIncrementedId++;
        goals.add(goal);
    }

    @Override
    public void updateGoal(Goal goal) {
        for (int i = 0; i < goals.size(); i++) {
            if (goals.get(i).getGoalId() == goal.getGoalId()) {
                goals.set(i, goal);
                return;
            }
        }
    }

    @Override
    public void deleteGoal(Goal goal) {
        goals.remove(goal);
    }

    @Override
    public Goal getGoal(int goalId) {
        for (Goal goal : goals) {
            if (goal.getGoalId() == goalId) {
                return goal;
            }
        }
        return null;
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