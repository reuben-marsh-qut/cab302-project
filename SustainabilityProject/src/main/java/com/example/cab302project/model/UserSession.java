package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;

import java.sql.Connection;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession () {}

    /**
     *
     * @return the singleton instance of UserSession
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     *
     * @return get the user currently logged in
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user the user to log in
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Clear the user session
     */
    public void clearUserSession() {
        this.user = null;
    }
}
