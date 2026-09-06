package com.example.cab302project.model;

import java.util.ArrayList;
import java.util.List;

public class MockUserDAO implements IUserDAO {

    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User getUserByEmail(String email) {

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }
}