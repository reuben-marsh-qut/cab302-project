package com.example.cab302project;

import com.example.cab302project.model.User;
import com.example.cab302project.model.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class databasetesting {
    static void run(){
        Connection connection = DatabaseConnection.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT sqlite_version()");
            statement.execute();
            var rs = statement.getResultSet();
            System.out.println(rs.getString(1));
            UserDAO dao = new UserDAO();
//            dao.deleteAllUsers();
//            dao.createUser("anne@anne.com", "chickens",4000);
//            dao.createUser("bob@anne.com", "squares",4200);
//            dao.createUser("jerryob@anne.com", "circles",4200);
//            User anne = dao.loginUser("anne@anne.com", "chickens");
//            System.out.println(dao.getAllUsers());
//            dao.deleteUser(anne);
//            System.out.println(dao.getAllUsers());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
