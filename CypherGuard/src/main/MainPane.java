package main;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;

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
        Button btnSavePassword = new Button("Save a Password");
        Button btnViewPassword = new Button("Look at Passwords");
        Button btnSaveNote = new Button("Settings");
        Button btnProfile = new Button("View Profile");
        Button btnClose = new Button("Close");
        
        // CSS Styling for buttons
        btnLogin.getStyleClass().add("login-button");
        btnSavePassword.getStyleClass().add("action-button");
        btnViewPassword.getStyleClass().add("action-button");
        btnSaveNote.getStyleClass().add("action-button");
        btnClose.getStyleClass().add("close-button");
        btnProfile.getStyleClass().add("action-button");

        // Button actions
        btnClose.setOnAction(e -> Platform.exit());

        // Login Page
        btnLogin.setOnAction(e -> {

            // Hide the current stage
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.hide();
        
            // Create a new stage for the login window
            Stage loginStage = new Stage();

            Label lblLoginTitle = new Label("Login or Create a New Account Below");
        
            // Create a layout for the login window
            VBox root = new VBox(lblLoginTitle);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));

            // Create a scene and set it to the login stage
            Scene loginScene = new Scene(root, 600, 400); // Adjust width and height as needed
            loginStage.setScene(loginScene);

            loginScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
            
            // Set the title of the login window
            loginStage.setTitle("CypherGuard - Login");
        
            // Show the login window
            loginStage.show();
            
            // Create a ToggleGroup
            ToggleGroup toggleGroup = new ToggleGroup();

            // Create Account radio button
            RadioButton rbtnCreate = new RadioButton("Create Account      "); // DO NOT REMOVE SPACES
            rbtnCreate.setToggleGroup(toggleGroup); 
            rbtnCreate.setSelected(false);

            // Login radio button
            RadioButton rbtnLogin = new RadioButton("Login      "); // DO NOT REMOVE SPACES
            rbtnLogin.setToggleGroup(toggleGroup); 
            rbtnLogin.setSelected(true);

            TextField userName = new TextField();
            userName.setPromptText("Enter your Username...");
            userName.setPrefWidth(100);
            TextField userPassword = new TextField();
            userPassword.setPromptText("Enter your Password...");

            // Create a back button
            Button btnBack = new Button("Back");
            Button btnLoginUser = new Button("Login");

            // Styling for login pane buttons
            btnBack.getStyleClass().add("back-button");
            rbtnCreate.getStyleClass().add("radio-button");
            rbtnLogin.getStyleClass().add("radio-button");
            userName.getStyleClass().add("text-field");
            userPassword.getStyleClass().add("text-field");
            btnLoginUser.getStyleClass().add("login-button");
            lblLoginTitle.getStyleClass().add("login-title");

            btnBack.setOnAction(backEvent -> {
                // Close the login stage
                loginStage.close();
        
                // Show the previous stage
                currentStage.show();
            });
        
            // Create an HBox for radio buttons
            HBox radioButtonsBox = new HBox(20); 
            radioButtonsBox.setAlignment(Pos.CENTER); 

            // Create an HBox for the login and back buttons
            HBox buttonBox = new HBox(20); 
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(btnLoginUser, btnBack);

            // Add radio buttons to the HBox
            radioButtonsBox.getStyleClass().add("radio-buttons-box");
            radioButtonsBox.getChildren().addAll(rbtnLogin, rbtnCreate);
            HBox.setMargin(radioButtonsBox, new Insets(20, 0, 0, 0));

            // Add spacing between elements
            VBox.setMargin(userName, new Insets(40, 0, 10, 0)); 
            VBox.setMargin(userPassword, new Insets(10, 0, 40, 0)); 
            VBox.setMargin(btnLoginUser, new Insets(0, 5, 10, 0)); 
            VBox.setMargin(btnBack, new Insets(0, 0, 10, 5)); 
            
            // Set preferred width for userPassword TextField
            userPassword.setPrefWidth(100);
            root.setPadding(new Insets(150)); // Set padding for the VBox
            // Add the components to the root layout
            root.getChildren().addAll(radioButtonsBox, userName, userPassword, buttonBox);
        });        

        // Profile Page
        btnProfile.setOnAction(e -> {
            // Hide the current stage
            Stage currentStage = (Stage) btnProfile.getScene().getWindow();
            currentStage.hide();

            // Create a new stage for the profile window
            Stage profileStage = new Stage();

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

            // Show the profile window
            profileStage.show();

            // Create a label for the profile title
            Label lblProfileTitle = new Label("Welcome to Your Profile");
            lblProfileTitle.getStyleClass().add("login-title");

            // Create a back button
            Button btnBack = new Button("Back");
            btnBack.getStyleClass().add("back-button");
            btnBack.setOnAction(backEvent -> {
                // Close the profile stage
                profileStage.close();

                // Show the previous stage
                currentStage.show();
            });

    // Add components to the root layout
    root.getChildren().addAll(lblProfileTitle, btnBack);
});

        // Set a fixed width for all buttons
        double buttonWidth = 200; // Adjust as needed

        // Set the width for each button
        btnSavePassword.setMinWidth(buttonWidth);
        btnViewPassword.setMinWidth(buttonWidth);
        btnSaveNote.setMinWidth(buttonWidth);
        btnLogin.setMinWidth(buttonWidth);
        btnClose.setMinWidth(buttonWidth);
        btnProfile.setMinWidth(buttonWidth);

        // Action Button Pane Design
        VBox actionbuttonBox = new VBox(20); 
        actionbuttonBox.setAlignment(Pos.CENTER);
        actionbuttonBox.getChildren().addAll(btnSavePassword, btnViewPassword, btnSaveNote);

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
