package com.example.cab302project.model;

public class LevelService {

    public int getLevel(int totalXp) {
        if (totalXp >= 100) {
            return 2;
        }

        return 1;
    }
}