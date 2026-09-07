# Consolidated User Stories

## Priority Definitions
- **Must Have** - Is part of the core application and is needed to satisfy the project requirements
- **Should Have** - It is important to the experience but could be reduced if time is a constraint
- **Could Have** - it is a valuable addition, but it is not necessary for the core product
- **Future/Out of Scope** - No intended for the initial implementation but could be added later

## Epic 1 - Accounts & Application Access
### US-01 - Register an Account

Priority: Must Have
Estimated Time: 5 hours

As a user, I want to create an account so that my goal and progress is stored privately for me.

Acceptance Criteria
- If I do not have an account after filling the registration form with valid details, a new account is created
- If I choose a username that is being used when attempting to register then I am told to choose another username
- If my password does not satisfy the password requirements then when I try to register I am shown a validation message

Consolidation Note
User story was taken from Reuben + Angus

### US-02 - Log In

Priority - Must Have
Estimated Time: 4 hours

As a registered user, I want to log in so that I can access my goals and progress

Acceptance Criteria
- If I have registered an account if I have entered the correct details then I am sent to the home screen
- If I enter the wrong details then I remain on the log in screen with a message
- After logging in I can only see data associated to my account

Consolidation Note

Source Ruben + Angus

Minor Consolidation

### US-03 - Update Profile Information
Priority: Should Have
Estimated Time: 4 hours

As a user, I want to update my profile information so that my account details remain current

Acceptance Criteria
- If I am logged in and change my profile information and save it the changes are stored
- If I attempte to change my username to one that is taken then the application rejects the change
- If I change the password I must provide a correct and current password before the new password is accepted

### US-04 - Log Out

Priority: Should Have
Estimated Time: 1.5 hours

As a user, I want to log out so that another person using my computer cannot access my account

Acceptance Criteria
- Given when I log out my authenticated session ends
- After logging out, it requires me to log back into access account specific things

## Epic 2 - Goals
### US-05 - Browse Goal Templates

Priority: Must Have
Estimated Time: 4 hours

As a user, I want to browse available goal templates so that I can choose a wellbeing goal appropriate for me

Acceptance Criteria
- Goal Templates are displayed to the user
- Each Template contains information for the user to understand its purpose
- Each template belongs to Mind, Body or Social
- Users can filter or browse templates by category


### US-06 - Create a Long-Term Goal

Priority: Must Have
Estimated Time: 6 hours

As a user, I want to create a personalized goal from a goal tempalte so that I can work towards an achievable wellbeing outcome

Acceptance criteria
- Given that I select a goal template, when I enter the target and deadline the goal is created
- The goal belongs to Mind, Body, or Social
- A deadline can't be put in the past
- Invalid Goal information produces an appropriate validation message

### US-07 - Have Multiple Active Goals

Priority: Should Have
Estimated Time: 3 hours

As a user, I want to work towards multiple goals at the same time so that I can improve different areas of my wellbeing

Acceptance Criteria
- Create another goal does not remove existing goal
- Multiple goals can remain active concurrently
- Completing or changing one goal does not unintentionally affect another

### US-08 - Edit a Goal
Priority: Should have
Estimated Time: 4 hours

As a user, I want to edit an active goal so that I Can adjust it when my circumstances change

Acceptance Criteria:
- If I have an existing goal, when I edit valid information and save it, the goal is updated
- Existing progress is retained where appropriate

### US-09 - Pause and Resume a Goal

Priority: Should have
Estimated Time: 3 hours

As a user, I want to pause and resume goals so that I can temporarily stop working towards a goal without losing my progress

Acceptance Criteria
- An Active Goal can be paused
- Pausing a goal retains its existing progress
- A paused goal can later be resumed
- Resuming restores goal to active status

### US-10 - Delete a Goal
Priority: Should Have
Estimated Time: 2 hours

As a user, I want to remove a goal that I no longer want to pursue so that my active goals remain relevant to me

Acceptance Criteria

- An Existing goal can be selected for deletion
- The user confirms the deletion before it occurs
- the deleted goal no longer appears as active

### US-11 - View Goal Progess

Priority: Must Have
Estimated Time: 7 hours

As a user, I want to see my progress towards each goal so that I know how close I am to achieving it

Acceptance Criteria
- Goal Progess is measurable
- Progress is displayed in an understandable format
- Completing related activities updates goal progress
- Achieving the target marks the goal as completed
- A goal that reaches its deadline without meeting its target can be identified as not achieved



### US-12 — View Completed Goals
**Priority:** Should Have
Estimated Time: 3 hours

**As a user, I want to view goals I have completed so that I can see what I have achieved over time.**

