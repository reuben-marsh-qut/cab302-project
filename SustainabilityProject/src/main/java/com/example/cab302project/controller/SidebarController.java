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
import java.util.Objects;

/**
 * Controls shared navigation, account actions and the user's XP summary.
 *
 * <p>The sidebar observes replacement of the session user so persisted
 * XP changes can refresh the displayed total and level.</p>
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
    private Button profileButton;

//    @FXML
//    private Button socialButton;

//    @FXML
//    private Button statsButton

    @FXML
    private Button statsButton;

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

    private static String currentPage = "home-view.fxml";

    private final LevelService levelService = new LevelService();

    /**
     * Retains the listener while this controller is active. Registration
     * uses a weak listener so discarded sidebars are not retained by
     * the shared session.
     */
    private final ChangeListener<User> sessionListener =
            (observable, previousUser, updatedUser) ->
                    onSessionUserChanged(previousUser, updatedUser);

    /**
     * Loads account details and subscribes to session user changes.
     */
    @FXML
    public void initialize() {
        loadUserDetails();
        clearLevelUpMessage();
        createAccountMenu();
        highlightActivePage();

        UserSession.getInstance()
                .userProperty()
                .addListener(new WeakChangeListener<>(sessionListener));
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

        userNameLabel.setText(displayName);
        userEmailLabel.setText(email == null ? "" : email);
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
     * Refreshes the sidebar and announces a level increase for the same user.
     *
     * @param previousUser the previous session user
     * @param updatedUser the replacement session user
     */
    private void onSessionUserChanged(
            User previousUser,
            User updatedUser
    ) {
        loadUserDetails();
        clearLevelUpMessage();

        if (previousUser == null || updatedUser == null) {
            return;
        }

        if (!Objects.equals(
                previousUser.getUserId(),
                updatedUser.getUserId()
        )) {
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
     * Clears any level-up message from an earlier session update.
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
     * @return a display name, or a fallback when no usable name is available
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

        MenuItem userSettingsItem =
                new MenuItem(
                        "User Settings"
                );

        MenuItem profileItem =
                new MenuItem(
                        "Your Profile"
                );

        userSettingsItem.setOnAction(
                event -> {
                    try {
                        openSettingsPage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        profileItem.setOnAction(
                event -> {
                    try {
                        openProfilePage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        MenuItem signOutItem = new MenuItem( "Sign Out" );


        accountMenu =
                new ContextMenu(
                        profileItem,
                        userSettingsItem,
                        signOutItem
                  );
          
        signOutItem.setOnAction(event -> {
            try {
                signOut();
            } catch (IOException exception) {
                throw new RuntimeException(
                        "Could not open the login page.",
                        exception
                );
            }
        });

        accountMenu = new ContextMenu(profileItem, userSettingsItem, signOutItem);
        accountMenu.getStyleClass().add("account-context-menu");
    }


    /**
     * Toggles the account menu above the account summary.
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
    @FXML
    public void highlightActivePage() {
        homeButton.getStyleClass().remove("sidebar-button-active");
        goalsButton.getStyleClass().remove("sidebar-button-active");
        habitsButton.getStyleClass().remove("sidebar-button-active");
        activitiesButton.getStyleClass().remove("sidebar-button-active");
        statsButton.getStyleClass().remove("sidebar-button-active");

        Button activeButton = buttonForPage(currentPage);

        if (activeButton != null) {
            activeButton.getStyleClass().add("sidebar-button-active");
        }
    }
        private Button buttonForPage(String page) {
            if (page.equals("home-view.fxml")) {
                return homeButton;
            }
            if (page.equals("goal-view.fxml")) {
                return goalsButton;
            }
            if (page.equals("habit-view.fxml")) {
                return habitsButton;
            }
            if (page.equals("activity-view.fxml")) {
                return activitiesButton;
            }
            if (page.equals("progress-view.fxml")) {
                return statsButton;
            }
            return null;
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

    @FXML
    protected void onProfileButtonClick()
            throws IOException {

        openPage(
                profileButton,
                "user-view.fxml"
        );
    }

//    @FXML
//    protected void onSocialButtonClick()
//            throws IOException {
//
//        /*
//         * There is no dedicated social page yet.
//         */
//        openPage(
//                socialButton,
//                "goal-view.fxml"
//        );
//    }

//    @FXML
//    protected void onStatsButtonClick()
//            throws IOException {
//
//        /*
//         * There is no dedicated stats page yet.
//         */
//        openPage(
//                statsButton,
//                "goal-view.fxml"
//        );
//    }
    /**
     * Opens the activity progress report.
     *
     * @throws IOException if the page cannot be loaded
     */
    @FXML
    protected void onStatsButtonClick() throws IOException {
        openPage(statsButton, "progress-view.fxml");
    }

    /**
     * Loads a page into the current scene.
     *
     * @param sourceButton the navigation button in the current scene
     * @param resource the FXML resource relative to HelloApplication
     * @throws IOException if the page cannot be loaded
     */

    private void openPage(
            Button sourceButton,
            String resource

    ) throws IOException {
        currentPage = resource;
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(resource)
        );

        Parent root = loader.load();

        hideAccountMenu();
        sourceButton.getScene().setRoot(root);
    }

    private void openSettingsPage()
            throws IOException {
        currentPage = "profile-view.fxml";
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Parent root = loader.load();

        hideAccountMenu();
        accountMenuButton.getScene().setRoot(root);
    }

    private void openProfilePage()
            throws IOException {
        currentPage = "user-view.fxml";
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("user-view.fxml")
        );

        Parent root = loader.load();

        accountMenuButton.getScene().setRoot(root);
    }

    private void signOut()
            throws IOException {
        currentPage = "home-view.fxml";
        UserSession
                .getInstance()
                .clearUserSession();

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );

        Parent root = loader.load();

        hideAccountMenu();
        UserSession.getInstance().clearUserSession();
        accountMenuButton.getScene().setRoot(root);
    }

    /**
     * Hides the account menu before navigation.
     */
    private void hideAccountMenu() {
        if (accountMenu != null) {
            accountMenu.hide();
        }
    }
}