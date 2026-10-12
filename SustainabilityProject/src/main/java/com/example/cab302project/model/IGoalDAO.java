package com.example.cab302project.model;

import java.util.List;

// Handle CRUD operations for the Goal class with db
public interface IGoalDAO {

    public void addGoal(Goal goal); // Create
    public void updateGoal(Goal goal); // Update
    public void deleteGoal(Goal goal); // Delete
    public Goal getGoalById(int id); // Read
    public List<Goal> getAllGoals(); // Read
    public List<Goal> getGoalsForUser(int userId);
    public List<Goal> getIncompletedGoalsForUser(int userId);
    public List<Goal> getCompletedGoalsForUser(int userId);
}