### Acceptance Criteria
- Completed goals are available through goal history.
- Completed goals are distinguishable from active goals.
- Relevant completion information remains available.



### US-13 — Receive Goal Recommendations
**Priority:** Should Have
Estimated Time: 5 hours

**As a user, I want the application to recommend goals in areas where I have been less active so that I can develop more balanced wellbeing behaviors.**

### Acceptance Criteria
- The application can compare completed activity levels across Mind, Body and Social.
- A recommended goal comes from an area in which the user has completed fewer activities.
- Recommendations use valid available goal templates.



### US-14 — Reconsider an Unsuccessful Goal
**Priority:** Should Have
Estimated Time: 5 hours

**As a user, I want the application to suggest reconsidering a goal when I consistently struggle with its activities so that I can choose something more achievable.**

### Acceptance Criteria
- Repeated failure to complete relevant activities can be detected.
- The application may suggest reconsidering the goal.
- The existing goal is not automatically removed.
- The user decides whether to change or replace it.



# Epic 3 — Habits

### US-15 — Generate Habits From Goals
**Priority:** Must Have
Estimated Time: 6 hours

**As a user, I want appropriate habits to be established from my goals so that I have repeatable behaviors that help me make progress.**

### Acceptance Criteria
- Creating an appropriate goal establishes its related habit or habits.
- Generated habits remain associated with their goal.
- Each habit has a defined frequency.



### US-16 — Understand How a Habit Supports a Goal
**Priority:** Must Have
Estimated Time: 2 hours

**As a user, I want to see how a habit relates to my goal so that I understand why I am being asked to complete it.**

### Acceptance Criteria
- A habit displays its associated goal.
- The required habit frequency is visible.
- The behavior required by the habit is explained.



### US-17 — Track Habit Completion
**Priority:** Must Have
Estimated Time: 7 hours

**As a user, I want my recurring habit completions to be tracked so that I can monitor whether I am maintaining healthy behaviors.**

### Acceptance Criteria
- Habit-related activity completions are recorded.
- Completion dates are retained.
- The application can determine whether the required frequency is being met.
- Previous habit completion history can be viewed.



# Epic 4 — Activities

### US-18 — View Activities I Need to Complete
**Priority:** Must Have
Estimated Time: 5 hours

**As a user, I want to see my upcoming activities so that I know what I should work on next.**

### Acceptance Criteria
- Activities due today are visible.
- Upcoming activities can be viewed.
- Activities explain what needs to be completed.
- Related goals or habits are identifiable.



### US-19 — Complete an Activity
**Priority:** Must Have
Estimated Time: 5 hours

**As a user, I want to mark an activity as completed so that my progress is recorded.**

### Acceptance Criteria
- An incomplete activity can't be marked complete.
- Its completion date is recorded.
- Related goal/habit progress is updated.
- Applicable XP is awarded once.



### US-20 — Complete Standalone Activities
**Priority:** Must Have
Estimated Time: 4 hours

**As a user, I want to complete activities outside my existing goals so that I can engage in additional positive wellbeing behaviors.**

### Acceptance Criteria
- Standalone activities can exist without a goal or habit.
- They can be marked complete.
- Their completion is recorded in history.
- Applicable XP is awarded.



## US-21 — View Activity Information
**Priority:** Must Have
Estimated Time: 2 hours

**As a user, I want to see information about an activity so that I understand what I need to do and what progress it contributes towards.**

### Acceptance Criteria
- Activity description is displayed.
- Category is displayed.
- Related goal/habit is displayed where applicable.
- Available XP is displayed.



### US-22 — View Completed Activities
**Priority:** Must Have
Estimated Time: 3 hours

**As a user, I want to view my completed activities so that I can review what I have accomplished.**

### Acceptance Criteria
- Completed activities are shown in history.
- Completion dates are available.
- Categories are identifiable.
- An appropriate empty state is shown where there is no history.



### US-23 — Search and Filter Activities
**Priority:** Could Have
Estimated Time: 4 hours

**As a user, I want to search and filter activities so that I can quickly find relevant items.**

### Acceptance Criteria
- Activities can be searched by text.
- Relevant filters can be applied.
- Filters can be cleared.



# Epic 5 — Gamification

### US-24 — Earn XP
**Priority:** Must Have
Estimated Time: 4 hours

**As a user, I want to earn XP when I complete activities so that I feel rewarded for making positive progress.**

### Acceptance Criteria
- Eligible activity completion awards XP.
- XP is added to the user's total.
- The earned XP is visibly communicated.
- Activity XP value can be viewed beforehand.



