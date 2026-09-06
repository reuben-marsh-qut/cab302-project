PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS users(
                                    userId PRIMARY KEY INTEGER,
                                    email UNIQUE NOT NULL TEXT,
                                    passwordHash NOT NULL TEXT,
                                    userExperience NOT NULL INTEGER,
                                    postcode NOT NULL INTEGER
) STRICT;
CREATE TABLE IF NOT EXISTS user_settings (
                                             userId PRIMARY KEY INTEGER,
                                             userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
    settingsKey PRIMARY KEY TEXT,
    settingsValue NOT NULL ANY,
    ) STRICT;

CREATE TABLE IF NOT EXISTS goals (
                                     goalId PRIMARY KEY INTEGER,
                                     userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,

    goalTitle NOT NULL TEXT,
    catagory NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- mind, body, social, planet
    startsAtUnixTime NOT NULL INTEGER,
    dueUnixTime INTEGER,  -- if null does not expire

    progress NOT NULL INTEGER,
    completionThreshold NOT NULL INTEGER, -- if binary this is 1
    completionType NOT NULL INTEGER, -- binary (yes/no)=0, progression (0->1m steps by eoy)=1 . No constraint as this might be extended
    isComplete NOT NULL INT GENERATED ALWAYS AS (progress>=completionThreshold)
    ) STRICT;
CREATE TABLE IF NOT EXISTS habits (

                                      habitId PRIMARY KEY INTEGER,
                                      goalId PRIMARY KEY INTEGER REFERENCES goals(goalId),
    userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,

    baseXpReward NOT NULL INTEGER, -- of the derivied task
    habitTitle NOT NULL TEXT, -- used for derived tasks
    catagory NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- mind, body, social, planet
    taskType NOT NULL INTEGER, -- binary (yes/no)=0, progression (0->1m steps by eoy)=1, timer=2 . No constraint as this might be extended
    repeatFrequencyType NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- daily, weekly, monthly, yearly
    repeatFrequency NOT NULL INTEGER, -- 1 and daily = every day, 2 and daily = every 2 days, 2 and weekly = every 2 weeks
    startsAtUnixTime NOT NULL INTEGER, -- start tracking habit from this point.
    dueUnixTime INTEGER, -- if null does not expire IE if you wanted to go for a walk each day in November but none thereafter this would be set to expire 30th nov 11:59
    completionThreshold NOT NULL INTEGER, -- for derived tasks

    doesContributeDirectlyToGoal NOT NULL INTEGER CHECK(doesContributeDirectlyToGoal IN (0, 1)), -- (ie progress in steps would directly add steps to steps goal)
    ) STRICT;
CREATE TABLE IF NOT EXISTS tasks (
                                     taskId PRIMARY KEY INTEGER,
                                     goalId PRIMARY KEY INTEGER REFERENCES goals(goalId),

    habitId INTEGER REFERENCES habits(habitId),
    userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,

    taskTitle NOT NULL TEXT,
    catagory NOT NULL INTEGER CHECK (catagory IN (0, 1, 2, 3)), -- mind, body, social, planet
    taskType NOT NULL INTEGER, -- binary (yes/no)=0, progression (0->1m steps by eoy)=1, timer=2 . No constraint as this might be extended

    startsAtUnixTime NOT NULL INTEGER,
    dueUnixTime INTEGER,
    progress NOT NULL INTEGER ,
    completionThreshold NOT NULL INTEGER,
    baseXpReward NOT NULL INTEGER,
    awardedXpReward NOT NULL INTEGER,  -- up scaled for habit streak, down scaled if late.??
    doesContributeDirectlyToGoal NOT NULL INTEGER CHECK(doesContributeDirectlyToGoal IN (0, 1)), -- (ie progress in steps would directly add steps to steps goal)
    ) STRICT;
