package com.example.cab302project.controller;

import com.example.cab302project.model.User;
import com.example.cab302project.model.UserDAO;
import com.example.cab302project.model.UserManager;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML
    private Button saveProfileButton;

    @FXML
    private Button changePasswordButton;

    private UserManager userManager;
    private User currentUser;

    @FXML
    public void initialize() {

        userManager = new UserManager(new UserDAO());

        currentUser = UserSession
                .getInstance()
                .getUser();

        if (currentUser == null) {
            showProfileError(
                    "No user is currently logged in."
            );
            return;
        }

        loadUserDetails();

        configurePostcodeField();
        configureProfileChangeTracking();
        configurePasswordChangeTracking();

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

        updateSaveButtonState();
    }

    private void configurePostcodeField() {

        postcodeField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (!newValue.matches("\\d*")) {
                        postcodeField.setText(
                                newValue.replaceAll(
                                        "[^\\d]",
                                        ""
                                )
                        );
                    }

                    if (postcodeField.getText().length() > 4) {
                        postcodeField.setText(
                                postcodeField
                                        .getText()
                                        .substring(0, 4)
                        );
                    }
                }
        );
    }

    private void configureProfileChangeTracking() {

        emailField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    hideProfileMessage();
                    updateSaveButtonState();
                }
        );

        postcodeField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    hideProfileMessage();
                    updateSaveButtonState();
                }
        );
    }

    private void updateSaveButtonState() {

        if (currentUser == null) {
            saveProfileButton.setDisable(true);
            return;
        }

        String currentEmail =
                currentUser.getEmail();

        String enteredEmail =
                emailField
                        .getText()
                        .trim();

        String currentPostcode =
                String.valueOf(
                        currentUser.getPostcode()
                );

        String enteredPostcode =
                postcodeField
                        .getText()
                        .trim();

        boolean unchanged =
                currentEmail.equalsIgnoreCase(
                        enteredEmail
                )
                        &&
                        currentPostcode.equals(
                                enteredPostcode
                        );

        saveProfileButton.setDisable(
                unchanged
        );
    }

    private void configurePasswordChangeTracking() {

        currentPasswordField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            hidePasswordMessage();
                            updatePasswordButtonState();
                        }
                );

        newPasswordField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            hidePasswordMessage();
                            updatePasswordButtonState();
                        }
                );

        confirmPasswordField
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            hidePasswordMessage();
                            updatePasswordButtonState();
                        }
                );

        updatePasswordButtonState();
    }

    private void updatePasswordButtonState() {

        boolean missingField =
                currentPasswordField.getText().isBlank()
                        ||
                        newPasswordField.getText().isBlank()
                        ||
                        confirmPasswordField.getText().isBlank();

        changePasswordButton.setDisable(
                missingField
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
                emailField
                        .getText()
                        .trim();

        String postcodeText =
                postcodeField
                        .getText()
                        .trim();

        int postcode;

        try {

            postcode =
                    Integer.parseInt(
                            postcodeText
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

            showProfileError(
                    result
            );

            return;
        }

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
                currentPasswordField
                        .getText();

        String newPassword =
                newPasswordField
                        .getText();

        String confirmPassword =
                confirmPasswordField
                        .getText();

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

            showPasswordError(
                    result
            );

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

        profileMessageLabel
                .getStyleClass()
                .removeAll(
                        "form-success",
                        "form-error"
                );

        profileMessageLabel
                .getStyleClass()
                .add(
                        "form-error"
                );

        profileMessageLabel.setText(
                message
        );

        profileMessageLabel.setVisible(
                true
        );

        profileMessageLabel.setManaged(
                true
        );
    }

    private void showProfileSuccess(
            String message
    ) {

        profileMessageLabel
                .getStyleClass()
                .removeAll(
                        "form-success",
                        "form-error"
                );

        profileMessageLabel
                .getStyleClass()
                .add(
                        "form-success"
                );

        profileMessageLabel.setText(
                message
        );

        profileMessageLabel.setVisible(
                true
        );

        profileMessageLabel.setManaged(
                true
        );
    }

    private void hideProfileMessage() {

        profileMessageLabel.setVisible(
                false
        );

        profileMessageLabel.setManaged(
                false
        );
    }

    private void showPasswordError(
            String message
    ) {

        passwordMessageLabel
                .getStyleClass()
                .removeAll(
                        "form-success",
                        "form-error"
                );

        passwordMessageLabel
                .getStyleClass()
                .add(
                        "form-error"
                );

        passwordMessageLabel.setText(
                message
        );

        passwordMessageLabel.setVisible(
                true
        );

        passwordMessageLabel.setManaged(
                true
        );
    }

    private void showPasswordSuccess(
            String message
    ) {

        passwordMessageLabel
                .getStyleClass()
                .removeAll(
                        "form-success",
                        "form-error"
                );

        passwordMessageLabel
                .getStyleClass()
                .add(
                        "form-success"
                );

        passwordMessageLabel.setText(
                message
        );

        passwordMessageLabel.setVisible(
                true
        );

        passwordMessageLabel.setManaged(
                true
        );
    }

    private void hidePasswordMessage() {

        passwordMessageLabel.setVisible(
                false
        );

        passwordMessageLabel.setManaged(
                false
        );
    }
}