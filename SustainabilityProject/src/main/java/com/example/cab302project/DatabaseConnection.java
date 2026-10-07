package com.example.cab302project;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Provides the application's shared SQLite database connection and
 * initialises its schema.
 */
public class DatabaseConnection {

    private static Connection instance;

    /**
     * Opens the application's SQLite database.
     *
     * @throws IllegalStateException if the database cannot be opened
     */
    private DatabaseConnection() {
        try {
            instance = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Could not open the application database.",
                    exception
            );
        }
    }

    /**
     * Returns the shared database connection, opening it when first needed.
     *
     * @return the application's database connection
     * @throws IllegalStateException if the database cannot be opened
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }

        return instance;
    }

    /**
     * Creates missing database tables and applies migrations to existing ones.
     *
     * @throws IOException if the database schema resource cannot be read
     * @throws IllegalStateException if schema creation or migration fails
     */
    static void initialise() throws IOException {
        String schema;

        try (InputStream schemaStream =
                     HelloApplication.class.getResourceAsStream(
                             "/database/createDB.sql"
                     )) {

            if (schemaStream == null) {
                throw new IOException(
                        "Database schema resource was not found."
                );
            }

            schema = new String(
                    schemaStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }
        migrate();
    }

<<<<<<< HEAD
    private static void migrate() {
        try {
            Connection connection = DatabaseConnection.getInstance();
            Statement statement = connection.createStatement();

            statement.execute("ALTER TABLE goals ADD COLUMN isPaused INTEGER NOT NULL DEFAULT 0");

            System.out.println("Database updated: added isPaused to the goals table.");
        } catch (SQLException exception) {
            // The column is already there, so this database is already up to date.
        }
    }
}
=======
        Connection connection = getInstance();
>>>>>>> origin/master

        try {
            try (Statement statement = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    if (!sql.isBlank()) {
                        statement.execute(sql);
                    }
                }
            }

            migrate(connection);
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Could not initialise the application database.",
                    exception
            );
        }
    }

    /**
     * Applies schema changes required by existing databases.
     *
     * <p>The tasks table must already exist. This method adds the nullable
     * completion timestamp column if it is missing. Historical activities
     * retain a null timestamp because their actual completion time is
     * unknown.</p>
     *
     * <p>This method can be called repeatedly without replacing existing
     * data or changing stored completion timestamps. The supplied connection
     * remains open.</p>
     *
     * @param connection the database connection to migrate
     * @throws SQLException if the schema cannot be inspected or updated
     */
    public static void migrate(Connection connection) throws SQLException {
        boolean completionColumnExists = false;

        try (Statement statement = connection.createStatement();
             ResultSet columns = statement.executeQuery(
                     "PRAGMA table_info(tasks)"
             )) {

            while (columns.next()) {
                if ("completedAtUnixTime".equalsIgnoreCase(
                        columns.getString("name")
                )) {
                    completionColumnExists = true;
                    break;
                }
            }
        }

        if (!completionColumnExists) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        ALTER TABLE tasks
                        ADD COLUMN completedAtUnixTime INTEGER
                        """);
            }
        }
    }
}