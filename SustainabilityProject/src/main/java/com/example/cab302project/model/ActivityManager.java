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

    public List<Activity> searchActivitiesByTitle(String query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityTitleMatched(activity, query))
                .toList();
    }

    private boolean isActivityTitleMatched(Activity activity, String query) {
        if (query == null || query.isEmpty()) return true;
        query = query.toLowerCase();
        String searchString = activity.getTitle();
        return searchString.toLowerCase().contains(query);
    }

    public List<Activity> searchActivitiesByCategory(Category query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityCategoryMatched(activity, query))
                .toList();
    }

    private boolean isActivityCategoryMatched(Activity activity, Category query) {
        if (query == null) return true;
        Category searchCategory = activity.getCategory();
        return (searchCategory == query);
    }

    public List<Activity> searchActivitiesByUserId(Integer query) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> isActivityUserIdMatched(activity, query))
                .toList();
    }

    private boolean isActivityUserIdMatched(Activity activity, Integer query) {
        if (query == null) return true;
        Integer searchUserId = activity.getUserId();
        return (searchUserId.equals(query));
    }

    public List<Activity> getActivitiesBeforeDate(LocalDateTime dateTime) {
        return activityDAO.getAllActivities()
                .stream()
                .filter(activity -> activity.getDueDateTime().isBefore(dateTime))
                .toList();
    }

    public void addActivity(Activity activity) {
        activityDAO.addActivity(activity);
    }
}
