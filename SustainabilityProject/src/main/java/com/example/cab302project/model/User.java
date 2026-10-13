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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setUserExperience(int userExperience) {
        this.userExperience = userExperience;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    private int userId;
    private String email;
    private String passwordHash;
    private int userExperience;
    private int postcode;
    public User(int userId, String email, String passwordHash, int userExperience, int postcode){
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash; // this is argon2i
        this.postcode = postcode;
        this.userExperience=userExperience;
    }

    /**
     * @return User's detais in a formatted string pattern
     */
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

    public double getUserLevel()
    {
        int level = this.userExperience / 100;
        double flooredLevel = Math.floor(level);
        return flooredLevel;
    }

    public void giveUserXpTest()
    {
        this.userExperience = this.userExperience + 200;
    }
}
