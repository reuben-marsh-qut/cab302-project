package com.example.cab302project;

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
