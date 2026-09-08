---
sessionId: session-260905-133829-1ns7
---

# Requirements

### Overview & Goals
When running the application via `Launcher.java`, `HelloApplication` loads `goal-view.fxml`. However, the label defined in `goal-view.fxml` (`"Select a contact to view or edit."`) is not visible on the screen. The goal is to resolve this issue so that the label is positioned correctly within the layout and renders as expected when the application is launched.

### Scope
- **In Scope**:
  - Fix the layout structure in `src/main/resources/com/example/cab302project/home-view.fxml` so that the `Label` is placed in an active layout region of `BorderPane`.
  - Validate that launching `Launcher.java` displays the label properly in the center of the window.
- **Out of Scope**:
  - Changes to underlying models or database DAOs (`Activity`, `Goal`, `Habit`, etc.).
  - Creating additional UI views or controllers.

### User Stories
- As a developer/user running the application, I want the home view to properly display the placeholder label so that I can see the initial home screen interface.

### Functional Requirements
- When `Launcher.main()` runs, `HelloApplication` loads `goal-view.fxml`.
- The label with text `"Select a contact to view or edit."` must appear centered within the main window.

# Technical Design

### Current Implementation
- `Launcher.java` launches `HelloApplication.class`.
- `HelloApplication.java` creates a `Scene` by loading `goal-view.fxml`:
  ```java
  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("goal-view.fxml"));
  Scene scene = new Scene(fxmlLoader.load(), 732*2, 412*2);
  ```
- In `src/main/resources/com/example/cab302project/home-view.fxml`:
  ```xml
  <BorderPane xmlns="http://javafx.com/javafx"
              xmlns:fx="http://javafx.com/fxml"
              fx:controller="com.example.cab302project.controller.GoalController"
              prefHeight="412.0" prefWidth="732.0">

      <padding>
          <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
      </padding>

      <Label text="Select a contact to view or edit." />

  </BorderPane>
  ```
- **Root Cause**: `BorderPane` inherits `@DefaultProperty("children")` from `Pane`. When `<Label>` is placed directly as a child of `<BorderPane>`, FXML adds it directly to `getChildren()`. However, `BorderPane`'s layout mechanism only lays out child nodes that are assigned to its five positional properties (`top`, `bottom`, `left`, `right`, `center`). Direct children not assigned to any position are not managed by `BorderPane` layout and thus are rendered with zero size / not displayed.

### Key Decisions
- **Wrap `Label` in `<center>` tag**: Place `<Label text="Select a contact to view or edit." />` inside `<center>` so `BorderPane` positions and sizes it in the center area.

### Proposed Changes
Update `src/main/resources/com/example/cab302project/home-view.fxml` to:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.geometry.Insets?>

<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.example.cab302project.controller.GoalController"
            prefHeight="412.0" prefWidth="732.0">

    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>

    <center>
        <Label text="Select a contact to view or edit."/>
    </center>

</BorderPane>
```

### File Structure
- **Modified**:
  - `src/main/resources/com/example/cab302project/home-view.fxml`
- **Referenced**:
  - `src/main/java/com/example/cab302project/Launcher.java`
  - `src/main/java/com/example/cab302project/HelloApplication.java`
  - `src/main/java/com/example/cab302project/controller/HomeController.java`

# Testing

### Validation Approach
- Verify FXML parsing of `goal-view.fxml` to confirm the root `BorderPane` contains the `Label` in its `center` property.
- Run `Launcher` to visually and functionally confirm that the window opens with the label displayed.

### Key Scenarios
- **Application Startup**: Executing `Launcher.main()` loads the window with dimensions `1464x824` containing the centered text `"Select a contact to view or edit."`.

### Regressions to Verify
- Ensure no `LoadException` or XML parsing errors are thrown when `FXMLLoader.load()` is executed.

# Delivery Steps

###   Step 1: Update home-view.fxml with BorderPane center position
The label in `goal-view.fxml` is properly assigned to the center slot of `BorderPane`.

- Modify `src/main/resources/com/example/cab302project/home-view.fxml` to wrap the `<Label text="Select a contact to view or edit." />` element inside a `<center>` tag within `<BorderPane>`.
- Clean up any unused imports or redundant whitespace in `goal-view.fxml` to ensure valid FXML structure.

###   Step 2: Verify JavaFX scene rendering and application launch
The home view renders the centered label successfully when starting the application.

- Verify that `FXMLLoader` loads `goal-view.fxml` correctly in `HelloApplication.java`.
- Launch the application via `Launcher.java` and confirm that the label `"Select a contact to view or edit."` is clearly visible and centered in the window.