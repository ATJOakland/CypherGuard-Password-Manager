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

public class PasswordPage extends VBox{

    public PasswordPage(Stage stage, Scene previousScene) {
        
        // Create a layout for the view passwords window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Save user inputted data
        Button btnSave = new Button("Save Password");
        btnSave.getStyleClass().add("action-button");
        
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
        Label lblViewPasswordsTitle = new Label("View Passwords");
        lblViewPasswordsTitle.getStyleClass().add("login-title");
        
        // Load CSS styles
        getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());

        // Set the title of the view passwords window
        stage.setTitle("CypherGuard - View Passwords");

        //Show stage
        stage.show();

        btnSave.setOnAction(event -> {

            String website = txtWebsiteName.getText();
            String appUsername = txtAddedUserName.getText();
            String appPassword = txtAddedPassword.getText();
            
            boolean isAdded = Database.addPassword(appUsername, website, appPassword);

            if (isAdded) {
                //Show success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credentials saved successfully!");
                alert.showAndWait();
            }
            else {
                // Show error alert
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save credentials.");
                alert.showAndWait();
            }
        });
        

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            // Close the view passwords stage
            stage.setScene(previousScene);
        });

        // Add components to the root layout
        getChildren().addAll(lblViewPasswordsTitle, txtAddedUserName, txtAddedPassword, txtWebsiteName, btnSave, btnBack);
    }
}