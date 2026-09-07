package com.example.cab302project.model;

// class to represent User. shouldn't be constructed, instead obtained via database?
public class User {
    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getUserExperience() {
        return userExperience;
    }

    public int getPostcode() {
        return postcode;
    }

    private int userId;
    private String email;
    private String passwordHash;
    private int userExperience;
    private int postcode;
    // this should be passed to a createUser? then what is the point TODO delete
//    public User(String email, String password, int postcode){
//        this.email = email;
//        this.passwordHash = password; // TODO: HASH argon2i or whatever java has
//        this.postcode = postcode;
//        this.userExperience=0;
//    }
//    // debug
    public User(int userId, String email, String passwordHash, int userExperience, int postcode){
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash; // this is argon2i
        this.postcode = postcode;
        this.userExperience=userExperience;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", userExperience=" + userExperience +
                ", postcode=" + postcode +
                ", passwordHash='" + (passwordHash==null ? "null" : "hasPassword") + '\'' +
                '}';
    }
}
