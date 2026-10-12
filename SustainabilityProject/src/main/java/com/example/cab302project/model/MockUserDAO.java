package com.example.cab302project.model;

import java.util.ArrayList;
import java.util.List;

// no hashing done
public class MockUserDAO implements IUserDAO {

    private final List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public boolean createUser(String email, String password, int postcode) {
        if (getUserByEmail(email) != null) {
            return false;
        }

        users.add(
                new User(
                        users.size() + 1,
                        email,
                        password,
                        0,
                        postcode
                )
        );

        return true;
    }

    @Override
    public User loginUser(String email, String password) {

        for (User check : users) {

            if (check.getEmail().equalsIgnoreCase(email)) {

                if (check.getPasswordHash().equals(password)) {
                    return check;
                }
            }
        }

        return null;
    }

    @Override
    public boolean updateUser(User user) {

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserId() == user.getUserId()) {
                users.set(i, user);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updatePassword(User user, String newPassword) {

        for (User existingUser : users) {

            if (existingUser.getUserId() == user.getUserId()) {
                existingUser.setPasswordHash(newPassword);
                return true;
            }
        }

        return false;
    }

    @Override
    public User getUserById(int userId) {

        for (User user : users) {

            if (user.getUserId() == userId) {
                return user;
            }
        }

        return null;
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

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users);
    }

    @Override
    public void deleteUser(User user) {

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserId() == user.getUserId()) {
                users.remove(i);
                return;
            }
        }
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}