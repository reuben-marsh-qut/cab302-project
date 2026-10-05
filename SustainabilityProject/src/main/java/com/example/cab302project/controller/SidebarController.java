package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.LevelService;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controls sidebar navigation, account actions and the user's XP display.
 *
 * <p>The sidebar observes the session so refreshed XP values are displayed
 * without requiring navigation to another page.</p>
 */
public class SidebarController {

    @FXML
    private Button homeButton;

    @FXML
    private Button goalsButton;

    @FXML
    private Button habitsButton;

    @FXML
    private Button activitiesButton;

    @FXML
    private HBox accountMenuButton;

    @FXML
    private Label avatarInitialLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private VBox progressSummary;

    @FXML
    private Label levelLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private Label levelUpLabel;

    private ContextMenu accountMenu;

    private final LevelService levelService = new LevelService();

    /*
     * Keep a strong reference to the underlying listener while this
     * controller is in use. The session holds only a weak reference,
     * allowing discarded sidebars to be garbage-collected.
     */
    private final ChangeListener<User> sessionListener =
            (observable, previousUser, updatedUser) ->
                    onSessionUserChanged(previousUser, updatedUser);

    /**
     * Initialises the account menu, user details and session observation.
     */
    @FXML
    public void initialize() {
        loadUserDetails();
        clearLevelUpMessage();
        createAccountMenu();

        UserSession.getInstance()
                .userProperty()
                .addListener(new WeakChangeListener<>(sessionListener));
    }

    /**
     * Refreshes the sidebar and announces a level increase for the same user.
     *
     * <p>Signing in or switching accounts does not produce a level-up
     * message.</p>
     *
     * @param previousUser the previous session user, possibly null
     * @param updatedUser the refreshed session user, possibly null
     */
    private void onSessionUserChanged(User previousUser, User updatedUser) {
        loadUserDetails();
        clearLevelUpMessage();

        if (previousUser == null || updatedUser == null) {
            return;
        }

        if (previousUser.getUserId() != updatedUser.getUserId()) {
            return;
        }

        int previousLevel = levelService.getLevel(
                previousUser.getUserExperience()
        );

        int updatedLevel = levelService.getLevel(
                updatedUser.getUserExperience()
        );

        if (updatedLevel > previousLevel) {
            levelUpLabel.setText(
                    "Level up! You reached level " + updatedLevel + "."
            );
            levelUpLabel.setVisible(true);
            levelUpLabel.setManaged(true);
        }
    }

    /**
     * Displays the current user's identity, total XP and calculated level.
     */
    private void loadUserDetails() {
        User currentUser = UserSession.getInstance().getUser();

        if (currentUser == null) {
            userNameLabel.setText("Not signed in");
            userEmailLabel.setText("");
            avatarInitialLabel.setText("?");

            levelLabel.setText("");
            xpLabel.setText("");
            progressSummary.setVisible(false);
            progressSummary.setManaged(false);
            return;
        }

        String email = currentUser.getEmail();
        String displayName = createDisplayNameFromEmail(email);

        userEmailLabel.setText(email == null ? "" : email);
        userNameLabel.setText(displayName);
        avatarInitialLabel.setText(
                displayName.substring(0, 1).toUpperCase()
        );

        int totalXp = currentUser.getUserExperience();

        levelLabel.setText("Level " + levelService.getLevel(totalXp));
        xpLabel.setText(totalXp + " XP total");

        progressSummary.setVisible(true);
        progressSummary.setManaged(true);
    }

    /**
     * Clears and hides the level-up notification.
     */
    private void clearLevelUpMessage() {
        levelUpLabel.setText("");
        levelUpLabel.setVisible(false);
        levelUpLabel.setManaged(false);
    }

    /**
     * Creates a readable display name from the local part of an email.
     *
     * @param email the user's email address
     * @return a display name, or a fallback when no usable name is present
     */
    private String createDisplayNameFromEmail(String email) {
        if (email == null || email.isBlank()) {
            return "Rooted User";
        }

        String localPart = email.split("@", 2)[0]
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ");

        StringBuilder displayName = new StringBuilder();

        for (String word : localPart.split("\\s+")) {
            if (word.isBlank()) {
                continue;
            }

            displayName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        String result = displayName.toString().trim();
        return result.isBlank() ? "Rooted User" : result;
    }

    /**
     * Creates the profile and sign-out account actions.
     */
    private void createAccountMenu() {
        MenuItem profileItem = new MenuItem("Your Profile");
        profileItem.setOnAction(event -> {
            try {
                openProfilePage();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });

        MenuItem signOutItem = new MenuItem("Sign Out");
        signOutItem.setOnAction(event -> {
            try {
                signOut();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });

        accountMenu = new ContextMenu(profileItem, signOutItem);
        accountMenu.getStyleClass().add("account-context-menu");
    }

    /**
     * Toggles the account menu above the account button.
     */
    @FXML
    protected void onAccountMenuClick() {
        if (accountMenu == null) {
            return;
        }

        if (accountMenu.isShowing()) {
            accountMenu.hide();
            return;
        }

        accountMenu.show(accountMenuButton, Side.TOP, 0, -8);
    }

    /**
     * Opens the home page.
     *
     * @throws IOException if the page cannot be loaded
     */
    @FXML
    protected void onHomeButtonClick() throws IOException {
        openPage(homeButton, "home-view.fxml");
    }

    /**
     * Opens the goals page.
     *
     * @throws IOException if the page cannot be loaded
     */
    @FXML
    protected void onGoalsButtonClick() throws IOException {
        openPage(goalsButton, "goal-view.fxml");
    }

    /**
     * Opens the habits page.
     *
     * @throws IOException if the page cannot be loaded
     */
    @FXML
    protected void onHabitsButtonClick() throws IOException {
        openPage(habitsButton, "habit-view.fxml");
    }

    /**
     * Opens the activities page.
     *
     * @throws IOException if the page cannot be loaded
     */
    @FXML
    protected void onActivitiesButtonClick() throws IOException {
        openPage(activitiesButton, "activity-view.fxml");
    }

    /**
     * Loads a page into the current scene.
     *
     * @param sourceButton the navigation button in the current scene
     * @param resource the FXML resource to load
     * @throws IOException if the page cannot be loaded
     */
    private void openPage(Button sourceButton, String resource)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(resource)
        );

        Parent root = loader.load();
        accountMenu.hide();
        sourceButton.getScene().setRoot(root);
    }

    /**
     * Opens the current user's profile.
     *
     * @throws IOException if the profile page cannot be loaded
     */
    private void openProfilePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Parent root = loader.load();
        accountMenu.hide();
        accountMenuButton.getScene().setRoot(root);
    }

    /**
     * Loads the login page and clears the current session.
     *
     * @throws IOException if the login page cannot be loaded
     */
    private void signOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );

        Parent root = loader.load();

        accountMenu.hide();
        UserSession.getInstance().clearUserSession();
        accountMenuButton.getScene().setRoot(root);
    }
}