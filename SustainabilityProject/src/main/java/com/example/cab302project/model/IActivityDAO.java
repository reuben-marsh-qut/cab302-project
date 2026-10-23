package com.example.cab302project.model;

import java.util.List;

public interface IActivityDAO {

    /**
     * Adds a new activity to the database.
     * @param activity the activity to add
     */
    public void addActivity(Activity activity); // Create

    /**
     * Updates an existing activity in the database.
     * @param activity the activity to update
     */
    public void updateActivity(Activity activity); // Update

    /**
     * Deletes an activity from the database.
     * @param activity the activity to delete
     */
    public void deleteActivity(Activity activity); // Delete

    /**
     * Retrieves an activity by its ID.
     * @param id the ID of the activity
     * @return the activity with the specified ID
     */
    public Activity getActivityById(int id); // Read

    /**
     * Retrieves all activities in the database.
     * @return a list of all activities
     */
    public List<Activity> getAllActivities(); // Read

    /**
     * Retrieves all incomplete activities associated with a user by their ID.
     * @param userId the ID of the user
     * @return a list of incomplete activities associated with the user
     */
    public List<Activity> getIncompletedActivitiesForUser(int userId);

    /**
     * Retrieves all completed activities associated with a user by their ID.
     * @param userId the ID of the user
     * @return a list of completed activities associated with the user
     */
    public List<Activity> getCompletedActivitiesForUser(int userId);
}
