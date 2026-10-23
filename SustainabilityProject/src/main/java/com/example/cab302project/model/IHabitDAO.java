package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDate;
import java.util.List;

public interface IHabitDAO {
    /**
     * Updates a habit in the database.
     * @param habit the habit to update
     */
    public void updateHabit(Habit habit); // Update

    /**
     * Deletes a habit from the database.
     * @param habit the habit to delete
     */
    public void deleteHabit(Habit habit); // Delete

    /**
     * Retrieves a habit by its ID.
     * @param id the ID of the habit to retrieve
     * @return the habit if found, null if not
     */
    public Habit getHabitById(int id); // Read

    /**
     * Retrieves all habits associated with a user by their ID.
     * @param id the ID of the user
     * @return a list of habits associated with the user
     */
    public List<Habit> getHabitsByUserId(int id);

    /**
     * Retrieves all habits in the database.
     * @return a list of all habits
     */
    public List<Habit> getAllHabits(); // Read

    /**
     * Retrieves the completion streak of a habit.
     * @param habit the habit to retrieve the completion streak for
     * @return the completion streak of the habit
     */
   public int getHabitCompletionStreak(Habit habit);

    /**
     * Retrieves the current associated task (activity) of a habit.
     * @param habit the habit to retrieve the current associated task for
     * @return the current associated task of the habit
     */
   public Activity getCurrentAssociatedTask(Habit habit);

    /**
     * Retrieves all associated tasks (activities) of a habit in ascending order of age.
     * @param habit the habit to retrieve the associated tasks for
     * @return a list of associated tasks of the habit
     */
   public List<Activity> getAllAssociatedTasks(Habit habit);

    /**
     * Persists a new habit to the database.
     *
     * @param habit the habit object to be created and stored.

     */
   public void createHabit (Habit habit);
}
