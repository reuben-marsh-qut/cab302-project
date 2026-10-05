package com.example.cab302project.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExperienceDAO {

    private final Connection connection;
    private final ExperienceService experienceService =
            new ExperienceService();

    public ExperienceDAO(Connection connection) {
        this.connection = connection;
    }

    public void awardXp(int userId, int activityId) throws SQLException {
        // This method manages its own transaction.
        if (!connection.getAutoCommit()) {
            throw new SQLException(
                    "XP awards require a connection without an active transaction."
            );
        }

        connection.setAutoCommit(false);
        boolean transactionEnded = false;

        try {
            persistAward(userId, activityId);
            connection.commit();
            transactionEnded = true;
        } catch (SQLException | RuntimeException exception) {
            try {
                connection.rollback();
                transactionEnded = true;
            } catch (SQLException rollbackFailure) {
                exception.addSuppressed(rollbackFailure);
            }

            throw exception;
        } finally {
            if (transactionEnded) {
                connection.setAutoCommit(true);
            }
        }
    }

    private void persistAward(int userId, int activityId)
            throws SQLException {

        User user;
        Activity activity;

        String selectSql = """
                SELECT t.*,
                       u.userId AS recipientId,
                       u.email,
                       u.passwordHash,
                       u.userExperience,
                       u.postcode
                FROM tasks t
                JOIN users u ON u.userId = ?
                WHERE t.taskId = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(selectSql)) {

            statement.setInt(1, userId);
            statement.setInt(2, activityId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return;
                }

                user = new User(
                        result.getInt("recipientId"),
                        result.getString("email"),
                        result.getString("passwordHash"),
                        result.getInt("userExperience"),
                        result.getInt("postcode")
                );

                // Reuse the existing conversion from a database row.
                activity = new ActivityDAO()
                        .activityFromDatabaseRequest(result);
            }
        }

        int previousXp = user.getUserExperience();
        int previousAward = activity.getAwardedXpReward();

        // Reuse completion, duplicate-award and ownership checks.
        experienceService.awardXp(user, activity);

        int earnedXp = user.getUserExperience() - previousXp;

        if (earnedXp == 0) {
            return;
        }

        String updateActivitySql = """
                UPDATE tasks
                SET awardedXpReward = ?
                WHERE taskId = ?
                  AND userId = ?
                  AND awardedXpReward = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(updateActivitySql)) {

            statement.setInt(1, activity.getAwardedXpReward());
            statement.setInt(2, activityId);
            statement.setInt(3, userId);
            statement.setInt(4, previousAward);

            if (statement.executeUpdate() != 1) {
                throw new SQLException(
                        "Could not save the activity XP award."
                );
            }
        }

        String updateUserSql = """
                UPDATE users
                SET userExperience = userExperience + ?
                WHERE userId = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(updateUserSql)) {

            statement.setInt(1, earnedXp);
            statement.setInt(2, userId);

            if (statement.executeUpdate() != 1) {
                throw new SQLException(
                        "Could not save the user's XP total."
                );
            }
        }
    }
}