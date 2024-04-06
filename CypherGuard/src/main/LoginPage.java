package main;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class LoginPage extends VBox{

    public LoginPage(Stage stage, Scene previousScene){
        // Create a layout for the login window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        Label lblLoginTitle = new Label("Login or Create a New Account Below");

        getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
        
        // Set the title of the login window
        stage.setTitle("CypherGuard - Login");
    
        // Show the login window
        stage.show();
        
        // Create a ToggleGroup
        ToggleGroup toggleGroup = new ToggleGroup();

        // Create Account radio button
        RadioButton rbtnCreate = new RadioButton("Create Account      "); // DO NOT REMOVE SPACES
        rbtnCreate.setToggleGroup(toggleGroup); 
        rbtnCreate.setSelected(false);

        // Login radio button
        RadioButton rbtnLogin = new RadioButton("Login      "); // DO NOT REMOVE SPACES
        rbtnLogin.setToggleGroup(toggleGroup); 
        rbtnLogin.setSelected(true);

        TextField userName = new TextField();
        userName.setPromptText("Enter your Username...");
        userName.setPrefWidth(100);
        TextField userPassword = new TextField();
        userPassword.setPromptText("Enter your Password...");

        // Create a back button
        Button btnBack = new Button("Back");
        Button btnLoginUser = new Button("Login");

        // Styling for login pane buttons
        btnBack.getStyleClass().add("back-button");
        rbtnCreate.getStyleClass().add("radio-button");
        rbtnLogin.getStyleClass().add("radio-button");
        userName.getStyleClass().add("text-field");
        userPassword.getStyleClass().add("text-field");
        btnLoginUser.getStyleClass().add("login-button");
        lblLoginTitle.getStyleClass().add("login-title");

        btnBack.setOnAction(backEvent -> {
            stage.setScene(previousScene);
        });
        
        btnLoginUser.setOnAction(loginEvent -> {
            String username = userName.getText();
            String password = userPassword.getText();
            String salt = null;

            if (rbtnLogin.isSelected()) {
                salt = Database.getSalt(username);
                String masterPassword = Database.getMaster(username);

                boolean isValid = Key.authenticate(password, masterPassword, salt);

                if (isValid) {
                    UserSession.getInstance(username);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Now logged in as " + username + "!");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password.");
                    alert.showAndWait();
                }
            }
            else if(rbtnCreate.isSelected()) {
                try{
                    //Generate salt and hash the master password
                    salt = Key.generateSalt();
                    password = Key.deriveKey(password, salt);
                }
                catch (Exception e) {
                    System.out.println("Error generating salt: " + e.getMessage());
                }
                boolean isCreated = Database.createUser(username, password, salt);
                if(isCreated){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully!");
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create account.");
                    alert.showAndWait();
                }
            }
        });

        // Create an HBox for radio buttons
        HBox radioButtonsBox = new HBox(20); 
        radioButtonsBox.setAlignment(Pos.CENTER); 

        // Create an HBox for the login and back buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnLoginUser, btnBack);

        // Add radio buttons to the HBox
        radioButtonsBox.getStyleClass().add("radio-buttons-box");
        radioButtonsBox.getChildren().addAll(rbtnLogin, rbtnCreate);
        HBox.setMargin(radioButtonsBox, new Insets(20, 0, 0, 0));

        // Add spacing between elements
        VBox.setMargin(userName, new Insets(40, 0, 10, 0)); 
        VBox.setMargin(userPassword, new Insets(10, 0, 40, 0)); 
        VBox.setMargin(btnLoginUser, new Insets(0, 5, 10, 0)); 
        VBox.setMargin(btnBack, new Insets(0, 0, 10, 5)); 
        
        // Set preferred width for userPassword TextField
        userPassword.setPrefWidth(100);
        setPadding(new Insets(100));
        // Add the components to the root layout
        getChildren().addAll(radioButtonsBox, userName, userPassword, buttonBox);
    }
}
