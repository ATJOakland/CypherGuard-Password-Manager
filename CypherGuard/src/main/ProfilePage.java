package main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ProfilePage extends VBox {

    public ProfilePage(Stage stage, Scene previousScene) {
        // Create a layout for the profile window
        setAlignment(Pos.TOP_CENTER); // Adjusted alignment
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Logo Code
        Image logoImg = new Image(getClass().getResourceAsStream("/resources/CypherGuardLogo.png"));
        ImageView logoImageView = new ImageView(logoImg);
        StackPane logoStackPane = new StackPane();
        logoStackPane.getChildren().add(logoImageView);
        
        // Apply the negative margin to the StackPane
        VBox.setMargin(logoStackPane, new Insets(-40, 0, 0, 0)); // Adjust the value as needed
        
        // Add the logoStackPane to the VBox
        getChildren().add(logoStackPane);


        Label lblProfileTitle = new Label("Welcome to Your Profile");
        lblProfileTitle.getStyleClass().add("login-title");

        getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        // Set the title of the profile window
        stage.setTitle("CypherGuard - Profile View");

        // Labels for user info
        Label lblUsername = new Label("Username: KanyeBest123");
        Label lblEmail = new Label("Email: KanyeBest123@gmail.com");
        Label lblMembershipStatus = new Label("Membership Status: Free-Trial");
        Label lblUserPassword = new Label("Password: ****************");
        lblUsername.getStyleClass().add("profile-label");
        lblEmail.getStyleClass().add("profile-label");
        lblMembershipStatus.getStyleClass().add("profile-label");
        lblUserPassword.getStyleClass().add("profile-label");

        // Create buttons for profile actions
        Button btnChangePassword = new Button("Change Password");
        Button btnEditProfile = new Button("Edit Profile");
        btnChangePassword.getStyleClass().add("action-button");
        btnEditProfile.getStyleClass().add("action-button");

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            stage.setScene(previousScene);
            stage.setTitle("CypherGuard - Password Manager");
        });

        // Create a RadioButton for show password
        RadioButton rbtnShowPassword = new RadioButton("Show Password       ");
        rbtnShowPassword.getStyleClass().add("show-password-radio-button");
        rbtnShowPassword.setOnAction(event -> {
            if (rbtnShowPassword.isSelected()) {
                lblUserPassword.setText("Password: actual_password");
            } else {
                lblUserPassword.setText("Password: ****************");
            }
        });

         // Set a fixed width for all buttons and labels
         double buttonWidth = 150;
         double labelWidth = 225;
 
         // Set the width for each button
         btnEditProfile.setMinWidth(buttonWidth);
         btnChangePassword.setMinWidth(buttonWidth);
         btnBack.setMinWidth(buttonWidth);
 
         // Set Standard width for each profile label
         lblUsername.setMinWidth(labelWidth);
         lblEmail.setMinWidth(labelWidth);
         lblUserPassword.setMinWidth(labelWidth);
         lblMembershipStatus.setMinWidth(labelWidth);

        // Create VBox for user info
        VBox userInfoBox = new VBox(20, lblUsername, lblEmail, lblUserPassword, lblMembershipStatus);
        userInfoBox.getStyleClass().add("profile-info-container");

        // Create VBox for user actions
        VBox userActionBox = new VBox(20, rbtnShowPassword, btnChangePassword, btnEditProfile, btnBack);

        // Create an HBox to hold user info and user actions side by side
        HBox profileContentHBox = new HBox(20);
        profileContentHBox.setAlignment(Pos.CENTER);

        // Set alignment for userInfoBox and userActionBox
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userActionBox.setAlignment(Pos.CENTER_RIGHT);

        // Add userInfoBox and userActionBox to profileContentHBox
        profileContentHBox.getChildren().addAll(userInfoBox, userActionBox);

        // Add components to the root layout
        getChildren().addAll(logoImageView, lblProfileTitle, profileContentHBox);
    }
}