### US-25 — Increase My Level
**Priority:** Must Have
Estimated Time: 4 hours

**As a user, I want my level to increase as I earn XP so that I have a visible sense of progression.**

### Acceptance Criteria
- Current XP and level are visible.
- Reaching an XP threshold increases the level.
- The user receives feedback when leveling up.



### US-26 — Build Habit Streaks
**Priority:** Should Have
Estimated Time: 6 hours

**As a user, I want to build streaks by consistently completing habits so that I am motivated to maintain positive behaviors.**

### Acceptance Criteria
- Successful consecutive habit periods increase the streak.
- Missing the required period breaks or resets the streak according to its rules.
- Current streak is visible.
- Streak logic respects the habit frequency.



### US-27 — Earn and View Rewards
**Priority:** Should Have
Estimated Time: 6 hours

**As a user, I want to unlock rewards when I achieve milestones so that my progress feels meaningful and rewarding.**

### Acceptance Criteria
- Defined milestones can unlock rewards.
- Newly earned rewards are communicated.
- Earned rewards can be viewed.
- Locked rewards show their unlock requirements.



# Epic 6 — Progress & Reporting

### US-28 — View Progress Over Time
**Priority:** Must Have
Estimated Time: 7 hours

**As a user, I want to view my activity progress over time so that I can see how consistently I have been working on my wellbeing.**

### Acceptance Criteria
- Completed activity information is presented graphically.
- Activity completion can be viewed over time.
- Changing the reporting period updates the information displayed.
- Reported values match activity history.



### US-29 — Compare Wellbeing Categories
**Priority:** Should Have
Estimated Time: 5 hours

**As a user, I want to compare my Mind, Body and Social progress so that I can identify areas of my wellbeing receiving less attention.**

### Acceptance Criteria
- Mind, Body and Social are presented together.
- Differences are understandable without relying solely on color.
- Completing relevant activities updates the category information.
- The same calculation approach is used consistently.



# Epic 7 — Application Experience

### US-30 — Navigate the Main Application
**Priority:** Must Have
Estimated Time: 4 hours

**As a user, I want to easily navigate between the application's primary features so that I can use Rooted without unnecessary effort.**

### Acceptance Criteria
- Logging in takes the user to the home screen.
- Core application sections are accessible from the primary navigation.
- Navigation behaves consistently.



### US-31 — Use Different Window Sizes
**Priority:** Must Have
Estimated Time: 6 hours

**As a user, I want the application to remain usable when its window size changes so that I can use it on different displays.**

### Acceptance Criteria
- Important content remains accessible across supported sizes.
- Controls do not overlap or become unusable.
- Components resize or reorganize appropriately.



### US-32 — Persist My Progress
**Priority:** Must Have
Estimated Time: 6 hours

**As a user, I want my progress to remain saved after I close the application so that I do not lose my work.**

### Acceptance Criteria
- Goals, habits, activities, progress and history are stored locally.
- Closing and reopening the application retains stored data.
- Data is associated with the appropriate user.



# Candidate Enhancement Stories

### CE-01 — High-Contrast Theme
**Priority:** Could Have
Estimated Time: 4 hours

**As a user with visual accessibility needs, I want a high-contrast display option so that application content is easier to distinguish.**



### CE-02 — Screen Reader Accessibility
**Priority:** Could Have
Estimated Time: 8 hours

**As a user who relies on assistive technology, I want application controls and content to work effectively with screen readers so that I can navigate the application.**



### CE-03 — Notification Reminders
**Priority:** Could Have
Estimated Time: 6 hours

**As a user, I want optional reminders for activities so that I am encouraged to remain consistent.**



### CE-04 — Application Theme Customisation
**Priority:** Could Have
Estimated Time: 5 hours

**As a user, I want to customize the visual theme so that I can choose an interface I find comfortable.**



# Parked / Future Stories

The following ideas are retained for possible future releases but are not currently part of the initial implementation:

- Create community groups
- Join community groups
- Add friends
- Invite users to community goals
- Participate in community challenges
- Community progress tracking
- Weekly XP leaderboards
- Compare XP with friends
- Cross-device data synchronization
- In-game store or currency
- Purchase cosmetic/profile items
- Reward-based profile customization
- Forgotten-password recovery through security questions



# Non-Functional Requirements / Definition of Done Candidates

The following should be managed as technical, quality, design or compliance requirements rather than standalone functional user stories:

- Consistent application styling
- Appropriate icon usage
- Consistent terminology and capitalization
- Copyright compliance
- Privacy requirements
- Appropriate operating-system data storage locations
- Navigation/performance targets
- General visual quality
- Responsive layout behavior
- Accessibility standards
