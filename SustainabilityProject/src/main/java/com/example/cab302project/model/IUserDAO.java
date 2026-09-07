package com.example.cab302project.model;

import java.util.List;

// the DAO will do all the hashing prior to insert
public interface IUserDAO {
    // returns success == true, failure == false
    public boolean createUser(String email, String password, int postcode);
    // will return null if invalid, use for login
    public User loginUser(String email, String password);
    // updates the user in the database with the changes you made to user object returns success == true, failure == false
    public boolean updateUser(User user);

    // ideally this does not get used
//    public void createRawUser(int userId, String email, String passwordHash, int userExperience, int postcode);
    public List<User> getAllUsers();
    public void deleteUser(User user);
    public void deleteAllUsers();
}
