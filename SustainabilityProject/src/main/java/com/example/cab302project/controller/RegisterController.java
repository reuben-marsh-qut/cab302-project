package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.IUserDAO;
import com.example.cab302project.model.UserDAO;
import com.example.cab302project.model.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField postcodeField;

    @FXML
    private Label errorLabel;

    private final UserManager userManager;

    public RegisterController() {
        IUserDAO userDAO = new UserDAO();
        userManager = new UserManager(userDAO);
    }

    @FXML
    private void handleRegister() {
        String password = passwordField.getText();
        if (!password.equals(confirmPasswordField.getText())) {
            showError("Passwords do not match.");
            return;
        }

        int postcode;
        try {
            postcode = Integer.parseInt(postcodeField.getText());
        } catch (NumberFormatException e) {
            showError("Enter a valid postcode.");
            return;
        }

        String error = userManager.register(emailField.getText(), password, postcode);
        if (error != null) {
            showError(error);
            return;
        }

        try {
            openLoginPage();
        } catch (IOException e) {
            showError("Unable to open the login page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        try {
            openLoginPage();
        } catch (IOException e) {
            showError("Unable to open the login page.");
            e.printStackTrace();
        }
    }

    private void openLoginPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );
        Scene scene = new Scene(
                loader.load(),
                HelloApplication.WIDTH,
                HelloApplication.HEIGHT
        );
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
