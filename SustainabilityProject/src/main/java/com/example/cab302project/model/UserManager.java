package com.example.cab302project.model;

public class UserManager {

    private final IUserDAO userDAO;

    public UserManager(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String register(String email, String password, int postcode) {
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return "Enter an email and password.";
        }

        email = email.trim().toLowerCase();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Enter a valid email.";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters.";
        }

        if (postcode < 1000 || postcode > 9999) {
            return "Enter a valid postcode.";
        }

        return userDAO.createUser(email, password, postcode) ? null : "Email already registered.";
    }

    public User login(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        if (email.isBlank() || password.isBlank()) {
            return null;
        }

        User user = userDAO.loginUser(email,password);

        if (user == null) {
            return null;
        }


        return user;
    }
}