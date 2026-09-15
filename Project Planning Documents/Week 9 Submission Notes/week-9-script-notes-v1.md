CAB302 Week 9 Submission Script
Notes:
Structure:
•	Brief introduction: project purpose and benefits
•	Overview of user stories (prioritisation, estimate, acceptance criteria) – Agile planning
•	Show planning artefacts (release plan, sprint plan, revisions) – Agile planning
•	Low fidelity UI prototypes (if created) – Agile planning
•	Demo the functional prototype (JavaFX UI + database persistence) - OOD
•	Show PM tool usage (boards, tasks, assignments) – Agile planning
•	Show version control workflow (commits, branches, authorship) – Version Control
•	Walk through relevant Java code - OOD
o	JavaFX UI structure
o	Persistence layer (DB access, DAOs)
o	OO design elements
•	Demonstrate the test suite - TDD
o	Explain test purpose
o	Show Red-Green-Refactor evidence
o	Run tests

Overview of user stories (prioritisation, estimate, acceptance criteria) – Agile planning
•	We have generated N user stories, all in the typical format (As a … I want to … so I can …)
•	We have attributed each user story realistic time estimates. In practice, we have found that user stories have taken approximately an hour longer than the given estimates, but they provide a valuable gauge regardless.
•	US have been prioritised as Must have, could have or should have based on how important they are to application functionality.
•	As evidenced by files in our US directory, we have gone through a process of iterative User story planning. Each member generated a set of user stories, which were then condensed assessed for prioritisation and time estimation as a group.
o	Give an example of a US which was common so was included in the final US



Show planning artefacts (release plan, sprint plan, revisions) – Agile planning
•	The prioritisation of US impacted sprint planning. This is because the higher prioritised ‘must have’ US have been the ones implemented first for the initial Week 9 release date.
•	From the release plan, which details US deadlines in terms of release dates, we have generated weekly sprint plans.
o	As can be seen when comparing the user stories present in sprint 1 compared to sprint 2, there is some overlap in the account and goals epics. This is because the acceptance criteria for these US were not met in the first sprint. As such, the second sprint was updated to include them.
•	In turns of implemented agile practices, our sprints run weekly, evidenced by the three of them and we have two weekly meetings, a ‘stand-up’ and a longer meeting, where we plan for the coming sprint. We have also developed sprint retrospectives for the first two sprints, evidenced here.
•	More on sprint plan revisions?

Low fidelity UI prototypes (if created) – Agile planning
•	Further evidence of revisions in our application come in the form of our UI prototype revisions. A group member first developed these low fidelity pen-and-paper UI mock ups. Based on iterative feedback they then developed initial medium-fidelity prototypes. Finally, they developed final high fidelity prototypes of multiple pages which we have used in development of the application.

Demo the functional prototype (JavaFX UI + database persistence) - OOD
•	How does application reflect US & does it meet requirements? – show example US side-by-side with application (to demonstrate version control – iterative updating to reflect user stories)
o	give a few examples of must-have requirements and how they have been implemented (e.g. log in, register, goal setting)
	note how goal setting demonstrates data persistence
o	Demonstrate how register and login requirements have been implemented at ‘highest quality standard’ because a) there is password length control and b) password stored as hash




Show PM tool usage (boards, tasks, assignments) – Agile planning
•	We have used Jira as our project management tool. A justification for this decision is located in our Planning Documents Directory.
•	In our Jira space, we have created tickets for each user story and additional tasks, like recording this video. They are grouped by sprint.
•	If they are relevant to the current sprint, they appear in ‘To Do’, ‘In Progress’, ‘In Review’ or ‘Done’. User stories for future sprints are in the ‘Product backlog’
•	Admittedly, the group has only recently begun using the PM tool meaningfully throughout the sprints. For the first two sprints, tickets were mostly only updated at the end of the sprint.

Show version control workflow (commits, branches, authorship) – Version Control
•	Show PR (closed) to demonstrate branching from the master branch and back into it continuously throughout sprint cycles
o	Iterative updates to satisfy US criteria are also evidenced by comments on PRs which have since been updated and approved
•	The commits to the master branch reflect progress towards the user stories
•	We have a document outlining the PR process to ensure we have a consistent and structured approach to creating and merging PRs
•	Evidenced by the naming of our commits, you can see how our commits a) relate a single piece of functionality and b) relate to their corresponding user story
o	For example Xcommit relates to Y feature, which was important in the implementation of Z US
•	Need more for this section?

Walk through relevant Java code - OOD
o	JavaFX UI structure
o	Persistence layer (DB access, DAOs)
o	OO design elements
•	Next, lets have a brief walk through of the codebase to showcase our code structure. We have used:
o	MVC for JavaFX UI
o	Singleton class instances for database access and for storing the current user session information
•	We have upheld good code quality practices, including:
o	Consistent naming conventions
o	OO design:
	Give examples of these things (poly, incapsulation, inheritance, abstraction)


Demonstrate the test suite - TDD
o	Explain test purpose
o	Show Red-Green-Refactor evidence
o	Run tests
•	Tests were first generated to test functionality of object and object manager classes. This ensures that functionality implemented in these classes meets the requirements and avoid unnecessary complexity or functionality.
•	How to show RGR evidence
