package com.example.cab302project.model;

import java.util.List;

public interface IHabitDAO {
    public void addHabit(Habit habit); // Create
    public void updateHabit(Habit habit); // Update
    public void deleteHabit(Habit habit); // Delete
    public Habit getHabitById(int id); // Read
    public List<Habit> getAllHabits(); // Read
}
