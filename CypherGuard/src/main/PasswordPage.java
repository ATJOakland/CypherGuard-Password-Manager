package main;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PasswordPage extends VBox {
    private Stage stage;

    public PasswordPage(Stage stage, Scene previousScene) {
        this.stage = stage;
        // Create a layout for the view passwords window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        VBox stageLayoutBox = new VBox(20);
        stageLayoutBox.setAlignment(Pos.CENTER);
        stageLayoutBox.setPadding(new Insets(20));

        // Load CSS styles
        try {
            
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
            } else { // light mode is on
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

        // Set the title of the view passwords window
        stage.setTitle("CypherGuard - View Passwords");

        // Button to open the add password popup
        Button btnAddPasswordPopup = new Button("Add Password");
        btnAddPasswordPopup.getStyleClass().add("action-button");

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");

        //Create a ListView for the passwords
        ListView<VBox> passwordListView = new ListView<>();

        //Get the passwords from the database
        List<Map<String, String>> passwords = Database.getUserPasswords();

        for (Map<String, String> password: passwords){
            Label lblPlatform = new Label(password.get("platform"));
            Label lblUsername = new Label(password.get("username"));

            //Create a VBox to hold the labels
            VBox passwordHBox = new VBox(10);
            passwordHBox.getChildren().addAll(lblPlatform, lblUsername);

            passwordListView.getItems().add(passwordHBox);
        }

        // Show stage
        stage.show();

        btnBack.setOnAction(backEvent -> {
            // Set the scene to the previous scene
            stage.setScene(previousScene);
        });

        btnAddPasswordPopup.setOnAction(openPopUpEvent -> {

            // Create a new AddPasswordPopup
            Stage popupStage = new Stage();

            // Create layout for popup
            VBox popupLayout = new VBox(20);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setPadding(new Insets(20));

            // Save user inputted data
            Button btnSave = new Button("Save Password");
            btnSave.getStyleClass().add("action-button");

            // Create a back button
            Button btnClose = new Button("Back");
            btnClose.getStyleClass().add("back-button");

            // Text Fields
            TextField txtWebsiteName = new TextField();
            txtWebsiteName.setPromptText("Enter the Wesbite's name...");

            TextField txtAddedUserName = new TextField();
            txtAddedUserName.setPromptText("Enter your Username...");

            TextField txtAddedPassword = new TextField();
            txtAddedPassword.setPromptText("Enter your Password...");

            // Assigning styles to text field
            txtWebsiteName.getStyleClass().add("text-field");
            txtAddedUserName.getStyleClass().add("text-field");
            txtAddedPassword.getStyleClass().add("text-field");

            // Create a label for the view passwords title
            Label lblViewPasswordsTitle = new Label("Add a new password");
            lblViewPasswordsTitle.getStyleClass().add("login-title");

            // Set the title of the view passwords window
            popupStage.setTitle("CypherGuard - Add a new Password");

            // Add components to the popup layout
            popupLayout.getChildren().addAll(lblViewPasswordsTitle, txtWebsiteName, txtAddedUserName, txtAddedPassword,
                    btnSave, btnClose);

            Scene popupScene = new Scene(popupLayout, 400, 400);

            try {
            
                if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                    popupScene.getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                    popupStage.setScene(popupScene);
                } else { // light mode is on
                    popupScene.getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                    popupStage.setScene(popupScene);
                }
    
            } catch (Exception e) {
                System.err.println("Failed to load CSS file: " + e.getMessage());
            }

            // Show stage
            popupStage.show();

            btnSave.setOnAction(saveInputevent -> {

                String website = txtWebsiteName.getText();
                String appUsername = txtAddedUserName.getText();
                String appPassword = txtAddedPassword.getText();
                String encrypted;

                if (website.isEmpty() || appUsername.isEmpty() || appPassword.isEmpty()) {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
                    alert.showAndWait();
                    return;
                }

                String currentUser = UserSession.getInstance().getUsername();
                try{
                    String key = Key.deriveKey(Database.getMaster(currentUser), Database.getSalt(currentUser));
                    encrypted = AES.encrypt(appPassword, key);
                }
                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to encrypt password, please try again.");
                    alert.showAndWait();
                    return;
                }

                boolean isAdded = Database.addPassword(appUsername, website, encrypted);

                if (isAdded) {
                    // Show success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credentials saved successfully!");
                    alert.showAndWait();
                } else {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save credentials.");
                    alert.showAndWait();
                }
            });

            btnClose.setOnAction(closePopUpEvent -> {
                // Close the view passwords stage
                popupStage.close();
            });
        });

        // To Do:
        // Implement password view - Open

        // Add components to the root layout
        getChildren().addAll(btnAddPasswordPopup, btnBack, passwordListView);
    }
}