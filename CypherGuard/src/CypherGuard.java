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

import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class CypherGuard extends Application {

    // Creates the main GUI Pane of the App
    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainPane mainPane = new MainPane(); // An instance of the main pane script

        Scene scene = new Scene(mainPane, 1280, 720); // Sets a new scene with 1280x720 dimensions

        // Main window variables
        primaryStage.setTitle("CypherGuard - Password Manager"); // Window title
        primaryStage.setScene(scene); // Setting the scene equal to the scene object which takes from the mainPane
        primaryStage.setResizable(false); // Set the stage (window) to be non-resizable
        primaryStage.show(); // Show stage

        // Calling SavePassword to encrypt all the Data in TestData.java and save it to a text file.
        SavePassword accountSaver = new SavePassword();
        TestData testData = new TestData(); // Probably Temporary
        for (HashMap<String, String> account : testData.accounts) { // For every HashMap in the accounts array in TestData.java
            accountSaver.savePasswordToFile(account); // Save the Hashmap
        }

        // Then getAccountCredentials() in SavePassword.java to load data from the text file with the account name.
        SavePassword accountRetriever = new SavePassword();
        try {
            // Get the HashMap of the account
            String accountName = "Instagram";
            HashMap<String, String> credentials = accountRetriever.getAccountCredentials(accountName);

            // Display the username and password
            if (!credentials.isEmpty()) {
                System.out.println("\ngetAccountCredentials() was called to get the details of: " + accountName);
                System.out.println("=======================================================================");
                System.out.println("Username: " + credentials.get("Username"));
                System.out.println("Password: " + credentials.get("Password"));
                System.out.println("=======================================================================\n");
            } else {
                System.out.println("\nAccount not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Non Java-FX Launcher fallback
    public static void main(String[] args) {
        launch(args);
    }
}
