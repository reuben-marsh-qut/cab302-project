package com.example.cab302project.model;

public class ExperienceService {

    public void awardXp(User user, Activity activity) {
        if (!Integer.valueOf(user.getUserId()).equals(activity.getUserId())) {
            return;
        }

        if (!activity.isComplete()) {
            return;
        }

        if (activity.getAwardedXpReward() > 0) {
            return;
        }

        int currentXp = user.getUserExperience();
        int reward = activity.getBaseXpReward();

        user.setUserExperience(currentXp + reward);
        activity.setAwardedXpReward(reward);
    }
}