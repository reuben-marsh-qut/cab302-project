package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;
import com.password4j.Password;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{
    @Override
    public boolean createUser(String email, String password, int postcode) {
        Connection conn = DatabaseConnection.getInstance();
        String passwordHash = Password.hash(password).withArgon2().getResult();

        try {
            PreparedStatement insertInto = conn.prepareStatement("INSERT INTO users (email, passwordHash, userExperience, postcode) VALUES (?, ?, 0, ?)");
            insertInto.setString(1,email);
            insertInto.setString(2,passwordHash);
            insertInto.setInt(3,postcode);
            int rowsAffected = insertInto.executeUpdate();
//            System.out.println(rowsAffected);
            if (rowsAffected!=1){
                return false;
            }

            return true;
//            return loginUser(email,password);
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")){
                return false;
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public User loginUser(String email, String password) {

        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM users WHERE email=?");
            getUser.setString(1, email);
            ResultSet pass = getUser.executeQuery();
            if (!pass.next()) {
                return null;
            }
            boolean passwordCheck = Password.check(password,pass.getString("passwordHash")).withArgon2();
//            System.out.println("password check "+ passwordCheck);
            if(!passwordCheck){
                return null;
            }

//            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM users WHERE email=? AND passwordHash=?");
//            getUser.setString(1,email);
//            getUser.setString(2,passwordHash);
//            ResultSet rs = getUser.executeQuery();
//            if (rs.getRow()==0) {
//                return null;
//            }
            return new User(pass.getInt("userId"),
                    pass.getString("email"),
                    pass.getString("passwordHash"),
                    pass.getInt("userExperience"),
                    pass.getInt("postcode"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUser(User user) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM users WHERE email=?");
            getUser.setString(1, user.getEmail());
            ResultSet doesOtherExist = getUser.executeQuery();
            if (!doesOtherExist.next()) {
                return false;
            }
            if (doesOtherExist.getInt("userId")!=user.getUserId()){
                return false;
            }
            PreparedStatement update = conn.prepareStatement("UPDATE users SET email=?, passwordHash=?, userExperience=?, postcode=? WHERE userId=?");
            update.setString(1,user.getEmail());
            update.setString(2,user.getPasswordHash());
            update.setInt(3,user.getUserExperience());
            update.setInt(4,user.getPostcode());
            update.setInt(5,user.getUserId());
            if (update.executeUpdate()!=1){
                throw new RuntimeException("update did not work"); // should just return null instead idk?
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(int userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

//    // Dont use this
//    @Override
//    public void createRawUser(int userId, String email, String passwordHash, int userExperience, int postcode) {
//
//    }

    @Override
    public List<User> getAllUsers() {
        Connection conn = DatabaseConnection.getInstance();
        List<User> users = new ArrayList<User>();

        try {
            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM users");
            ResultSet userSet = getUser.executeQuery();
            while (userSet.next()){
                users.add(new User(userSet.getInt("userId"),
                        userSet.getString("email"),
                        userSet.getString("passwordHash"),
                        userSet.getInt("userExperience"),
                        userSet.getInt("postcode")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void deleteUser(User user) {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM users WHERE userId=?");
            deleteUser.setInt(1,user.getUserId());
            deleteUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteAllUsers() {
        Connection conn = DatabaseConnection.getInstance();
        try {
            PreparedStatement deleteUsers = conn.prepareStatement("DELETE FROM users");
            deleteUsers.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addUser(User user) {
        Connection conn = DatabaseConnection.getInstance();
        PreparedStatement insertInto = null;
        try {
            insertInto = conn.prepareStatement("INSERT INTO users (userId, email, passwordHash, userExperience, postcode) VALUES (?,?, ?, ?, ?)");
            insertInto.setInt(1,user.getUserId());
            insertInto.setString(2,user.getEmail());
            insertInto.setString(3,user.getPasswordHash());
            insertInto.setInt(4,user.getUserExperience());
            insertInto.setInt(5,user.getPostcode());
            int rowsAffected = insertInto.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//
    }
}
