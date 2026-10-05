package com.example.cab302project.model;

public class ExperienceService {

    public void awardXp(User user, Activity activity) {
        int currentXp = user.getUserExperience();
        int reward = activity.getBaseXpReward();

        user.setUserExperience(currentXp + reward);
    }
}