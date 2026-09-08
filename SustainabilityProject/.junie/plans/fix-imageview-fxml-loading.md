---
sessionId: session-260905-135109-zyhy
---

# Requirements

### Overview & Goals
Fix the startup / loading failure caused by `<ImageView>` containing an empty `<Image url=""/>` inside `goal-view.fxml`, allowing the JavaFX application to build and run successfully.

### Scope
- **In Scope**:
  - Fixing the `ImageView` definition in `src/main/resources/com/example/cab302project/home-view.fxml`.
  - Cleaning up commented-out imports in `goal-view.fxml`.
  - Ensuring the application launches and initializes the home view without runtime `LoadException`.
- **Out of Scope**:
  - Redesigning unrelated application views or adding new business logic in model managers.

### Functional Requirements
- `goal-view.fxml` must be loaded by `FXMLLoader` without throwing `LoadException` or `IllegalArgumentException`.
- `ImageView` in the sidebar should remain ready to display an asset when one is provided.

# Technical Design

### Current Implementation
In `src/main/resources/com/example/cab302project/home-view.fxml`, the sidebar contains:
```xml
<ImageView fitHeight="70" fitWidth="70" preserveRatio="true">
    <Image url=""/>
</ImageView>
```
When `HelloApplication.java` executes `FXMLLoader.load()`, `javafx.scene.image.Image` fails to construct an image from an empty string `""`, leading to an application launch failure.

### Key Decisions
- **Self-closing ImageView placeholder**: Replace `<ImageView><Image url=""/></ImageView>` with a self-closing `<ImageView fitHeight="70" fitWidth="70" preserveRatio="true"/>` so layout sizing is preserved while avoiding runtime instantiation errors for non-existent image URLs.

### Proposed Changes
- Modify `src/main/resources/com/example/cab302project/home-view.fxml` to remove `<Image url=""/>` and remove unnecessary commented-out import headers.

### File Structure
- `src/main/resources/com/example/cab302project/home-view.fxml`: Target FXML file to be updated.

# Testing

### Validation Approach
Verify that the FXML parses properly and that the application starts up without throwing any `javafx.fxml.LoadException`.

### Key Scenarios
- **Application Startup**: Run `HelloApplication` (or `mvn javafx:run`) and verify that the window opens displaying the sidebar with `ImageView` layout bounds intact and no runtime exceptions.
- **FXML Parsing**: Verify `goal-view.fxml` is well-formed XML and loads cleanly through `FXMLLoader`.

# Delivery Steps

###   Step 1: Fix invalid ImageView configuration in home-view.fxml
The FXML layout in `goal-view.fxml` is corrected so `ImageView` does not attempt to resolve an invalid or empty resource URL.

- Remove the invalid `<Image url=""/>` child tag from `<ImageView>` in `src/main/resources/com/example/cab302project/home-view.fxml`.
- Keep `<ImageView fitHeight="70" fitWidth="70" preserveRatio="true"/>` as a self-closing element to act as a placeholder or configure a valid `@` resource reference if an asset is added.
- Ensure the XML syntax is well-formed and conformant with JavaFX FXML schema.

###   Step 2: Clean up FXML markup and verify view loading
The FXML file is cleaned up and verified to load smoothly when starting the JavaFX application.

- Remove commented-out and redundant import statements from `src/main/resources/com/example/cab302project/home-view.fxml`.
- Verify that `HelloApplication.java` successfully loads `goal-view.fxml` and launches the JavaFX stage without `LoadException` or `IllegalArgumentException`.