package com.example.cab302project.model;

import java.util.List;

public interface IActivityDAO {
    public void addActivity(Activity activity); // Create
    public void updateActivity(Activity activity); // Update
    public void deleteActivity(Activity activity); // Delete
    public Activity getActivityById(int id); // Read
    public List<Activity> getAllActivities(); // Read
    public List<Activity> getIncompletedActivitiesForUser(int userId);
    public List<Activity> getCompletedActivitiesForUser(int userId);
}
