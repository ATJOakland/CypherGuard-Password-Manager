package main;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SettingsPage extends VBox {

    public SettingsPage(Stage stage, Scene previousScene) {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20); // Add spacing between components

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Logo Code
        Image logoImg = new Image(getClass().getResourceAsStream("/resources/CypherGuardLogo.png"));
        ImageView logoImageView = new ImageView(logoImg);
        StackPane logoStackPane = new StackPane();
        logoStackPane.getChildren().add(logoImageView);

        // Create a label for the settings title
        Label lblSettingsTitle = new Label("Welcome to Settings");
        lblSettingsTitle.getStyleClass().add("login-title");

        // Create a radio button for enabling light mode
        RadioButton rbtnStyleChoice = new RadioButton("Light Mode Enabled      ");
        rbtnStyleChoice.getStyleClass().add("show-password-radio-button");

        try {
            boolean isLightModeEnabled = CypherGuard.isLightModeEnabled(CypherGuard.class);
            if (isLightModeEnabled) {
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
            } else {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
            }
            rbtnStyleChoice.setSelected(isLightModeEnabled);
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
                boolean isLightModeEnabled = CypherGuard.isLightModeEnabled(CypherGuard.class);
                if (!isLightModeEnabled) {
                    getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                    printWriter.println("light-mode:enabled");
                    System.out.println("Light mode styling enabled...\n");
                } else {
                    getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                    printWriter.println("light-mode:disabled");
                    System.out.println("Light mode styling disabled...\n");
                }

                printWriter.close();
            } catch (Exception e) {
                System.err.println("Failed to update settings file: " + e.getMessage());
            }
        });

        // Create a back button
        Button btnBack = new Button("Back");
        btnBack.getStyleClass().add("back-button");
        btnBack.setOnAction(backEvent -> {
            MainPane mainPane = (MainPane) previousScene.getRoot();
            mainPane.refreshStylesheet();
            stage.setScene(previousScene);
            stage.setTitle("CypherGuard - Password Manager");
        });

        // Add components to the root layout
        getChildren().addAll(logoImageView, lblSettingsTitle, rbtnStyleChoice, btnBack);
    }
}