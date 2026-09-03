package com.example.cab302project.model;

import java.util.List;


public interface IGoalDAO {

    /** Saves a new goal and assigns it an id. */
    void addGoal(Goal goal);

    /** Saves changes to an existing goal. */
    void updateGoal(Goal goal);

    /** Removes a goal. */
    void deleteGoal(Goal goal);

    /** Returns the goal with the given id, or null if there isn't one. */
    Goal getGoal(int goalId);

    /** Returns every goal belonging to the given user. */
    List<Goal> getGoalsForUser(int userId);
}