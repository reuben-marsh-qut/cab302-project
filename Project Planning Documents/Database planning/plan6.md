# notes

- [x] We should use STRICT tables
- [x] Text enums need to be changed to Int enums. Storing a catagory string is wasteful when it can be represented by a number instead
- [x] figure out catagories (mind, body, social, ~~planet~~)
- [ ]figure out better userExperience -> level equation
- [ ] currently the whole application is designed around goals and tasks where progression is 0 to positive threshold, should we consider other progressions (ie going from 100kg->80kg?)
- think about habit streak
- Seb "user complete action streak?"
- in SQLite autoincrement int primary key is somewhat implicit https://sqlite.org/autoinc.html
- PRIMARY KEY implies NOT NULL

## dao?
- given the design of the tables smarter getters such as "getHabitStreak" should be implemented
- getWhatever and setWhatever should require a user?


## relationship between tasks, habits and goals.

### Habits
- Habits are benefical behaviours that the user wants to do more regularly.
- Habits repeat at user specified frequencies (daily, weekly, monthly) and intervals (ie every two days, every four weeks)
- Habits may have a due/expiry date
- Habits may be attached to a goal
- Habits may have a start date (ex created on thursday but task start from friday)
- Habits automatically create an associated task every interval
- Habits must have a type for their associated task
- streak is not directly stored in DB?
- the experience from completing the associated task will gradually increase as the streak gets longer.

### Tasks
- Tasks are individual (potentially one-off) activities
- Tasks may have a due/expiry date
- Tasks may be attached to a goal
- Tasks must have a type. This allows for special UI for timer tasks (ex meditate for 15 minutes), binary tasks and progression tasks.
- Tasks grant xp on completion
### Goals
- Goals are long term achievements that the user is working towards. 
- Goals must belong to a catagory.
- On goal completion the xp of all the COMPLETED tasks that were assigned to that goal is granted again with some sort of TBD multipier (idk 0.5 or 1.5)?
- Binary goal. Either completed or not. Ex. Do a marathon next year
- Progression goal. Tasks can directly progress this goal (in addition to the user adding progress? idk about this). Ex. Walk 1 million steps in the next 12 months, progress in the walk 10 thousand steps daily habit would directly add progress toward the goal.


## user flow
1. Account Creation
2. User Login
3. Tutorial?
4. Create Goal/s, either custom or from one of our templates
5. Create Habit/s
6. Create Task/s


# users

```SQL
CREATE TABLE IF NOT EXISTS users(
    userId PRIMARY KEY INTEGER,
    email UNIQUE NOT NULL TEXT,
    passwordHash NOT NULL TEXT,
    userExperience NOT NULL INTEGER,
    postcode NOT NULL INTEGER
) STRICT;
PRAGMA foreign_keys = ON;
```
Postcode used for 'Community Goals'. This is a stretch goal.

Levels can be something the front end derives from userExperience using an equation (levels get progressively harder)

Seb: "1+ floor(experience/5)"

I'm against storing age, it's another bit of PII that I don't really think is needed?


# user settings

when a user is created all associated settings should be created.
If new settings are added after user creation that old user doesn't have they should be created with default values on access


I agree with Reuben, I'm not sure what we would store here and thus this might not be needed.

Settings should always have a defined value - NO NULLS

user-key-value allows for flexibility but we lose typing and our schema is now somewhat abstracted from the database.
We need to keep track of typing, migration (what happens when keys are removed/added/type changes), initalisation is now our responsiblity.

user,setting,setting,setting IS BETTER when we know exactly what settings we have

```sql
CREATE TABLE IF NOT EXISTS user_settings (
userId PRIMARY KEY INTEGER,
userId INTEGER NOT NULL REFERENCES users(userId) ON DELETE CASCADE,
settingsKey PRIMARY KEY TEXT,
settingsValue NOT NULL ANY,
) STRICT
```
# goals
isComplete should be a generated column "GENERATED ALWAYS AS (progress>=completionThreshold)" 

the meaning of completionThreshold depends on completionType, for binary goals (where completion is YES/NO) it is just 1 or 0, for progressive goals (where a numerical goal is progressed towards overtime (ie $10k saved)) is is the progress towards the goal.



```sql
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
) STRICT
```

# habits
The daily/weekly/monthly task derived from a habit is autogenerated 

```sql
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
) STRICT
```

# tasks

```sql
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
) STRICT
```

# friends
Not implementing this. Stretch Goal

```
table friends

FOREIGN KEY NOT NULL INTEGER, userId ON DELETE CASCADE REFERENCES users(userId) \
FOREIGN KEY NOT NULL INTEGER, friendId ON DELETE CASCADE REFERENCES users(userId) \
PRIMARY KEY(userId, friendId)
```


# research links
https://www.sqlite.org/datatype3.html
TEXT,NUMERIC,INTEGER,REAL,BLOB
We are using strict typing.

https://www.sqlite.org/lang_createtable.html#check_constraints
use this for any enums bc sqlite doesn't support enums.

https://andersmurphy.com/2026/06/05/the-perils-of-uuid-primary-keys-in-sqlite.html
using autoincrement INT for primary keys.

https://sqlite.org/foreignkeys.html
ON DELETE CASCADE means we don't need to manually delete values dependent on FKs. (ie on user delete all records associated are auto deleted)

Gemini recommended that I should add these
```
Fast lookup for due tasks per user
CREATE INDEX idx_tasks_user_due ON tasks(userId, dueUnixTime);

Fast lookup for checking habits generated for a date range
CREATE INDEX idx_tasks_habit_completion ON tasks(habitId, completedAtUnixTime);

Fast lookup for aggregating progress towards goals
CREATE INDEX idx_tasks_goal_contribution ON tasks(goalId) WHERE doesContributeDirectlyToGoal = 1;

```
