---
sessionId: session-260908-094231-hhpv
---

# Requirements

### Overview & Goals
The goal is to ensure that Maven's Surefire plugin properly discovers and executes test cases during the build, producing test reports in `target/surefire-reports/`, and that CI workflows correctly capture and publish test results.

### Scope
- **In Scope:**
  - Reorganizing the test source folder structure from `SustainabilityProject/src/test/` to the standard Maven directory `SustainabilityProject/src/test/java/`.
  - Updating `.github/workflows/maven.yml` to correctly target the Maven project directory and fix the report path typo.
  - Cleaning up POM dependencies in `SustainabilityProject/pom.xml`.
- **Out of Scope:**
  - Modifying application domain logic or rewriting existing test assertions.

### User Stories
- As a developer, I want my unit tests to be executed when running `mvn test` so that test reports are generated in `target/surefire-reports`.
- As a CI system, I want GitHub Actions to run tests in the proper module directory and locate Surefire report XMLs so that test results are reported accurately.

### Functional Requirements
- Executing `mvn test` in `SustainabilityProject` must find and execute all test classes (`ActivityManagerTest`, `ActivityTest`, `GoalManagerTest`, `GoalTest`, `HabitManagerTest`, `HabitTest`, `UserManagerTest`).
- Maven must create `SustainabilityProject/target/surefire-reports/` containing JUnit report XML files (e.g., `TEST-ActivityManagerTest.xml`).
- GitHub Actions workflow must successfully run tests and publish report data without missing file errors.

# Technical Design

### Current Implementation
- Unit test files currently reside in `SustainabilityProject/src/test/` rather than the Maven standard `SustainabilityProject/src/test/java/`.
- `SustainabilityProject/pom.xml` has `maven-surefire-plugin` listed in `<dependencies>` in addition to `<build><plugins>`.
- `.github/workflows/maven.yml` executes Maven commands in the workspace root instead of `SustainabilityProject/`, and references a misspelled report path `SustainablityProject/target/surefire-reports/*.xml`.

### Key Decisions
- **Follow Standard Maven Directory Structure:** Rather than configuring `<testSourceDirectory>` in POM, relocate test files to `src/test/java/` to adhere to standard conventions and maintain IDE / CI tool compatibility.
- **Workflow Directory Scoping:** Use `working-directory: SustainabilityProject` in `.github/workflows/maven.yml` for all Maven steps.

### Proposed Changes
1. **Directory Relocation:**
   - Move test classes from `SustainabilityProject/src/test/*.java` to `SustainabilityProject/src/test/java/`.
2. **POM Cleanup (`SustainabilityProject/pom.xml`):**
   - Remove `<dependency>` entry for `maven-surefire-plugin` under `<dependencies>`.
3. **Workflow Fix (`.github/workflows/maven.yml`):**
   - Set `working-directory: SustainabilityProject` for the Maven build and test steps.
   - Fix report path typo to `SustainabilityProject/target/surefire-reports/*.xml`.

### File Structure Changes
```
SustainabilityProject/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    └── test/
        └── java/                      <-- Move test files here
            ├── ActivityManagerTest.java
            ├── ActivityTest.java
            ├── GoalManagerTest.java
            ├── GoalTest.java
            ├── HabitManagerTest.java
            ├── HabitTest.java
            └── UserManagerTest.java
```

# Testing

### Validation Approach
- Verify test compilation and discovery using Maven command-line tool.
- Verify directory generation and presence of XML report files in `target/surefire-reports/`.

### Key Scenarios
1. **Maven Test Execution:**
   - Run `mvn clean test` from within `SustainabilityProject/`.
   - Verify all test classes are detected and executed.
   - Verify `SustainabilityProject/target/surefire-reports/` contains corresponding `TEST-*.xml` report files.

2. **CI Workflow Verification:**
   - Verify that test paths specified in `.github/workflows/maven.yml` match the actual generated file paths.

# Delivery Steps

###   Step 1: Restructure test files to standard Maven layout
Move test files to the standard Maven directory structure so that the Surefire plugin detects and runs unit tests.

- Move all test classes from `SustainabilityProject/src/test/` to `SustainabilityProject/src/test/java/`.
- Ensure test files are properly compiled when running `mvn test-compile`.
- Verify test execution with `mvn test` and confirm that `SustainabilityProject/target/surefire-reports/` is created containing test output XML and TXT files.

###   Step 2: Fix GitHub Actions workflow and Maven POM configuration
Fix the working directory and artifact paths in `.github/workflows/maven.yml` and clean up dependencies in `SustainabilityProject/pom.xml`.

- Update `.github/workflows/maven.yml` to execute Maven commands inside the `SustainabilityProject` directory (using `working-directory: SustainabilityProject` or `--file SustainabilityProject/pom.xml`).
- Correct the typo in `.github/workflows/maven.yml` line 47 from `SustainablityProject` to `SustainabilityProject`.
- Remove the redundant `maven-surefire-plugin` dependency from `<dependencies>` in `SustainabilityProject/pom.xml` since it is already configured in `<build><plugins>`.