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
        return false;
    }

    @Override
    public User loginUser(String email, String password) {
        for (User check : users) {
            if (check.getEmail().equalsIgnoreCase(email)) {
                if (check.getPasswordHash().equals(password)){
                    return check;
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        for (User check : users) {
            if (check.getUserId() == user.getUserId()) {
                users.remove(check);
                users.add(user);
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
        for (User check : users) {
            if (check.getUserId() == user.getUserId()) {
                users.remove(check);
            }
        }
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}