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
import java.io.FileWriter;
import java.io.PrintWriter;

public class SettingsPage extends VBox {

    public SettingsPage(Stage stage, Scene previousScene) {
        // Create a layout for the settings window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Create a label for the settings title
        Label lblSettingsTitle = new Label("Welcome to Settings");
        lblSettingsTitle.getStyleClass().add("login-title");

        // Create a radio button for enabling light mode
        RadioButton rbtnStyleChoice = new RadioButton("Light Mode Enabled");

        try {
            
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                rbtnStyleChoice.setSelected(false);
            } else { // light mode is on
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                rbtnStyleChoice.setSelected(true);
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

        rbtnStyleChoice.setOnAction(event -> {
            try {
                // Update user preference in the settings file
                FileWriter fileWriter = new FileWriter("CypherGuard\\src\\resources\\userSettings.txt");

                PrintWriter printWriter = new PrintWriter(fileWriter);
        
                // Update application styling
                getStylesheets().clear(); // Remove existing stylesheets
        
                if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                    getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                    printWriter.println("light-mode:enabled");
                    System.out.println("light-mode styling enabled...\n");
                } else {
                    getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                    printWriter.println("light-mode:disabled");
                    System.out.println("light-mode styling disabled...\n");
                }
        
                printWriter.close();
            } catch (Exception e) {
                System.err.println("Failed to update settings file: " + e.getMessage());
            }
        });
        

        // Set the title of the settings window
        stage.setTitle("CypherGuard - Settings");

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            MainPane mainPane = (MainPane) previousScene.getRoot();
            // Refresh the stylesheet
            mainPane.refreshStylesheet();
            stage.setScene(previousScene);
            stage.setTitle("CypherGuard - Password Manager");
        });
        

        // Add components to the root layout
        getChildren().addAll(lblSettingsTitle, rbtnStyleChoice, btnBack);
    }
}
