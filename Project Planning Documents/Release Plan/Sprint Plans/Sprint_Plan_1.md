# Rooted — Sprint 1 Plan

## Sprint Details

**Sprint:** Sprint 1
**Length:** 1 Week
**Sprint Goal:**
Create the basic application so a user can create an account, log in, choose a goal and save it.

## User Stories for Sprint 1

* **US-01 — Register an Account**
* **US-02 — Log In**
* **US-05 — Browse Goal Templates**
* **US-06 — Create a Long-Term Goal**
* **US-32 — Persist My Progress (initial implementation)**

# Work Groups

## Group 1 — Accounts and Login

### User Stories

* US-01 — Register an Account
* US-02 — Log In

### Main Tasks

* Create account/user model.
* Create registration page.
* Create login page.
* Validate usernames and passwords.
* Save user accounts.
* Load the correct account after login.
* Test registration and login.

### Assigned To

* Javier
* Patrick

## Group 2 — Goals and UI

### User Stories

* US-05 — Browse Goal Templates
* US-06 — Create a Long-Term Goal

### Main Tasks

* Create goal and goal-template models.
* Create some starting Mind, Body and Social goal templates.
* Build the goal-template screen.
* Build the goal creation screen.
* Allow target and deadline selection.
* Validate goal information.
* Display created goals.

### Assigned To

* Reuben
* Sujhav

## Group 3 — Database and Integration

### User Stories

* US-32 — Persist My Progress (initial implementation)

### Main Tasks

* Set up SQLite.
* Create the first database tables.
* Save user data.
* Save goal data.
* Link goals to users.
* Load saved user and goal data when the application starts.
* Establish the persistence structure that can later be extended to habits, activities, progress and history.
* Help connect the account and goal work together.

### Assigned To

* Seb
* Angus

# Shared Team Tasks

These tasks may need input from everyone:

* Set up the Java/JavaFX project.
* Agree on basic structure.
* Make sure everyone's code works together.
* Review code before merging.

# Sprint 1 Demo

By the end of the Sprint, we want to be able to demonstrate:

1. Open App.
2. Register an account.
3. Log in.
4. View goal templates.
5. Choose a goal.
6. Create the goal.
7. Close the application.
8. Reopen it.
9. Log back in.
10. See the saved goal.

# Definition of Done

A User Story is Done when:

* The feature works.
* Its acceptance criteria are met.
* It has been tested.
* It has been merged into the team project.
* It does not have any known major bugs.
* It works with the other completed parts of the application.

Where a User Story has acceptance criteria that depend on features planned for a later Sprint, the implemented portion should be identified and the remaining criteria should stay open until they can be completed.

### Sprint Review

* Demonstrate what was completed.
* Check whether the Sprint Goal was achieved.
* Identify anything unfinished.
* Update the Product Backlog if needed.
* Confirm whether US-32 requires additional work in later Sprints as habits, activities, progress and history are introduced.
