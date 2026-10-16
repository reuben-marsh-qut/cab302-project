package com.example.cab302project.model;

import com.example.cab302project.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 */
public class UserSettingsDAO implements IUserSettingsDAO {

    /**
     *
     * @param userId
     * @param settingsKey
     * @param settingsValue
     */
    @Override
    public void createUserSetting(int userId, String settingsKey, int settingsValue)
    {
        try
        {
            Connection connection = DatabaseConnection.getInstance();
            String query = "INSERT INTO user_settings (userId, settingsKey, settingsValue) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, settingsKey);
            statement.setInt(3, settingsValue);

            int result = statement.executeUpdate();

        }
        catch (RuntimeException | SQLException e)
        {
            throw new RuntimeException(e);
        }


    }


    /**
     *
     * @param user
     * @return
     */
    @Override
    public ArrayList<UserSetting> getSettingsByUser(User user)
    {
        try
        {
            ArrayList<UserSetting> settingsArray = new ArrayList<>();
            int userId = user.getUserId();
            Connection connection = DatabaseConnection.getInstance();
            String query = "SELECT * FROM user_settings WHERE userId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                String settingsKey = results.getString("settingsKey");
                int settingsValue = results.getInt("settingsValue");
                UserSetting setting = new UserSetting(userId, settingsKey, settingsValue);
                settingsArray.add(setting);
            }

            return settingsArray;

        }
        catch (RuntimeException | SQLException e)
        {
            throw new RuntimeException(e);
        }

    }


    /**
     *
     * @param userId
     * @param settingsKey
     * @param settingsValue
     */
    @Override
    public void updateUserSetting(int userId, String settingsKey, int settingsValue)
    {
        try
        {
            Connection connection = DatabaseConnection.getInstance();
            String query = "UPDATE user_settings SET settingsValue = ? WHERE userId = ? AND settingsKey = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, settingsValue);
            statement.setInt(2, userId);
            statement.setString(3, settingsKey);

            int results = statement.executeUpdate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }



    }

    /**
     *
     * @param userId
     * @param settingsKey
     */
    @Override
    public void deleteUserSetting(int userId, String settingsKey)
    {

        try
        {
            Connection connection = DatabaseConnection.getInstance();
            String query = "DELETE FROM user_settings";
            PreparedStatement statement = connection.prepareStatement(query);
            int results = statement.executeUpdate();

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
