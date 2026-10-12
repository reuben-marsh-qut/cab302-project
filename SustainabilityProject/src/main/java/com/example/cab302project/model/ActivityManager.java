package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityManager {
    private IActivityDAO activityDAO;

    public ActivityManager(IActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * Searches for activities with titles containing the query
     * @param query the target title string
     * @return List of all matching activities
     */
    public List<Activity> searchActivitiesByTitle(String query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityTitleMatched(activity, query))
                .toList();
    }

    /**
     * Checks if a given activity's title matches the query
     * @param activity the activity to inspect
     * @param query the target title string
     * @return true if the activity's title contains the query string
     */
    private boolean isActivityTitleMatched(Activity activity, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = activity.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    /**
     * Searches for activities with categories matching the query
     * @param query the target category
     * @return List of all matching activities
     */
    public List<Activity> searchActivitiesByCategory(Category query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityCategoryMatched(activity, query))
                .toList();
    }

    /**
     * Checks if a given activity's category matches the query
     * @param activity the activity to inspect
     * @param query the target category
     * @return true if the activity's category matches the query
     */
    private boolean isActivityCategoryMatched(Activity activity, Category query) {
        if (query == null) return true;
        Category searchCategory = activity.getCategory();
        return (searchCategory == query);
    }

    /**
     * Searches for completed activities with user IDs matching the query
     * @param query the target user ID
     * @return List of all matching completed activities
     */
    public List<Activity> searchCompletedActivitiesByUserId(Integer query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityUserIdMatched(activity, query))
                .filter(activity -> isActivityComplete(activity))
                .toList();
    }

    /**
     * Searches for incomplete activities with user IDs matching the query
     * @param query the target user ID
     * @return List of all matching incomplete activities
     */
    public List<Activity> searchIncompleteActivitiesByUserId(Integer query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityUserIdMatched(activity, query))
                .filter(activity -> !isActivityComplete(activity))
                .toList();
    }

    /**
     * Searches for activities with user IDs matching the query
     * @param query the target user ID
     * @return List of all matching activities
     */
    public List<Activity> searchActivitiesByUserId(Integer query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityUserIdMatched(activity, query))
                .toList();
    }

    /**
     * Checks if a given activity's user ID matches the query
     * @param activity the activity to inspect
     * @param query the target user ID
     * @return true if the activity's user ID matches the query
     */
    private boolean isActivityUserIdMatched(Activity activity, Integer query) {
        if (query == null) return true;
        Integer searchUserId = activity.getUserId();
        return (searchUserId.equals(query));
    }

    /**
     * Checks if a given activity is complete
     * @param activity the activity to inspect
     * @return true if the activity is complete
     */
    private boolean isActivityComplete(Activity activity) {
        return (activity.getIsComplete());
    }

    /**
     * Get all activities due before a specified date and time
     * @param dateTime the target date and time
     * @return List of all matching activities
     */
    public List<Activity> getActivitiesBeforeDate(LocalDateTime dateTime) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> activity.getDueDateTime().isBefore(dateTime))
                .toList();
    }

    /**
     * Adds a new activity to the database
     * @param activity the activity to add
     */
    public void addActivity(Activity activity) {
        activityDAO.addActivity(activity);
    }
}
