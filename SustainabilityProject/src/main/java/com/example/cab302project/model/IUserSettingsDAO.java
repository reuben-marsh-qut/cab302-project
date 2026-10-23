package com.example.cab302project.model;

import java.util.ArrayList;

public interface IUserSettingsDAO {

    /**
     * Creates a user setting
     * @param userId user's ID
     * @param settingsKey the name of the setting to create
     * @param settingsValue the value to set
     */
    public void createUserSetting(int userId, String settingsKey, int settingsValue);

    /**
     * Gets a user's settings
     * @param user user to get setting for
     * @return ArrayList of user's settings
     */
    public ArrayList<UserSetting> getSettingsByUser(User user);

    /**
     * Updates a user's setting
     * @param userId user's ID
     * @param settingsKey the name of the setting to update
     * @param settingsValue the value to set
     */
    public void updateUserSetting(int userId, String settingsKey, int settingsValue);

    /**
     * Deletes a user's setting
     * @param userId user's ID
     * @param settingsKey the name of the setting to delete
     */
    public void deleteUserSetting(int userId, String settingsKey);
}
