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

    // Creates the main GUI Pane of the App
    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPane mainPane = new MainPane(primaryStage);
        Scene scene = new Scene(mainPane, 1200, 720);

        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        primaryStage.getIcons().add(icon);

        try {
            scene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

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