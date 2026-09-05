package com.example.cab302project.model;

import java.util.ArrayList;
import java.util.List;

public class MockActivityDAO implements IActivityDAO {
    public final ArrayList<Activity> activities = new ArrayList<>();
    private int autoIncrementedId = 0;

    @Override
    public void addActivity(Activity activity) {
        activity.setId(autoIncrementedId);
        autoIncrementedId++;
        activities.add(activity);
    }

    @Override
    public void updateActivity(Activity activity) {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).getId() == activity.getId()) {
                activities.set(i, activity);
                break;
            }
        }
    }

    @Override
    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    @Override
    public Activity getActivityById(int id) {
        for (Activity activity : activities) {
            if (activity.getId() == id) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public List<Activity> getAllActivities() {
        return new ArrayList<>(activities);
    }
}
