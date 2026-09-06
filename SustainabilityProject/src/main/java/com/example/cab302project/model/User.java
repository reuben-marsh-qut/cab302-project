package com.example.cab302project.model;

// class to represent User. shouldn't be constructed, instead obtained via database?
public class User {
    private int userId;
    private String email;
    private String passwordHash;
    private int userExperience;
    private int postcode;
    // this should be passed to a createUser? then what is the point TODO delete
    public User(String email, String password, int postcode){
        this.email = email;
        this.passwordHash = password; // TODO: HASH argon2i or whatever java has
        this.postcode = postcode;
        this.userExperience=0;
    }
    // debug
    public User(int userId, String email, String passwordHash, int userExperience, int postcode){
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash; // TODO: HASH argon2i or whatever java has
        this.postcode = postcode;
        this.userExperience=userExperience;
    }
}
