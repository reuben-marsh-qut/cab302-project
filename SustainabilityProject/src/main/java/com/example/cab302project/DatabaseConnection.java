package com.example.cab302project;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

        Connection connection = getInstance();

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
     * <p>The tasks and goals tables must already exist. This method adds the
     * nullable completion timestamp column to tasks, and the paused flag to
     * goals, if either is missing. Historical activities retain a null
     * timestamp because their actual completion time is unknown, and existing
     * goals default to not paused.</p>
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

        boolean pausedColumnExists = false;

        try (Statement statement = connection.createStatement();
             ResultSet columns = statement.executeQuery(
                     "PRAGMA table_info(goals)"
             )) {

            while (columns.next()) {
                if ("isPaused".equalsIgnoreCase(
                        columns.getString("name")
                )) {
                    pausedColumnExists = true;
                    break;
                }
            }
        }

        if (!pausedColumnExists) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("""
                        ALTER TABLE goals
                        ADD COLUMN isPaused INTEGER NOT NULL DEFAULT 0
                        """);
            }
        }
    }
}