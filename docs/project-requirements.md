# Project Requirements

Date: 17/08/2026
Version Number: 1.0.0

Document Structure: https://plaky.com/blog/business-requirements-document-template/

## Executive Summary

*Rooted* is a productivity game that focuses on behaviours and tasks which promote and facilitate good mental health. 
The game leverages an XP-based level system, as well as player rewards and incentives to encourage them to engage in activities which will improve their mental health.
Progression in the game is based around three related items: goals, habits and activities. The three pillars act to 
keep uses engaged and consciously progressing towards their aims, improving their mental health.

1. Goals: Based on App-defined (using AI) goal templates which are related to, and proven to improve mental health, users are able to tailor goals based on what is realistic and achievable for them. Habits are the cornerstone of goals and contribute to the completion of goals.
2. Habits: A habit is an app-defined repeated task which the user should complete to gain progress towards goals and procure in-game rewards. A habit may require a user to complete an activity some number of times a day, a week or a month. Habits are defined by the app based on the user-defined goals.
3. Activities: Activities are the foundation of habits. Habits require activities to be completed periodically. As such, an activitiy is a singular task which may be part of building a habit. Activities can also be standalone (i.e. not attached to a habit).

By building habits, users are able to reinforce behaviours which promote good mental health. The game keeps users engage via in-game rewards and in-game streaks, which are tied to building habits.
Also, the game divides goals, habits and activities into three categories, which each have distinct importance in 
developing strong mental health: mind, focussing on being mindful, thoughtful and 'in the moment'; body, physical exercise
promoting positive mental health and social, focussing on connection, building meaningful relationships.

This document outlines project objectives, the necessity for *Rooted* presently, project scope, requirements, stakeholders and constraints.

## Project Objectives

The primary objective of *Rooted* is to help users develop and maintain postive mental health behvaviours through gamified producitvity systems. This application
is trying to make the process of building healthy routines more intresting and engaging by combining goals, habbits and activities with progression systems such as XP, levels, streaks and rewards.

The project has the following objectives:

- Develop a functional application in the given timeframe that allows users to create and account, manage wellbeing goals, establish habbits and complete activities
- Provide users with templates for goals across mind, body and social categories, and allow goals to be adapated to the users own circumstances
- It should enable users to monitor their own progress towards goals through measurable habit and activity completion
- The system needs to implement an XP and level system that rewards users for completing their activites and maintaining their habbits
- It should implement streaks and in-game rewards to encourage users to return to the application and continue working towards their goal
- Provide repots that give users the ability to review their progress over time and compare across mind, body and social categories
- The app should provide personalised recommendations for activities and goals based on user's existing goals and previous activities
- Help users develop sustainable behaviours relating to their personal wellbeing

## Needs Statement

*Rooted* addresses a pertinent topic which impacts all people in society in their daily lives - mental health.
It is a logical and necessary tool to support positive mental health given the continually evolving mobile space.
The app provides a means for people who may otherwise spend time engaging in unhealthy behaviours like doomsccrolling 
on social media, isolating themselves from friends and family and eating heavily processed foods. *Rooted* bridges the gap between
the addictive pull of social media and healthy, productive activities. In turn, helping people who may be at risk of falling into 
unhealthy behaviours to avoid them and, instead, foster healthy habits. As such, the app targets people who may be spending lots of 
time on their devices, on social media, for example. [This demographic is mainly 16 to 24 year olds.](https://explodingtopics.com/blog/social-media-usage)

In relation to the theme 'Technology for Sustainable Futures', *Rooted* especially target the *people* dimension of sustainability,
encouraging users to nurture themselves and prioritise their mental health. In turn, users could 
achieve financial gain, for example if their goal related to savings, tapping into the *prosperity* dimension.

## Project Scope

The scope of *Rooted* covers the design, implementation and testing of a gamified application which helps users deleop postive wellbeing behaviours.

### In Scope

The project will include:

- User Registration and Login
- User Profile Management
- Goal creation using predefined goal templates
- Classification of goals, habits and activies into the mind, body and social wellbeing categories
- Creation and tracking of habits related to the users goal
- Support for standalone activites that are not associated with a habbit
- Tracking progress towards habits and goals
- Awarding XP for completing things
- Players levels are based on accumulated XP
- Habit streak tracking
- In-game rewards assocaiated with user progress
- A method for us to view earned rewards
- Historical tracking of completeed activities
- Reporting of user progress over time
- Comparision of progress between mind, body and social categories
- Suggested new goals based on areas where the users has completed fewer activities
- Suggestions for users to reconsider goals with their associated activities are repeatedely not completled

### Out of Scope

The intial version of *Rooted* will not include:

- Diagnosis of mental health conditions
- Clinical treatment plans or medical advice
- replacement of professional mental health services
- Emergency of crisis-support services
- Communication between users and healthcare professionals
- A public social-media network or direct messaging between users
- Real-moeny purchases or finacial transactions
- Integrations with external wearble devices

These featuers may be future possible extentions but are not required for intial implementation

## Requirements

The following requirements relate to identified key features of the app:

- The user should be able to register to create an account
- The user should be able to log in once registered with an account
- The user should be able to update their profile information
- The user should be able to set goals based on templates
- The software shall generate activities based on user goals
- The software shall generate activities which allow for habit building
- The user should be able to view their past activities as graphical reports
- The user should be able to compare their mind, body and social progress as graphical reports
- The user should be able to obtain rewards for completing activities
- The user should be able to view their rewards
- The software should suggest for the user to replace their goals if they are repeatedly not completing related activities
- The software shall award the user XP for completing activities
- The software shall provide the user suggestions for new goals based on categories which they done the fewest activities in

## Key Stakeholders

### End Users

The end user is the primary stakeholder of *Rooted*. They are individuals who want to improve their everyday wellbeing. The users need the application
tobe easy to use, engaging, reliable and motivating. They design needs to account for them to better influence the applications goal setting, habit-tracking and progress reporting/gamification features.

### The Project Development Team

The Project Team is responsible for planning designing, Implementation, Testing adn documenting *Rooted*

The development team acts a key stakeholder as they are responsible for ensuring the applications systems integrate properly and satistfy the agreed project requirements

## Project Constraints

The development of *Rooted* is subject to the following constraints:

### Technical Constraints

- The application will be developed using Java and JavaFX as a desktop application.
- The application should operate consistently on both Windows and macOS, as these are the primary development environments used by the project team.
- SQLite will be used for local data persistence.
- Core application functionality should remain available without an internet connection.
- The user interface should use a responsive design so that the application remains usable across different window sizes and display configurations.

### Time Constraints

- The project must be designed, implemented, tested and documented within the CAB302 teaching period and associated project milestone deadlines.
- The scope and complexity of features must therefore remain achievable within the available development timeframe.

### Data and Privacy Constraints

- User wellbeing information, including goals, habits, activities and progress history, will be stored locally using SQLite.
- The application should avoid collecting or exposing information that is not required for its core functionality.
- User wellbeing information should remain private to the local application unless additional functionality requiring data sharing is explicitly introduced and agreed upon by the project team.

### Scope Constraints

- The project is focused on encouraging positive mental wellbeing behaviours through goals, habits, activities and gamification rather than providing general healthcare functionality.
- The application will not provide mental health diagnoses, clinical treatment plans, medical advice, crisis-support services or communication with healthcare professionals, as defined within the agreed project scope.
- Features outside the agreed project scope should only be introduced where they can be completed without negatively affecting delivery of the core requirements.

### Platform Constraints

- Although the application's interface may use responsive and mobile-inspired design principles, the delivered application will remain a JavaFX desktop application rather than a native mobile application.


