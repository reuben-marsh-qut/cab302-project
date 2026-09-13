package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.util.List;

public class GoalManager {
    private IGoalDAO goalDAO;

    public GoalManager(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    /**
     * Searches for goals with a matching title
     * @param query title of the goal to find
     * @return goals with matching title
     */
    public List<Goal> searchGoalsByTitle(String query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalTitleMatched(goal, query))
                .toList();
    }

    /**
     * Checks if the goal title matches the query
     * @param goal goal to compare title for
     * @param query title to compare against
     * @return true if the goal title matches the query, false otherwise
     */
    private boolean isGoalTitleMatched(Goal goal, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = goal.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    /**
     * Searches for goals with a matching category
     * @param query category to find goals of
     * @return goals with matching category
     */
    public List<Goal> searchGoalsByCategory(Category query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalCategoryMatched(goal, query))
                .toList();
    }

    /**
     * Checks if the goal category matches the query
     * @param goal goal to compare category for
     * @param query category to compare against
     * @return true if the goal category matches the query, false otherwise
     */
    private boolean isGoalCategoryMatched(Goal goal, Category query) {
        if (query == null) return true;
        Category searchCategory = goal.getCategory();
        return (searchCategory == query);
    }

    /**
     * Searches for goals with a matching user id
     * @param query user id to find goals of
     * @return goals with matching user id
     */
    public List<Goal> searchGoalsByUserId(Integer query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalUserIdMatched(goal, query))
                .toList();
    }

    /**
     * Checks if the goal user id matches the query
     * @param goal goal to compare user id for
     * @param query user id to compare against
     * @return true if the goal user id matches the query, false otherwise
     */
    private boolean isGoalUserIdMatched(Goal goal, Integer query) {
        if (query == null) return true;
        Integer searchUserId = goal.getUserId();
        return (searchUserId.equals(query));
    }

    /**
     * Searches for goals with a due date before the specified date
     * @param date date to find goals before
     * @return goals with due date before the specified date
     */
    public List<Goal> getGoalsBeforeDate(LocalDate date) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> goal.getDueDate().isBefore(date))
                .toList();
    }

    /**
     * Deletes all goals from the goalDAO
     */
    public void deleteAllGoals(){
        for(Goal goal : goalDAO.getAllGoals()){
            goalDAO.deleteGoal(goal);
        }
    }

    /**
     * Adds a goal to the goalDAO
     * @param goal the goal to add
     */
    public void addGoal(Goal goal) {
        goalDAO.addGoal(goal);
    }
}
