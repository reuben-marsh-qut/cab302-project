package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.util.List;

public class GoalManager {
    private IGoalDAO goalDAO;

    public GoalManager(IGoalDAO goalDAO) {
        this.goalDAO = goalDAO;
    }

    public List<Goal> searchGoalsByTitle(String query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalTitleMatched(goal, query))
                .toList();
    }

    private boolean isGoalTitleMatched(Goal goal, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = goal.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    public List<Goal> searchGoalsByCategory(Category query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalCategoryMatched(goal, query))
                .toList();
    }

    private boolean isGoalCategoryMatched(Goal goal, Category query) {
        if (query == null) return true;
        Category searchCategory = goal.getCategory();
        return (searchCategory == query);
    }

    public List<Goal> searchGoalsByUserId(Integer query) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> isGoalUserIdMatched(goal, query))
                .toList();
    }

    private boolean isGoalUserIdMatched(Goal goal, Integer query) {
        if (query == null) return true;
        Integer searchUserId = goal.getUserId();
        return (searchUserId.equals(query));
    }

    public List<Goal> getGoalsBeforeDate(LocalDate date) {
        return goalDAO.getAllGoals()
                .stream()
                .filter(goal -> goal.getDueDate().isBefore(date))
                .toList();
    }
    public void deleteAllGoals(){
        for(Goal goal : goalDAO.getAllGoals()){
            goalDAO.deleteGoal(goal);
        }
    }

    public void addGoal(Goal goal) {
        goalDAO.addGoal(goal);
    }
}
