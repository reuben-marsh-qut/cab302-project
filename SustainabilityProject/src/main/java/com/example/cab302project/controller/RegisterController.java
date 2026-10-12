package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.IUserDAO;
import com.example.cab302project.model.UserDAO;
import com.example.cab302project.model.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

        String postcodeText = postcodeField.getText().trim();

        if (!postcodeText.matches("\\d{4}")) {
            showError("Enter a valid 4-digit postcode.");
            return;
        }

        int postcode = Integer.parseInt(postcodeText);

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

        Parent root = loader.load();

        emailField.getScene().setRoot(root);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
