package com.example.cab302project.model;

public interface IUserDAO {

    User getUserByEmail(String email);

    void addUser(User user);
}