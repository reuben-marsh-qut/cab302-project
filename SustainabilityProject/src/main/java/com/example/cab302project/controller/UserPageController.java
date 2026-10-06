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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;

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

    private static final String defaultPFP = "/assets/UserCustomisation/pfp/pfp1.png";
    private static final String defaultBanner = "/assets/UserCustomisation/banners/banner1.png";

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
            pfp(currentUser);
            // profile details command
            userInfo(currentUser);

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
           // banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/UserCustomisation/banner" + user.bannerNumber + ".png")));
            //temp fix
            banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultBanner)));
        }
        ImageView bannerImageView = new ImageView(banner);
        bannerImageView.setPreserveRatio(true);
        bannerImageView.setFitHeight(50);
        bannerImageView.setFitWidth(300);


        Rectangle clip = new Rectangle(300, 50);
        clip.widthProperty().bind(bannerArea.widthProperty());
        clip.heightProperty().bind(bannerArea.heightProperty());
        bannerArea.setClip(clip);

        bannerArea.setMaxHeight(50);
        bannerArea.setMaxWidth(300);

        bannerArea.getChildren().clear();
        bannerArea.getChildren().add(bannerImageView);
    }

    private void pfp(User user)
    {
        Image pfp;

        if (currentUser == null)
        {
            pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultPFP)));
        }
        else
        {
            // pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/UserCustomisation/pfp" + user.pfpNumber + ".png")));
            //temp fix
            pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultPFP)));
        }
        ImagePattern pfpImageView = new ImagePattern(pfp);
        profileCircle.setFill(pfpImageView);
    }

    private void userInfo(User user)
    {

        // User's name
        // Apparently we don't even have that so it'll be the email for now

        Label nameLabel = new Label("Name");
        nameLabel.getStyleClass().add("item-category");
        String email = currentUser.getEmail();
        Label nameValue = new Label("email: " + email);
        nameValue.getStyleClass().add("item-header");

        // Streak except we don't have it implemented in the table or user class yet


        // add to area
        userInfoContainer.getChildren().addAll(nameLabel, nameValue);
    }

    @FXML
    private void initialize() {
        currentUser = UserSession.getInstance().getUser();
        syncUserPage();
    }

}
