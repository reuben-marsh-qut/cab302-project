package com.example.cab302project.model;

public class UserSetting
{

    private int userId;
    private String settingsKey;
    private int settingsValue;

    public int getUserId()
    {
        return userId;
    }

    public String getSettingsKey()
    {
        return settingsKey;
    }

    public int getSettingsValue()
    {
        return settingsValue;
    }

    public UserSetting(int userId, String settingsKey, int settingsValue)
    {
        this.userId = userId;
        this.settingsKey = settingsKey;
        this.settingsValue = settingsValue;
    }

}
