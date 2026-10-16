package com.example.cab302project.model;

import java.util.ArrayList;

public interface IUserSettingsDAO {

    public void createUserSetting(int userId, String settingsKey, int settingsValue);

    public ArrayList<UserSetting> getSettingsByUser(User user);

    public void updateUserSetting(int userId, String settingsKey, int settingsValue);

    public void deleteUserSetting(int userId, String settingsKey);
}
