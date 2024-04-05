package main;

import javax.swing.text.View;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPane extends GridPane {
    private Stage previousStage;

    public MainPane(Stage previousStage) {

        this.previousStage = previousStage;
        // Center vertically
        setAlignment(Pos.CENTER);

        // Logo Code
        Image logoImg = new Image(getClass().getResourceAsStream("/resources/CypherGuardLogo.png"));
        ImageView logoImageView = new ImageView(logoImg);
        add(logoImageView, 0, 0);

        // Center the logoImageView
        setHalignment(logoImageView, HPos.CENTER);
        setValignment(logoImageView, VPos.CENTER);

        // Buttons
        Button btnLogin = new Button("Login/Create an Account");
        Button btnViewPasswords = new Button("Save or View Passwords");
        Button btnSettings = new Button("Settings");
        Button btnProfile = new Button("View Profile");
        Button btnClose = new Button("Close");
        
        // CSS Styling for buttons
        btnLogin.getStyleClass().add("login-button");
        btnViewPasswords.getStyleClass().add("action-button");
        btnSettings.getStyleClass().add("action-button");
        btnClose.getStyleClass().add("close-button");
        btnProfile.getStyleClass().add("action-button");

        // Button actions
        btnClose.setOnAction(e -> Platform.exit());

        // Login Page
        btnLogin.setOnAction(e -> {
            UserAuthentication userAuthentication = new UserAuthentication(previousStage, previousStage.getScene());
            Scene userAuthenticationScene = new Scene(userAuthentication, 1200, 720);
            previousStage.setScene(userAuthenticationScene);
        });        

        // Profile Page
        btnProfile.setOnAction(e -> {
            // Hide the current stage
            Stage currentStage = (Stage) btnProfile.getScene().getWindow();
            currentStage.hide();
        
            // Create a new stage for the profile window
            Stage profileStage = new Stage();
        
            // Icon
            Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
            profileStage.getIcons().add(icon);
        
            // Create a layout for the profile window
            VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
        
            // Create a scene and set it to the profile stage
            Scene profileScene = new Scene(root, 600, 400);
            profileStage.setScene(profileScene);
        
            // Load CSS styles
            profileScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        
            // Set the title of the profile window
            profileStage.setTitle("CypherGuard - Profile View");
        
            // Labels for title and user info
            Label lblProfileTitle = new Label("Welcome to Your Profile");
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

            // Radio Button for show Password
            RadioButton rbtnShowPassword = new RadioButton("Show Password       ");
            rbtnShowPassword.getStyleClass().add("show-password-radio-button");
        
            // Set styles for buttons
            btnChangePassword.getStyleClass().add("action-button");
            btnEditProfile.getStyleClass().add("action-button");
        
            // Create a back button
            Button btnBack = new Button("Back");
            btnBack.getStyleClass().add("back-button");
            btnBack.setOnAction(backEvent -> {
                // Close the profile stage
                profileStage.close();
        
                // Show the previous stage
                currentStage.show();
            });

            // Show Password
            rbtnShowPassword.setOnAction(event -> {
                // Check if the radio button is selected
                if (rbtnShowPassword.isSelected()) {
                    // If selected, show the password
                    lblUserPassword.setText("Password: actual_password");
                } else {
                    // If not selected, hide the password
                    lblUserPassword.setText("Password: ****************");
                }
            });
            
            // Set a fixed width for all buttons and labels
            double buttonWidth = 150;
            double labelWidth = 200;

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
            VBox userInfoBox = new VBox(20);
            userInfoBox.getChildren().addAll(lblUsername, lblEmail, lblUserPassword, lblMembershipStatus);
            userInfoBox.getStyleClass().add("profile-info-container");

            // Create VBox for user actions
            VBox userActionBox = new VBox(20);
            userActionBox.getChildren().addAll(rbtnShowPassword, btnChangePassword, btnEditProfile, btnBack);

            // Create an HBox to hold user info and user actions side by side
            HBox profileContentHBox = new HBox(20);
            profileContentHBox.getChildren().addAll(userInfoBox, userActionBox);
            profileContentHBox.setAlignment(Pos.CENTER); // Align center

            // Add components to the root layout
            root.getChildren().addAll(lblProfileTitle, profileContentHBox);

            // Show the profile window
            profileStage.show();
        }); 

        // Settings Page
        btnSettings.setOnAction(e -> {
            // Hide the current stage
            Stage currentStage = (Stage) btnSettings.getScene().getWindow();
            currentStage.hide();

            // Create a new stage for the settings window
            Stage settingsStage = new Stage();

            // Icon
            Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
            settingsStage.getIcons().add(icon);

            // Create a layout for the settings window
            VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));

            // Create a scene and set it to the settings stage
            Scene settingsScene = new Scene(root, 600, 400);
            settingsStage.setScene(settingsScene);

            // Load CSS styles
            settingsScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

            // Set the title of the settings window
            settingsStage.setTitle("CypherGuard - Settings");

            // Show the settings window
            settingsStage.show();

            // Create a label for the settings title
            Label lblSettingsTitle = new Label("Welcome to Settings");
            lblSettingsTitle.getStyleClass().add("login-title");

            // Create a back button
            Button btnBack = new Button("Back");
            btnBack.getStyleClass().add("back-button");
            btnBack.setOnAction(backEvent -> {
                // Close the settings stage
                settingsStage.close();

                // Show the previous stage
                currentStage.show();
            });

            // Add components to the root layout
            root.getChildren().addAll(lblSettingsTitle, btnBack);
        });

        // View Passwords Page
        btnViewPasswords.setOnAction(e -> {
            ViewAndSavePasswords viewAndSavePasswords = new ViewAndSavePasswords(previousStage, previousStage.getScene());
            Scene viewAndSavePasswordsScene = new Scene(viewAndSavePasswords, 1200, 720);
            previousStage.setScene(viewAndSavePasswordsScene);
        });

        // Set a fixed width for all buttons
        double buttonWidth = 200;

        // Set the width for each button
        btnViewPasswords.setMinWidth(buttonWidth);
        btnSettings.setMinWidth(buttonWidth);
        btnLogin.setMinWidth(buttonWidth);
        btnClose.setMinWidth(buttonWidth);
        btnProfile.setMinWidth(buttonWidth);

        // Action Button Pane Design
        VBox actionbuttonBox = new VBox(20); 
        actionbuttonBox.setAlignment(Pos.CENTER);
        actionbuttonBox.getChildren().addAll(btnViewPasswords, btnSettings);

        // Header Button Pane Design
        VBox HeaderButtonBox = new VBox(20);
        HeaderButtonBox.setAlignment(Pos.TOP_RIGHT);
        HeaderButtonBox.getChildren().addAll(btnLogin, btnProfile, btnClose);

        // Create an HBox to hold the VBox containers side by side
        HBox hbox = new HBox(20);
        hbox.getChildren().addAll(actionbuttonBox, HeaderButtonBox);

        // Add padding to the HBox
        hbox.setPadding(new Insets(60));

        // Add the HBox to your layout
        add(hbox, 0, 2);
    }
}
