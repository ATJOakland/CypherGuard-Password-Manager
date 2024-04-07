package main;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class SettingsPage extends VBox{

    public SettingsPage(Stage stage, Scene previousScene){
        // Create a layout for the settings window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Create a label for the settings title
        Label lblSettingsTitle = new Label("Welcome to Settings");
        lblSettingsTitle.getStyleClass().add("login-title");   

        // Create some radio buttons for settings
        RadioButton rbtnStyleChoice = new RadioButton("Light Mode Enabled");

        try {
            
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
            } else { // light mode is on
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

        // Set the title of the settings window
        stage.setTitle("CypherGuard - Settings");

        // Show the settings window
        stage.show();

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            stage.setScene(previousScene);
            stage.setTitle("CypherGuard - Password Manager");
        });

        // To Do:
        // LIGHT DARK VERSION VISUALS OF APP - Tim
        // Start up APP on PC launch option - Open
        // Delete Account Option - Open

        // Add components to the root layout
        getChildren().addAll(lblSettingsTitle, rbtnStyleChoice, btnBack);
    }
}
