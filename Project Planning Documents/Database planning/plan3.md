This plan is based upon Angus' plans 1 & 2, with some modifications based on my (Reuben) feedback.

### Key changes:
* A few new tables (friends, habits)
* Notes on existing tables
* A few new fields

### Plan:
#### Considerations:
* Strict typing - I think we should use strict typing, since will reduce the likelihood of running into weird runtime errors and the flexibility we get otherwise is not really necessary
* SSO - probably don't have the time to implement single sign-on 
* NULL dates - for: an activity doesn't have a set date
  * I think dates shouldn't be nullable since for analytics, it is important to know activity dates (i.e. I want to see activities completed over the last week we will need to know dates)
  * I don't think the user will actually have to input a date anywhere?? (assuming they can't retrospectively put in an activity, which I think is fair)
* Community goals - yes (this might be more of an extension, since it will be similar logic/UI implementation to the standard goals)
* 'super goals' - I think these are our categories => mind, body, social
  * These are not so much goals, but will exist as a field in the goals table - a goal belongs to a category, and they are certain app features that relate to categories:
    * e.g. app-suggested goals based on previous goal categories
* Goals are made up of activities (e.g. goal? might be a FK on the activities table (nullable since you can have an unrelated activity))
  * each activity contributes to goal (i.e. you get xp for activity completion, and more xp when you reach a goal)


#### Tables

table users

PRIMARY KEY AI INTEGER, userId \
UNIQUE NOT NULL TEXT, email \
NOT NULL blob/text, passwordHash \
NOT NULL INTEGER, userExperience // RM: I think this is a good place to store this, then levels are calculate dynamically, bsed on level logic\

// RM Fields

NOT NULL STRING, postcode // this could be used for community goals \
NOT NULL DATE, dob // might be useful to keep track to know age

user-key-value allows for flexibility but we lose typing and our schema is now somewhat abstracted from the database.
We need to keep track of typing, migration (what happens when keys are removed/added/type changes), initalisation is now our responsiblity.

should we allow null? i'm leaning towards no. settings should always have a defined value. // RM: I agree, no null settings
maybe user,setting,setting,setting might be better when we know exactly what settings we have?

table user_settings

PRIMARY KEY FOREIGN KEY NOT NULL INTEGER, userId ON DELETE CASCADE REFERENCES users(userId) \
PRIMARY KEY TEXT, key \
BLOB, value

// RM: Do we need this table (user_settings). I guess it could be useful if we wanted to introduce things like: accessibility - otherwise not really sure it is necessary

table goals

PRIMARY KEY AI INT, goalId \
NOT NULL INT, userId FK ON DELETE CASCADE \ 
NOT NULL TEXT, category constraint (mind, body, social) // RM: I think this is good (but I don't think we need planet) \
nullable INT dueUnixTime // if null does not expire? // RM: should goals expire? I don't think we need a 'due' date for this, unless it was for some sort of projections? i.e. by x date, you should be able to do y - which could be useful tbf \
NOT NULL INT progress // RM: a goal might be to do something x times in y interval (e.g. walk 20 times in a month) OR to just to be able to do something (e.g. run a sub 2 half mara) - so this might track PB? \
NOT NULL INT completionThreshold // **RM:** Not sure how we will implement the logic - maybe we have goalTypes wich define how we assess whether we have completed a goald/reached the threshold. e.g. for a sub 2 mara the threshold might = 120 (i.e. 120 minutes) and the goalType = maxTime therefore, if progress < threshold, goal is complete. For walking 20 times in 30 days, it might be threshold = 20 (which resets to 0 every month or 30 days), goalType = minReps \
NOT NULL INT, isComplete (constraint 0,1) do we need this? // RM: I think this is useful so we can track what goals the suer has previously completed for analytics

table tasks // RM: I think this is the activity table
 
PK AI INT, taskId \
NOT NULL INT, goalId FK idk // RM: I think this is useful, but should be nullable (an acitivity can be standalone) \
NOT NULL INT, userId FK ON DELETE CASCADE \
NOT NULL TEXT, taskTitle \
NOT NULL INT, taskDifficulty (constrainted betwen 1 and 10?) idk xp reward dependent on this? or should this just be xpReward // RM: I like this idea, since I think it will make the xp system easier to standardise \
NOT NULL TEXT, category constraint (mind, body, social) \
NOT NULL INT, isHabit (constraint 0,1) // RM: I think we have a separate habit table and a habitId field here? \
Nullable INT, repeatFreqUnix ie 2* 86400 * 1000 would be every two days // RM: I think this would be a field on the habit table \
Nullable INT, currentStreak can this be derived from task_completion_log? \
nullable INT dueUnixTime// if null does not expire? // RM: I think activities should always expire after a week (or less if attached to a habit, in which case the logic would be like today + habit.interval) \ 
NOT NULL INT progress \
NOT NULL INT doesContributeDirectlyToGoal CHECK(doesContributeDirectlyToGoal IN (0, 1)) (ie progress in steps would directly add steps to steps goal) \
NOT NULL INT completionThreshold

// RM Fields

NOT NULL INT, completedAtUnixTime \

should this have a userId FK (joins exist for a reason)

table task_completion_log? // RM: not sure what this is for

PK AI INT, logId \
NOT NULL INT FK, taskId \
NOT NULL INT, completedAtUnixTime \
NOT NULL INT, awardedXpReward // why? completing a task after the deadline should not give full xp rewards, this way the can be the source of truth for xp gains

// RM: friends table to track user's friends

table friends

FOREIGN KEY NOT NULL INTEGER, userId ON DELETE CASCADE REFERENCES users(userId) \
FOREIGN KEY NOT NULL INTEGER, friendId ON DELETE CASCADE REFERENCES users(userId) \
PRIMARY KEY(userId, friendId)

table habits

PK AI INT, habitId
nullable INT, goalId FK REFERENCES goals(goalId) // should this be nullable? \
NOT NULL INT, taskId FK REFERENCES tasks(taskId)
NOT NULL INT, intervalUnix // ie 2* 86400 * 1000 would be every two days
NOT NULL INT, currentStreak




