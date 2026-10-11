package com.example.cab302project.model;

public class UserManager {

    private final IUserDAO userDAO;

    public UserManager(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user with their email, password, and postcode
     * @param email the user's email
     * @param password the user's password
     * @param postcode the user's postcode
     * @return If registration is successful user is created and returns userDAO.createUser, otherwise an error message
     */
    public String register(
            String email,
            String password,
            int postcode
    ) {

        if (email == null
                || password == null
                || email.isBlank()
                || password.isBlank()) {

            return "Enter an email and password.";
        }

        email = email.trim().toLowerCase();

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            return "Enter a valid email.";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters.";
        }

        if (postcode < 0 || postcode > 9999) {
            return "Enter a valid postcode.";
        }

        return userDAO.createUser(
                email,
                password,
                postcode
        )
                ? null
                : "Email already registered.";
    }

    /**
     * Logs in an exisiting user
     * @param email the user's email
     * @param password the user's password
     * @return the logged in user, or null if login fails
     */
    public User login(
            String email,
            String password
    ) {

        if (email == null || password == null) {
            return null;
        }

        if (email.isBlank() || password.isBlank()) {
            return null;
        }

        return userDAO.loginUser(
                email,
                password
        );
    }

    /**
     * Updates a user's profile information
     * @param user the user to update
     * @param email the user's email
     * @param postcode the user's postcode
     * @return null if user is successfully updated, throws an error otherwise
     */
    public String updateProfile(
            User user,
            String email,
            int postcode
    ) {

        if (user == null) {
            return "No user is logged in.";
        }

        if (email == null || email.isBlank()) {
            return "Enter a valid email.";
        }

        email = email.trim().toLowerCase();

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            return "Enter a valid email.";
        }

        if (postcode < 0 || postcode > 9999) {
            return "Enter a valid postcode.";
        }

        User existingUser =
                userDAO.getUserByEmail(email);

        if (existingUser != null
                && existingUser.getUserId()
                != user.getUserId()) {

            return "Email already registered.";
        }

        user.setEmail(email);
        user.setPostcode(postcode);

        boolean updated =
                userDAO.updateUser(user);

        if (!updated) {
            return "Unable to update profile.";
        }

        return null;
    }

    /**
     * Changes a user's password
     * @param user the user to change the password for
     * @param currentPassword the user's current password
     * @param newPassword the user's new password
     * @return null if password is successfully changed, otherwise an error message
     */
    public String changePassword(
            User user,
            String currentPassword,
            String newPassword
    ) {

        if (user == null) {
            return "No user is logged in.";
        }

        if (newPassword == null || newPassword.length() < 8) {
            return "Password must be at least 8 characters.";
        }

        User authenticatedUser =
                userDAO.loginUser(
                        user.getEmail(),
                        currentPassword
                );

        if (authenticatedUser == null) {
            return "Current password is incorrect.";
        }

        boolean updated =
                userDAO.updatePassword(
                        user,
                        newPassword
                );

        if (!updated) {
            return "Unable to change password.";
        }

        return null;
    }
}