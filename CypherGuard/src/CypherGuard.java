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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CypherGuard extends Application {

    // Creates the main GUI Pane of the App
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPane mainPane = new MainPane(); // An instance of the main pane script

        Scene scene = new Scene(mainPane, 1280, 720); // Sets a new scene with 1280x720 dimensions

        // Main window variables
        primaryStage.setTitle("CypherGuard - Password Manager"); // Window title
        primaryStage.setScene(scene); // Setting the scene equal to the scene object which takes from the mainPane
        primaryStage.setResizable(false); // Set the stage (window) to be non-resizable
        primaryStage.show(); // Show stage
    }

    // Non Java-FX Launcher fallback
    public static void main(String[] args) {
        launch(args);
    }
}
