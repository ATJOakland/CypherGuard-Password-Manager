package main;
//************************************************************************
// CypherGuard.java Authors: 
// Andrae Taylor,
// Tim Rascol,
// Cooper Salisbury,
// Alan Shen, Nathan
// Smith,
// Waheeda Rahman
//
// Class: CSI3370
// Oakland University
//************************************************************************

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

//************************************************************************
// This Script is the main script. It sets the window variables and references the first pane of the app. 
//************************************************************************

/*
 * For Windows:
 * "vmArgs": "--module-path \"C:\/Program Files\/Java\/javafx-sdk-21.0.2\/lib\" --add-modules javafx.controls,javafx.fxml"
 * This line may need to be added to the launch.json if it's not there already to use JFX libraries
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CypherGuard extends Application {

    public static boolean isLightModeEnabled(Class<?> clazz) {
        boolean isLightModeEnabled = false;
        try {
            // Load the settings file as a resource stream
            InputStream inputStream = clazz.getResourceAsStream("/resources/userSettings.txt");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("light-mode:")) {
                        String value = line.substring(line.indexOf(":") + 1).trim();
                        isLightModeEnabled = value.equalsIgnoreCase("enabled");
                        break; // No need to continue reading the file once the light-mode preference is found
                    }
                }
                reader.close();
            } else {
                System.err.println("Settings file not found!");
            }
        } catch (Exception e) {
            // Handle file reading errors
            e.printStackTrace();
            // Default to dark mode if unable to read the preference
        }
        return isLightModeEnabled;
    }

    // Creates the main GUI Pane of the App
    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPane mainPane = new MainPane(primaryStage);
        Scene scene = new Scene(mainPane, 1200, 720);

        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        primaryStage.getIcons().add(icon);

        // Main window variables
        primaryStage.setTitle("CypherGuard - Password Manager"); // Window title
        primaryStage.setScene(scene); // Setting the scene equal to the scene object which takes from the mainPane
        primaryStage.setResizable(false); // Set the stage (window) to be non-resizable
        primaryStage.show(); // Show stage
    }

    // Non Java-FX Launcher fallback
    public static void main(String[] args) {
        Database.initializeDatabase();
        launch(args);
    }
}