package com.example.cab302project.controller;

import com.example.cab302project.model.User;
import com.example.cab302project.model.UserDAO;
import com.example.cab302project.model.UserManager;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ProfileController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField postcodeField;

    @FXML
    private Label profileMessageLabel;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label passwordMessageLabel;

    private UserManager userManager;
    private User currentUser;

    @FXML
    public void initialize() {

        userManager = new UserManager(new UserDAO());

        currentUser = UserSession
                .getInstance()
                .getUser();

        if (currentUser == null) {
            showProfileError("No user is currently logged in.");
            return;
        }

        loadUserDetails();

        hideProfileMessage();
        hidePasswordMessage();
    }

    private void loadUserDetails() {

        emailField.setText(
                currentUser.getEmail()
        );

        postcodeField.setText(
                String.valueOf(
                        currentUser.getPostcode()
                )
        );
    }

    @FXML
    protected void onSaveProfileClick() {

        hideProfileMessage();

        if (currentUser == null) {
            showProfileError(
                    "No user is currently logged in."
            );
            return;
        }

        String email =
                emailField.getText();

        String postcodeText =
                postcodeField.getText();

        int postcode;

        try {

            postcode = Integer.parseInt(
                    postcodeText.trim()
            );

        } catch (NumberFormatException e) {

            showProfileError(
                    "Enter a valid postcode."
            );

            return;
        }

        String result =
                userManager.updateProfile(
                        currentUser,
                        email,
                        postcode
                );

        if (result != null) {

            showProfileError(result);

            return;
        }

        /*
         * currentUser has now been updated by
         * UserManager, so refresh the fields in
         * case values such as email were normalised.
         */
        loadUserDetails();

        showProfileSuccess(
                "Profile updated successfully."
        );
    }

    @FXML
    protected void onChangePasswordClick() {

        hidePasswordMessage();

        if (currentUser == null) {

            showPasswordError(
                    "No user is currently logged in."
            );

            return;
        }

        String currentPassword =
                currentPasswordField.getText();

        String newPassword =
                newPasswordField.getText();

        String confirmPassword =
                confirmPasswordField.getText();

        if (currentPassword == null
                || currentPassword.isBlank()) {

            showPasswordError(
                    "Enter your current password."
            );

            return;
        }

        if (newPassword == null
                || newPassword.isBlank()) {

            showPasswordError(
                    "Enter a new password."
            );

            return;
        }

        if (!newPassword.equals(
                confirmPassword
        )) {

            showPasswordError(
                    "New passwords do not match."
            );

            return;
        }

        String result =
                userManager.changePassword(
                        currentUser,
                        currentPassword,
                        newPassword
                );

        if (result != null) {

            showPasswordError(result);

            return;
        }

        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();

        showPasswordSuccess(
                "Password changed successfully."
        );
    }

    private void showProfileError(
            String message
    ) {

        profileMessageLabel.setText(message);

        profileMessageLabel.setStyle(
                "-fx-text-fill: #b00020;"
        );

        profileMessageLabel.setVisible(true);
        profileMessageLabel.setManaged(true);
    }

    private void showProfileSuccess(
            String message
    ) {

        profileMessageLabel.setText(message);

        profileMessageLabel.setStyle(
                "-fx-text-fill: #18752c;"
        );

        profileMessageLabel.setVisible(true);
        profileMessageLabel.setManaged(true);
    }

    private void hideProfileMessage() {

        profileMessageLabel.setVisible(false);
        profileMessageLabel.setManaged(false);
    }

    private void showPasswordError(
            String message
    ) {

        passwordMessageLabel.setText(message);

        passwordMessageLabel.setStyle(
                "-fx-text-fill: #b00020;"
        );

        passwordMessageLabel.setVisible(true);
        passwordMessageLabel.setManaged(true);
    }

    private void showPasswordSuccess(
            String message
    ) {

        passwordMessageLabel.setText(message);

        passwordMessageLabel.setStyle(
                "-fx-text-fill: #18752c;"
        );

        passwordMessageLabel.setVisible(true);
        passwordMessageLabel.setManaged(true);
    }

    private void hidePasswordMessage() {

        passwordMessageLabel.setVisible(false);
        passwordMessageLabel.setManaged(false);
    }
}