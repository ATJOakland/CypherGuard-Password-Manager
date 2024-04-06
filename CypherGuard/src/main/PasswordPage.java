package main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
        getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        // Set the title of the view passwords window
        stage.setTitle("CypherGuard - View Passwords");

        // Button to open the add password popup
        Button btnAddPasswordPopup = new Button("Add Password");
        btnAddPasswordPopup.getStyleClass().add("action-button");
        
        // Show stage
        stage.show();

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
            Button btnBack = new Button("Back");
            btnBack.getStyleClass().add("back-button");

            // Text Fields
            TextField txtAddedUserName = new TextField();
            txtAddedUserName.setPromptText("Enter your Username...");

            TextField txtAddedPassword = new TextField();
            txtAddedPassword.setPromptText("Enter your Password...");

            TextField txtWebsiteName = new TextField();
            txtWebsiteName.setPromptText("Enter the Wesbite's name...");

            // Assigning styles to text field
            txtAddedUserName.getStyleClass().add("text-field");
            txtAddedPassword.getStyleClass().add("text-field");
            txtWebsiteName.getStyleClass().add("text-field");

            // Create a label for the view passwords title
            Label lblViewPasswordsTitle = new Label("Add a new password");
            lblViewPasswordsTitle.getStyleClass().add("login-title");

            // Set the title of the view passwords window
            popupStage.setTitle("CypherGuard - Add a new Password");

            // Add components to the popup layout
            popupLayout.getChildren().addAll(lblViewPasswordsTitle, txtAddedUserName, txtAddedPassword, txtWebsiteName,
                    btnSave, btnBack);

            Scene popupScene = new Scene(popupLayout, 400, 400);
            popupScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
            popupStage.setScene(popupScene);

            // Show stage
            popupStage.show();

            btnSave.setOnAction(saveInputevent -> {

                String website = txtWebsiteName.getText();
                String appUsername = txtAddedUserName.getText();
                String appPassword = txtAddedPassword.getText();

                boolean isAdded = Database.addPassword(appUsername, website, appPassword);

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

            btnBack.setOnAction(backEvent -> {
                // Close the view passwords stage
                popupStage.close();
            });
        });

        // Add components to the root layout
        getChildren().addAll(btnAddPasswordPopup);
    }
}