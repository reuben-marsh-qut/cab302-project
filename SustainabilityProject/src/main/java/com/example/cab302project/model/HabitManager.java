package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.util.List;

public class HabitManager {
    private IHabitDAO habitDAO;

    public HabitManager(IHabitDAO habitDAO) {
        this.habitDAO = habitDAO;
    }

    public List<Habit> searchHabitsByTitle(String query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitTitleMatched(habit, query))
                .toList();
    }

    private boolean isHabitTitleMatched(Habit habit, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = habit.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    public List<Habit> searchHabitsByCategory(Category query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitCategoryMatched(habit, query))
                .toList();
    }

    private boolean isHabitCategoryMatched(Habit habit, Category query) {
        if (query == null) return true;
        Category searchCategory = habit.getCategory();
        return (searchCategory == query);
    }

    public List<Habit> searchHabitsByUserId(Integer query) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> isHabitUserIdMatched(habit, query))
                .toList();
    }

    private boolean isHabitUserIdMatched(Habit habit, Integer query) {
        if (query == null) return true;
        Integer searchUserId = habit.getUserId();
        return (searchUserId.equals(query));
    }

    public List<Habit> getHabitsBeforeDate(LocalDate date) {
        return habitDAO.getAllHabits()
                .stream()
                .filter(habit -> habit.getDueDateTime().isBefore(date))
                .toList();
    }

    public void addHabit(Habit habit) {
        habitDAO.addHabit(habit);
    }
}
