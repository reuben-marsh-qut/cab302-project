package com.example.cab302project.model;

public class LevelService {

    /**
     * Get the current level of the user based on their total XP.
     * @param totalXp total XP of the user
     * @return level of the user
     */
    public int getLevel(int totalXp) {
        int level = 1;
        long nextLevelThreshold = 100;

        while (totalXp >= nextLevelThreshold) {
            level++;
            nextLevelThreshold += 100L * level;
        }

        return level;
    }
}