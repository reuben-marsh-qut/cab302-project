package com.example.cab302project;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


// database connection singleton from activity 4.1
public class DatabaseConnection {
    private static Connection instance = null;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }
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

