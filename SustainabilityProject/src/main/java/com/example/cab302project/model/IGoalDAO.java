package com.example.cab302project.model;

import java.util.List;

// Handle CRUD operations for the Goal class with db
public interface IGoalDAO {

    /**
     * Adds a goal to the database.
     * @param goal the goal to add
     */
    public void addGoal(Goal goal); // Create

    /**
     * Updates a goal in the database.
     * @param goal the goal to update
     */
    public void updateGoal(Goal goal); // Update

    /**
     * Deletes a goal from the database.
     * @param goal the goal to delete
     */
    public void deleteGoal(Goal goal); // Delete

    /**
     * Retrieves a goal by its ID.
     * @param id the ID of the goal to retrieve
     * @return the goal if found, null if not
     */
    public Goal getGoalById(int id); // Read

    /**
     * Retrieves all goals in the database.
     * @return a list of all goals
     */
    public List<Goal> getAllGoals(); // Read

    /**
     * Retrieves all goals associated with a user by their ID.
     * @param userId the ID of the user
     * @return a list of goals associated with the user
     */
    public List<Goal> getGoalsForUser(int userId);

    /**
     * Retrieves all incomplete goals associated with a user by their ID.
     * @param userId the ID of the user
     * @return a list of incomplete goals associated with the user
     */
    public List<Goal> getIncompletedGoalsForUser(int userId);

    /**
     * Retrieves all completed goals associated with a user by their ID.
     * @param userId the ID of the user
     * @return a list of completed goals associated with the user
     */
    public List<Goal> getCompletedGoalsForUser(int userId);
}
