package com.example.cab302project.model;

public class UserManager {

    private final IUserDAO userDAO;

    public UserManager(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        if (email.isBlank() || password.isBlank()) {
            return null;
        }

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }
}