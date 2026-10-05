package com.example.cab302project.model;

import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.TaskType;

import java.time.LocalDateTime;

public class ActivityFactory {

    public Activity createActivity(
            int goalId,
            int habitId,
            int userId,
            String title,
            Category category,
            TaskType taskType,
            int target
    ) {
        return new Activity(
                goalId,
                habitId,
                userId,
                title,
                category,
                taskType,
                LocalDateTime.now(),
                null,
                0,
                target,
                100,  // Potential XP reward for completing the activity.
                0,    // No XP has been awarded yet.
                false
        );
    }
}