package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import java.util.List;


public class UserPageController
{

    @FXML
    private HBox bannerArea;

    @FXML
    private Circle profileCircle;

    @FXML
    private HBox userInfoContainer;

    private User currentUser;

    // defaults for fallback

    private static final String defaultPFP = "assets/UserCustomisation/pfp/pfp1.png";
    private static final String defaultBanner = "assets/UserCustomisation/pfp/banner/banner1.png";

    private void syncUserPage()
    {
        if (currentUser == null)
        {
            return;
        }
        else
        {
            // banner command
            banner(currentUser);
            // profile picture command
            // profile details command
        }
    }

    private void banner(User user)
    {
        Image banner;

        if (currentUser == null)
        {
            banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultBanner)));
        }
        else
        {
            banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/UserCustomisation/pfp/banner/banner" + user.bannerNumber + ".png")));
        }
        ImageView bannerImageView = new ImageView(banner);
        bannerImageView.setPreserveRatio(false);
        bannerImageView.fitHeightProperty().bind(bannerArea.heightProperty());
        bannerImageView.fitWidthProperty().bind(bannerArea.widthProperty());

        bannerArea.getChildren().clear();
        bannerArea.getChildren().add(bannerImageView);
    }



    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        syncUserPage();
    }

}
