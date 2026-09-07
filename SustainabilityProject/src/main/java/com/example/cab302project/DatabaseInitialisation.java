package com.example.cab302project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DatabaseInitialisation {
    static void initialise() throws IOException {
        InputStream inputStream = HelloApplication.class.getResourceAsStream("/database/createDB.sql");
        char[] raw = new char[inputStream.available()];
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)){
            streamReader.read(raw);
        }
        String createQuery = new String(raw);
//        System.out.println(createQuery);
        Connection connection = DatabaseConnection.getInstance();
        var queries = createQuery.split(";");
        try {
            Statement statement = connection.createStatement();

            for (String query: queries){
                statement.addBatch(query);
            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

    }
}
