package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.List;


public class UserPageController
{

    private final IUserSettingsDAO UserSettingsDAO;

    @FXML
    private VBox bannerArea;

    @FXML
    private Circle profileCircle;

    @FXML
    private HBox userInfoContainer;

    private User currentUser;

    // defaults for fallback

    private static final String defaultPFP = "/assets/UserCustomisation/pfp/pfp1.png";
    private static final String defaultBanner = "/assets/UserCustomisation/banners/banner1.png";

    public UserPageController() { UserSettingsDAO = new UserSettingsDAO(); };

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
        Integer bannerNumber = null;
        ArrayList<UserSetting> userSettings = UserSettingsDAO.getSettingsByUser(user);

        for (UserSetting setting : userSettings)
        {
            if ("userBanner".equals( setting.getSettingsKey()))
            {

                bannerNumber = setting.getSettingsValue();

            }
            else
            {
                UserSettingsDAO.createUserSetting(user.getUserId(), "bannerNumber", 2);
                bannerNumber = 2;
            }


        }

        if (user == null)
        {
            banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultBanner)));
        }
        else
        {
            banner = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/UserCustomisation/banners/banner" + bannerNumber.toString() + ".png")));
        }

        double targetWidth = bannerArea.getPrefWidth();
        double targetHeight = bannerArea.getPrefHeight();
        ImageView bannerImageView = new ImageView(banner);
        bannerImageView.setPreserveRatio(true);

        double widthScale = targetWidth / banner.getWidth();
        double heightScale = targetHeight / banner.getHeight();
        double coverScale = Math.max(widthScale, heightScale);
        bannerImageView.setFitWidth(banner.getWidth() * coverScale);
        bannerImageView.setFitHeight(banner.getHeight() * coverScale);

        Rectangle clip = new Rectangle(targetWidth, targetHeight);
        //round
        clip.setArcWidth(36);
        clip.setArcHeight(36);

        bannerArea.setClip(clip);
        bannerArea.setAlignment(Pos.CENTER);
        bannerArea.setMaxWidth(targetWidth);
        bannerArea.setMaxHeight(targetHeight);

        bannerArea.getChildren().clear();
        bannerArea.getChildren().add(bannerImageView);
    }

    private void pfp(User user)
    {
        Image pfp;
        Integer pfpNumber = null;
        ArrayList<UserSetting> userSettings = UserSettingsDAO.getSettingsByUser(user);

        for (UserSetting setting : userSettings)
        {
            if ("userPFP".equals( setting.getSettingsKey()))
            {

                pfpNumber = setting.getSettingsValue();

            }
            else
            {
                UserSettingsDAO.createUserSetting(user.getUserId(), "pfpNumber", 2);
                pfpNumber = 2;
            }


        }


        if (user == null)
        {
            pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultPFP)));
        }
        else
        {
            pfp = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/UserCustomisation/pfp/pfp" + pfpNumber.toString() + ".png")));
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
