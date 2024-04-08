package main;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class LoginPage extends VBox{

    private boolean passwordVisible = false;

    public static boolean isUserOnline() {
        return userOnline;
    }

    private static boolean userOnline = false; // False initially

    public LoginPage(Stage stage, Scene previousScene){
        // Create a layout for the login window
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        // Icon
        Image icon = new Image(getClass().getResourceAsStream("/resources/CypherGuardIcon.png"));
        stage.getIcons().add(icon);

        // Logo Code
        Image logoImg = new Image(getClass().getResourceAsStream("/resources/CypherGuardLogo.png"));
        ImageView logoImageView = new ImageView(logoImg);
            
        Label lblLoginTitle = new Label("Login or Create a New Account Below");

        try {
            
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
            } else { // light mode is on
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }
        
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
        
        TextField txtuserName = new TextField();
        txtuserName.setPromptText("Enter your Username...");
        TextField txtuserPassword = new TextField();
        PasswordField hidden_userPassword = new PasswordField();
        hidden_userPassword.setPromptText("Enter your Password...");
        txtuserPassword.setPromptText("Enter your Password...");
        txtuserPassword.setVisible(false);

        // Create a back button
        Button btnBack = new Button("Back");
        Button btnLoginUser = new Button("Login");

        // Radio button for show password
        RadioButton rbtnShowPassword = new RadioButton("Show Password       ");
        rbtnShowPassword.getStyleClass().add("show-password-radio-button");

        // The Show Password Function (This took forEVER to figure out, i ended up with two controls that sit on top of each other... Bad solution, but it works.)
        rbtnShowPassword.setOnAction(event -> {

            // Toggle password visibility
            passwordVisible = !passwordVisible;

            if (passwordVisible) {
                txtuserPassword.setVisible(true);
                hidden_userPassword.setVisible(false);
            } else {
                txtuserPassword.setVisible(false);
                hidden_userPassword.setVisible(true);
            }
        });

        // Styling for login pane buttons
        btnBack.getStyleClass().add("back-button");
        rbtnCreate.getStyleClass().add("radio-button");
        rbtnLogin.getStyleClass().add("radio-button");
        txtuserName.getStyleClass().add("text-field");
        txtuserPassword.getStyleClass().add("text-field");
        hidden_userPassword.getStyleClass().add("text-field");
        btnLoginUser.getStyleClass().add("login-button");
        lblLoginTitle.getStyleClass().add("login-title");

        btnBack.setOnAction(backEvent -> {
            stage.setScene(previousScene);
            stage.setTitle("CypherGuard - Password Manager");
        });
        
        // Add event listeners to update password value
        txtuserPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            hidden_userPassword.setText(newValue);
        });

        hidden_userPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            txtuserPassword.setText(newValue);
        });

        btnLoginUser.setOnAction(loginEvent -> {
            String username = txtuserName.getText();
            String password = txtuserPassword.getText();
            String salt = null;

            if (rbtnLogin.isSelected()) {
                salt = Database.getSalt(username);
                String masterPassword = Database.getMaster(username);

                boolean isValid = Key.authenticate(password, masterPassword, salt);

                if (isValid) {
                    UserSession.getInstance(username);
                    stage.setScene(previousScene);
                    stage.setTitle("CypherGuard - Password Manager");
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
                    stage.setScene(previousScene);
                    stage.setTitle("CypherGuard - Password Manager");
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to create account.");
                    alert.showAndWait();
                }
            }
        });

        // Set a fixed width for all buttons and labels
        double buttonWidth = 150;
        double textFieldWidth = 250;
        double radioButtonBoxWidth = 300;

        // Set the width for each button
        btnBack.setMinWidth(buttonWidth);
        btnLoginUser.setMinWidth(buttonWidth);

        // Set Standard width for each text field
        txtuserName.setMaxWidth(textFieldWidth);
        txtuserPassword.setMaxWidth(textFieldWidth);
        hidden_userPassword.setMaxWidth(textFieldWidth);

        // Create an HBox for radio buttons
        HBox radioButtonsBox = new HBox(20); 
        radioButtonsBox.setAlignment(Pos.CENTER); 

        // Create an HBox for the login and back buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnLoginUser, rbtnShowPassword, btnBack);
        VBox.setMargin(buttonBox, new Insets(20, 0, 0, 0));

        // Add radio buttons to the HBox
        radioButtonsBox.getStyleClass().add("radio-buttons-box");
        radioButtonsBox.getChildren().addAll(rbtnLogin, rbtnCreate);
        HBox.setMargin(radioButtonsBox, new Insets(-10, 0, 0, 0));
        radioButtonsBox.setMaxWidth(radioButtonBoxWidth);

        // Add spacing between elements
        VBox.setMargin(txtuserName, new Insets(10, 0, 10, 0)); 
        VBox.setMargin(txtuserPassword, new Insets(10, 0, 40, 0)); 
        
        // Set preferred width for userPassword TextField
        txtuserPassword.setPrefWidth(100);
        setPadding(new Insets(100));
        // Add the components to the root layout

        StackPane passwordPane = new StackPane();
        passwordPane.getChildren().addAll(hidden_userPassword, txtuserPassword);

        // Adjust the margins to move everything upward
        VBox.setMargin(logoImageView, new Insets(-80, 0, -20, 0));
        VBox.setMargin(radioButtonsBox, new Insets(-20, 0, 0, 0));
        VBox.setMargin(txtuserName, new Insets(20, 0, 10, 0)); 
        VBox.setMargin(passwordPane, new Insets(10, 0, 10, 0)); 
        VBox.setMargin(buttonBox, new Insets(10, 0, 0, 0));
        VBox.setMargin(lblLoginTitle, new Insets(-20, 0, 10, 0));

        getChildren().addAll(logoImageView, lblLoginTitle, radioButtonsBox, txtuserName, passwordPane, buttonBox);
    }
}