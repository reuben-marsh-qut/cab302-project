package com.example.cab302project.model;

import java.util.List;

// the DAO will do all the hashing prior to insert
public interface IUserDAO {

    /**
     * Creates a new user
     * @param email The new user's email
     * @param password The new user's password
     * @param postcode The new user's postcode
     * @return true if User created, false if not
     */
    public boolean createUser(String email, String password, int postcode);

    /**
     * logs an existing user in
     * @param email the user's email
     * @param password the user's password
     * @return the User if login successful, null if not
     */
    public User loginUser(String email, String password);

    /**
     * updates the user in the database with the changes you made to User object
     * @param user the updated user
     * @return true if User updated, false if not
     */
    public boolean updateUser(User user);

    /**
     * updates a user's password (the DAO is responsible for hashing where required)
     * @param user the user to update
     * @param newPassword the new password
     * @return true if User updated, false if not
     */
    public boolean updatePassword(User user, String newPassword);

    /**
     * Gets a user by their ID
     * @param userId the user's ID
     * @return the User if found, null if not
     */
    public User getUserById(int userId);

    /**
     * Gets a user by their email
     * @param email the user's email
     * @return the User if found, null if not
     */
    User getUserByEmail(String email);

    /**
     * Gets all users
     * @return a list of all users
     */
    public List<User> getAllUsers();

    /**
     * Deletes a user
     * @param user the user to delete
     */
    public void deleteUser(User user);

    /**
     * Deletes all users
     */
    public void deleteAllUsers();

    /**
     * Adds a user to the database (testing ONLY)
     * @param user the user to add
     */
    void addUser(User user);
}