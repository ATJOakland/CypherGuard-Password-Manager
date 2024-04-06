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
            LoginPage LoginPage = new LoginPage(previousStage, previousStage.getScene());
            Scene LoginScene = new Scene(LoginPage, 1200, 720);
            previousStage.setScene(LoginScene);
        });        

        // Profile Page
        btnProfile.setOnAction(e -> {
            ProfilePage profilePage = new ProfilePage(previousStage, previousStage.getScene());
            Scene profileScene = new Scene(profilePage, 1200, 720);
            previousStage.setScene(profileScene);
        }); 

        // Settings Page
        btnSettings.setOnAction(e -> {
            SettingsPage settingsPage = new SettingsPage(previousStage, previousStage.getScene());
            Scene settingsScene = new Scene(settingsPage, 1200, 720);
            previousStage.setScene(settingsScene);
        });

        // View Passwords Page
        btnViewPasswords.setOnAction(e -> {
            PasswordPage PasswordPage = new PasswordPage(previousStage, previousStage.getScene());
            Scene PasswordScene = new Scene(PasswordPage, 1200, 720);
            previousStage.setScene(PasswordScene);
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
