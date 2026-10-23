package com.example.cab302project.controller;

import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.Objects;


/**
 * Controls the user settings page
 */
public class ProfileController {

    @FXML
    public VBox bannerCardAreaThing;

    @FXML
    public FlowPane pfpPicker;

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
    private FlowPane bannerPicker;

    @FXML
    private Label profileCosmeticsLabel;

    private final IUserSettingsDAO UserSettingsDAO;

    /**
     *  creates new instance of the user settings dao
     */
    public ProfileController()
    {
        UserSettingsDAO = new UserSettingsDAO();
    }

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

        loadBanners();
        loadPFPs();

        hideProfileMessage();
        hidePasswordMessage();
    }

    private void loadUserDetails() {

        emailField.setText(
                currentUser.getEmail()
        );

        postcodeField.setText(
                String.format(
                        "%04d",
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
                String.format(
                        "%04d",
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

        if (!postcodeText.matches("\\d{4}")) {
            showProfileError(
                    "Enter a valid 4-digit postcode."
            );
            return;
        }

        int postcode = Integer.parseInt(postcodeText);

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

    private boolean checkUserLevel(int wantedLevel)
    {
        int userXP = currentUser.getUserExperience();
        int level = new LevelService().getLevel(userXP);
        if (level >= wantedLevel)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void loadBanners()
    {
        // Change the following variable upon adding new banners
        int numberOfBanners = 4;
        int count = 1;
        while (count <= numberOfBanners)
        {
            if (checkUserLevel(count))
            {
                final int bannerNumber = count;
                Image banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/UserCustomisation/banners/banner" + count + ".png")));
                ImageView bannerImageView = new ImageView(banner);
                bannerImageView.setPreserveRatio(true);
                double width = bannerCardAreaThing.getWidth();
                width = width - 20;
                bannerImageView.setFitWidth(500);

                HBox bannerHbox = new HBox();
                Button bannerButton = new Button("Save Banner" + count);
                bannerButton.getStyleClass().add("primary-button");
                bannerButton.setGraphic(bannerImageView);
                bannerButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                bannerButton.setOnAction(event -> {saveBannerCosmetics(bannerNumber);});
                bannerHbox.getChildren().add(bannerButton);
                bannerPicker.getChildren().add(bannerHbox);
            }
            count++;
        }
    }

    private void loadPFPs()
    {
        // Change the following variable upon adding new pfps
        int numberOfPFPs = 4;
        int count = 1;
        while (count <= numberOfPFPs)
        {
            if (checkUserLevel(count))
            {
                final int pfpNumber = count;
                Image pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/UserCustomisation/pfp/pfp" + count + ".png")));
                ImageView pfpImageView = new ImageView(pfp);
                pfpImageView.setPreserveRatio(true);
                double width = bannerCardAreaThing.getWidth();
                width = width - 20;
                pfpImageView.setFitWidth(225);

                HBox pfpHbox = new HBox();
                Button pfpButton = new Button("Save Profile Picture" + count);
                pfpButton.getStyleClass().add("primary-button");
                pfpButton.setGraphic(pfpImageView);
                pfpButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                pfpButton.setOnAction(event -> {savePFPCosmetics(pfpNumber);});
                pfpHbox.getChildren().add(pfpButton);
                pfpPicker.getChildren().add(pfpHbox);
            }
            count++;
        }
    }

    // my user pfp save didnt work so I refactored and fixed at the same time

    private void saveSetting(String key, int value)
    {
        for (UserSetting setting : UserSettingsDAO.getSettingsByUser(currentUser))
        {

            if (key.equals(setting.getSettingsKey()))
            {
                UserSettingsDAO.updateUserSetting(currentUser.getUserId(), key, value);
                return;

            }
        }

        UserSettingsDAO.createUserSetting(currentUser.getUserId(), key, value);
    }

    private void savePFPCosmetics(int pfpId)
    {
        saveSetting("pfpNumber", pfpId);
        showCosmeticsSuccess("Profile Picture Updated Successfully");
    }

    private void saveBannerCosmetics(int bannerId)
    {
        saveSetting("bannerNumber", bannerId);
        showCosmeticsSuccess("Profile Banner Updated Successfully");
    }

    private void showCosmeticsSuccess(
            String message
    ) {

        profileCosmeticsLabel
                .getStyleClass()
                .removeAll(
                        "form-success",
                        "form-error"
                );

        profileCosmeticsLabel
                .getStyleClass()
                .add(
                        "form-success"
                );

        profileCosmeticsLabel.setText(
                message
        );

        profileCosmeticsLabel.setVisible(
                true
        );

        profileCosmeticsLabel.setManaged(
                true
        );
    }
}