package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.util.List;

public class HabitManager {
    private IHabitDAO habitDAO;

    public HabitManager(IHabitDAO habitDAO) {
        this.habitDAO = habitDAO;
    }

    /**
     * Searches for habits whose title contains a given query
     * @param query the search query
     * @return List of all matching habits
     */
    public List<Habit> searchHabitsByTitle(String query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitTitleMatched(habit, query))
                .toList();
    }

    /**
     * Checks if a given habit's title matches the query
     * @param habit the habit to inspect
     * @param query the target search title
     * @return true if the habit's title matches the query
     */
    private boolean isHabitTitleMatched(Habit habit, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = habit.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    /**
     * Searches for habits whose category matches a given query
     * @param query the target search category
     * @return List of all matching habits
     */
    public List<Habit> searchHabitsByCategory(Category query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitCategoryMatched(habit, query))
                .toList();
    }

    /**
     * Checks if a given habit's category matches the query
     * @param habit the habit to inspect
     * @param query the target search category
     * @return true if the habit's category matches the query
     */
    private boolean isHabitCategoryMatched(Habit habit, Category query) {
        if (query == null) return true;
        Category searchCategory = habit.getCategory();
        return (searchCategory == query);
    }

    /**
     * Searches for habits whose user ID matches a given query
     * @param query the target search user ID
     * @return List of all matching habits
     */
    public List<Habit> searchHabitsByUserId(Integer query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitUserIdMatched(habit, query))
                .toList();
    }

    /**
     * Checks if a given habit's user ID matches the query
     * @param habit the habit to inspect
     * @param query the target search user ID
     * @return true if the habit's user ID matches the query
     */
    private boolean isHabitUserIdMatched(Habit habit, Integer query) {
        if (query == null) return true;
        Integer searchUserId = habit.getUserId();
        return (searchUserId.equals(query));
    }

    /**
     * Searches for habits whose due date is before a given date
     * @param date the target search date
     * @return List of all matching habits
     */
    public List<Habit> getHabitsBeforeDate(LocalDate date) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> habit.getDueDateTime().isBefore(date))
                .toList();
    }

    /**
     * Adds a habit to the database
     * @param habit the habit to add
     */
    public void createHabit(Habit habit) {
        habitDAO.createHabit(habit);
    }
}
