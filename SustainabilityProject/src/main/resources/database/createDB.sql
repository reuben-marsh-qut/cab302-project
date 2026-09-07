PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
                                     userId INTEGER PRIMARY KEY,
                                     email TEXT UNIQUE NOT NULL,
                                     passwordHash TEXT NOT NULL,
                                     userExperience INTEGER NOT NULL,
                                     postcode INTEGER NOT NULL
) STRICT;

CREATE TABLE IF NOT EXISTS user_settings (
                                             userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    settingsKey TEXT NOT NULL,
    settingsValue ANY NOT NULL,
    PRIMARY KEY (userId, settingsKey)
    ) STRICT;

CREATE TABLE IF NOT EXISTS goals (
                                     goalId INTEGER PRIMARY KEY,
                                     userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    goalTitle TEXT NOT NULL,
    catagory INTEGER NOT NULL CHECK (catagory IN (0, 1, 2, 3)),
    startsAtUnixTime INTEGER NOT NULL,
    dueUnixTime INTEGER,
    progress INTEGER NOT NULL,
    completionThreshold INTEGER NOT NULL,
    completionType INTEGER NOT NULL,
    isComplete INT GENERATED ALWAYS AS (progress >= completionThreshold)
    ) STRICT;

CREATE TABLE IF NOT EXISTS habits (
                                      habitId INTEGER PRIMARY KEY,
                                      goalId INTEGER REFERENCES goals(goalId),
    userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    baseXpReward INTEGER NOT NULL,
    habitTitle TEXT NOT NULL,
    catagory INTEGER NOT NULL CHECK (catagory IN (0, 1, 2, 3)),
    taskType INTEGER NOT NULL,
    repeatFrequencyType INTEGER NOT NULL CHECK (repeatFrequencyType IN (0, 1, 2, 3)),
    repeatFrequency INTEGER NOT NULL,
    startsAtUnixTime INTEGER NOT NULL,
    dueUnixTime INTEGER,
    completionThreshold INTEGER NOT NULL,
    doesContributeDirectlyToGoal INTEGER NOT NULL CHECK (doesContributeDirectlyToGoal IN (0, 1))
    ) STRICT;

CREATE TABLE IF NOT EXISTS tasks (
                                     taskId INTEGER PRIMARY KEY,
                                     goalId INTEGER REFERENCES goals(goalId),
    habitId INTEGER REFERENCES habits(habitId),
    userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    taskTitle TEXT NOT NULL,
    catagory INTEGER NOT NULL CHECK (catagory IN (0, 1, 2, 3)),
    taskType INTEGER NOT NULL,
    startsAtUnixTime INTEGER NOT NULL,
    dueUnixTime INTEGER,
    progress INTEGER NOT NULL,
    completionThreshold INTEGER NOT NULL,
    baseXpReward INTEGER NOT NULL,
    awardedXpReward INTEGER NOT NULL,
    doesContributeDirectlyToGoal INTEGER NOT NULL CHECK (doesContributeDirectlyToGoal IN (0, 1))
    ) STRICT;