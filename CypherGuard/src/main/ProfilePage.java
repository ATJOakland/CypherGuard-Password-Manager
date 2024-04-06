package main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ProfilePage extends VBox{

    public ProfilePage(Stage stage, Scene previousScene){
        // Create a layout for the profile window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        Label lblProfileTitle = new Label("Welcome to Your Profile");

        getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        // Set the title of the profile window
        stage.setTitle("CypherGuard - Profile View");

        // Labels for user info
        Label lblUsername = new Label("Username: KanyeBest123");
        Label lblEmail = new Label("Email: KanyeBest123@gmail.com");
        Label lblMembershipStatus = new Label("Membership Status: Free-Trial");
        Label lblUserPassword = new Label("Password: ****************");

        // Styling for labels
        lblProfileTitle.getStyleClass().add("login-title");
        lblUsername.getStyleClass().add("profile-label");
        lblEmail.getStyleClass().add("profile-label");
        lblMembershipStatus.getStyleClass().add("profile-label");
        lblUserPassword.getStyleClass().add("profile-label");

        // Create buttons for profile actions
        Button btnChangePassword = new Button("Change Password");
        Button btnEditProfile = new Button("Edit Profile");

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            stage.setScene(previousScene);
        });

        // Create VBox for user info
        VBox userInfoBox = new VBox(20);
        userInfoBox.getChildren().addAll(lblUsername, lblEmail, lblUserPassword, lblMembershipStatus);

        // Create VBox for user actions
        VBox userActionBox = new VBox(20);
        userActionBox.getChildren().addAll(btnChangePassword, btnEditProfile, btnBack);

        // Create an HBox to hold user info and user actions side by side
        HBox profileContentHBox = new HBox(20);
        profileContentHBox.getChildren().addAll(userInfoBox, userActionBox);
        profileContentHBox.setAlignment(Pos.CENTER); // Align center

        // Add components to the root layout
        getChildren().addAll(lblProfileTitle, profileContentHBox);
    }
}
