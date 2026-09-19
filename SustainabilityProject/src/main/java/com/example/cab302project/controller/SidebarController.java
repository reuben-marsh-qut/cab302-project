package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

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
    private Button socialButton;

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

    private ContextMenu accountMenu;

    @FXML
    public void initialize() {
        loadUserDetails();
        createAccountMenu();
    }

    private void loadUserDetails() {

        User currentUser =
                UserSession
                        .getInstance()
                        .getUser();

        if (currentUser == null) {

            userNameLabel.setText("Not signed in");
            userEmailLabel.setText("");
            avatarInitialLabel.setText("?");

            return;
        }

        String email = currentUser.getEmail();

        userEmailLabel.setText(email);

        String displayName =
                createDisplayNameFromEmail(email);

        userNameLabel.setText(displayName);

        avatarInitialLabel.setText(
                displayName
                        .substring(0, 1)
                        .toUpperCase()
        );
    }

    private String createDisplayNameFromEmail(
            String email
    ) {

        if (email == null || email.isBlank()) {
            return "Rooted User";
        }

        String localPart =
                email.split("@")[0];

        localPart =
                localPart.replace(".", " ")
                        .replace("_", " ")
                        .replace("-", " ");

        String[] words =
                localPart.split("\\s+");

        StringBuilder displayName =
                new StringBuilder();

        for (String word : words) {

            if (word.isBlank()) {
                continue;
            }

            displayName
                    .append(
                            Character.toUpperCase(
                                    word.charAt(0)
                            )
                    )
                    .append(
                            word.substring(1)
                    )
                    .append(" ");
        }

        String result =
                displayName
                        .toString()
                        .trim();

        if (result.isBlank()) {
            return "Rooted User";
        }

        return result;
    }

    private void createAccountMenu() {

        MenuItem profileItem =
                new MenuItem(
                        "Your Profile"
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

        MenuItem signOutItem =
                new MenuItem(
                        "Sign Out"
                );

        signOutItem.setOnAction(
                event -> {
                    try {
                        signOut();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        accountMenu =
                new ContextMenu(
                        profileItem,
                        signOutItem
                );

        accountMenu
                .getStyleClass()
                .add(
                        "account-context-menu"
                );
    }

    @FXML
    protected void onAccountMenuClick() {

        if (accountMenu == null) {
            return;
        }

        if (accountMenu.isShowing()) {

            accountMenu.hide();
            return;
        }

        accountMenu.show(
                accountMenuButton,
                Side.TOP,
                0,
                -8
        );
    }

    @FXML
    protected void onHomeButtonClick()
            throws IOException {

        openPage(
                homeButton,
                "goal-view.fxml"
        );
    }

    @FXML
    protected void onGoalsButtonClick()
            throws IOException {

        openPage(
                goalsButton,
                "goal-view.fxml"
        );
    }

    @FXML
    protected void onHabitsButtonClick()
            throws IOException {

        /*
         * There is no dedicated habits page yet.
         * This preserves the behaviour from the old
         * NavBarController.
         */
        openPage(
                habitsButton,
                "goal-view.fxml"
        );
    }

    @FXML
    protected void onActivitiesButtonClick()
            throws IOException {

        openPage(
                activitiesButton,
                "activity-view.fxml"
        );
    }

    @FXML
    protected void onSocialButtonClick()
            throws IOException {

        /*
         * There is no dedicated social page yet.
         */
        openPage(
                socialButton,
                "goal-view.fxml"
        );
    }

    @FXML
    protected void onStatsButtonClick()
            throws IOException {

        /*
         * There is no dedicated stats page yet.
         */
        openPage(
                statsButton,
                "goal-view.fxml"
        );
    }

    private void openPage(
            Button sourceButton,
            String resource
    ) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(resource)
        );

        Parent root = loader.load();

        sourceButton.getScene().setRoot(root);
    }

    private void openProfilePage()
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("profile-view.fxml")
        );

        Parent root = loader.load();

        accountMenuButton.getScene().setRoot(root);
    }

    private void signOut()
            throws IOException {

        UserSession
                .getInstance()
                .clearUserSession();

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );

        Parent root = loader.load();

        accountMenuButton.getScene().setRoot(root);
    }
}