package com.example.cab302project.model;

public class LevelService {

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