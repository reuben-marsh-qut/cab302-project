package com.example.cab302project.model;

import java.util.List;

// the DAO will do all the hashing prior to insert
public interface IUserDAO {

    // returns success == true, failure == false
    public boolean createUser(String email, String password, int postcode);

    // will return null if invalid, use for login
    public User loginUser(String email, String password);

    // updates the user in the database with the changes you made to user object
    // returns success == true, failure == false
    public boolean updateUser(User user);

    // updates the user's password
    // the DAO is responsible for hashing where required
    public boolean updatePassword(User user, String newPassword);

    public User getUserById(int userId);

    User getUserByEmail(String email);

    public List<User> getAllUsers();

    public void deleteUser(User user);

    public void deleteAllUsers();

    // use for testing ONLY
    void addUser(User user);
}