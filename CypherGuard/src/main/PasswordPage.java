package main;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PasswordPage extends GridPane {
    private static final int SPACING = 20;
    private static final int PADDING = 20;
    private static final String CSS_FILE_PATH = "/resources/styles.css";
    private static final String ACTION_BUTTON_STYLE = "action-button";
    private static final String BACK_BUTTON_STYLE = "back-button";
    private static final String LOGIN_TITLE_STYLE = "login-title";
    private static final String TEXT_FIELD_STYLE = "text-field";

    private Stage stage;
    private ListView<Map<String, String>> passwordListView;
    private ObservableList<Map<String, String>> observableList;
    List<Map<String, String>> passwords;

    private TextField txtUsername;
    private TextField txtPassword;
    private TextField txtUrl;
    private ImageView favicon;
    private Label lblWebsiteName;
    private TextField txtSearch;

    public PasswordPage(Stage stage, Scene previousScene) {
        this.stage = stage;
        List<Pane> layout = setupLayout();
        Pane vboxLeft = layout.get(0);
        Pane vboxRight = layout.get(1);
        Pane topLeftHBox = layout.get(2);
        Pane topRightHBox = layout.get(3);
        Pane selectionVBox = layout.get(4);
        Pane topSelectionHBox = layout.get(5);

        // Set a border on the VBox instances for visibility
        vboxLeft.setStyle("-fx-border-color: red;");
        vboxRight.setStyle("-fx-border-color: yellow;");
        topLeftHBox.setStyle("-fx-border-color: white;");
        topRightHBox.setStyle("-fx-border-color: white;");
        selectionVBox.setStyle("-fx-border-color: white;");
        topSelectionHBox.setStyle("-fx-border-color: white;");

        List<Button> buttons = setupButtons(previousScene);
        Button btnAddPasswordPopup = buttons.get(0);
        Button btnBack = buttons.get(1);
        Button btnEditPassword = buttons.get(2);
        Button btnDeletePassword = buttons.get(3);

        // Labels
        Label lblWebsite = createLabel("Website: ", "label");
        Label lblUsername = createLabel("Username: ", "label");
        Label lblPassword = createLabel("Password: ", "label");

        // Create a ListView for the passwords
        passwordListView = new ListView<>();
        setupListView();

        //Search bar
        setupSearchBar();

        passwordListView.getSelectionModel().selectFirst();

        topSelectionHBox.getChildren().addAll(favicon, lblWebsiteName);
        selectionVBox.getChildren().addAll(topSelectionHBox, lblUsername, txtUsername, lblPassword, txtPassword,
                lblWebsite, txtUrl);
        topRightHBox.getChildren().addAll(btnEditPassword, btnDeletePassword);
        topLeftHBox.getChildren().addAll(txtSearch, btnAddPasswordPopup);
        vboxLeft.getChildren().addAll(topLeftHBox, passwordListView);
        vboxRight.getChildren().addAll(topRightHBox, selectionVBox, btnBack);

        add(vboxLeft, 1, 0);
        add(vboxRight, 2, 0);

        // Show stage
        stage.show();
    }

    private List<Pane> setupLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(PADDING));
        // Load CSS styles
        try {
            
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
            } else { // light mode is on
                getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

        VBox vboxLeft = new VBox(SPACING);
        HBox topLeftHBox = new HBox(SPACING);

        VBox vboxRight = new VBox(SPACING);
        HBox topRightHBox = new HBox(SPACING);
        VBox selectionVBox = new VBox(SPACING);
        HBox topSelectionHBox = new HBox(SPACING);

        topLeftHBox.setAlignment(Pos.CENTER);
        topRightHBox.setAlignment(Pos.CENTER_RIGHT);

        vboxLeft.setPrefWidth(600);
        vboxLeft.setPrefHeight(600);
        vboxRight.setPrefWidth(600);
        vboxRight.setPrefHeight(600);

        vboxLeft.setAlignment(Pos.CENTER);
        vboxRight.setAlignment(Pos.CENTER);

        selectionVBox.setAlignment(Pos.CENTER_LEFT);
        topSelectionHBox.setAlignment(Pos.CENTER_LEFT);

        // Set the title of the view passwords window
        stage.setTitle("CypherGuard - View Passwords");

        return Arrays.asList(vboxLeft, vboxRight, topLeftHBox, topRightHBox, selectionVBox, topSelectionHBox);
    }

    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private TextField createTextField(String promptText, String styleClass) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add(styleClass);
        return textField;
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private List<Button> setupButtons(Scene previousScene) {
        // Button to open the add password popup
        Button btnAddPasswordPopup = createButton("+", ACTION_BUTTON_STYLE);
        // Create a back button
        Button btnBack = createButton("Back", BACK_BUTTON_STYLE);
        // Create edit password button
        Button btnEditPassword = createButton("Edit", ACTION_BUTTON_STYLE);
        // Create delete password button
        Button btnDeletePassword = createButton("Delete", ACTION_BUTTON_STYLE);

        btnEditPassword.setOnAction(editEvent -> {
            if (btnEditPassword.getText().equals("Edit")) {
                //Make test text fields editable
                txtUsername.setEditable(true);
                txtPassword.setEditable(true);
                txtUrl.setEditable(true);

                btnEditPassword.setText("Save");
            } 
            else {
                txtUsername.setEditable(false);
                txtPassword.setEditable(false);
                txtUrl.setEditable(false);
                btnEditPassword.setText("Edit");

                //Create a new ConfirmMasterPasswordPopup
                Stage popupStage = new Stage();

                //Create layout for popup
                VBox popupLayout = new VBox(SPACING);
                popupLayout.setAlignment(Pos.CENTER);
                popupLayout.setPadding(new Insets(PADDING));

                //Buttons
                Button btnConfirm = createButton("Confirm", ACTION_BUTTON_STYLE);
                Button btnClose = createButton("Back", BACK_BUTTON_STYLE);

                //Text Fields
                TextField txtConfirmMaster = createTextField("", TEXT_FIELD_STYLE);

                //Label
                Label lblConfirmMaster = createLabel("Confirm master password", LOGIN_TITLE_STYLE);

                // Set the title of the view passwords window
                popupStage.setTitle("CypherGuard - Confirm Master Password");

                // Add components to the popup layout
                popupLayout.getChildren().addAll(lblConfirmMaster, txtConfirmMaster, btnConfirm, btnClose);

                Scene popupScene = new Scene(popupLayout, 400, 400);
                popupScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
                popupStage.setScene(popupScene);

                popupStage.show();

                btnConfirm.setOnAction(confirmEvent -> {
                    String currentUser = UserSession.getInstance().getUsername();
                    String inputMaster = txtConfirmMaster.getText();
                    Boolean isAuthenticated = Key.authenticate(inputMaster, Database.getMaster(currentUser), Database.getSalt(currentUser));

                    if (isAuthenticated == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect master password.");
                        alert.showAndWait();
                        return;
                    }
                        String user = UserSession.getInstance().getUsername();
                        String encryptedPassword = "";

                        //Encrypt the password
                        try{
                            String key = Key.deriveKey(Database.getMaster(user), Database.getSalt(user));
                            encryptedPassword = AES.encrypt(txtPassword.getText(), key);
                        }
                        catch (Exception e) {
                            System.out.println("Error decrypting password: " + e.getMessage());
                        }

                        boolean isUpdated = Database.updatePassword(txtUrl.getText(),encryptedPassword);
                        isUpdated = Database.updateUsername(txtUrl.getText(),txtUsername.getText());

                    if (isUpdated) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credentials updated successfully!");
                        alert.showAndWait();
                        // Clear the obersvable list
                        observableList.clear();
                        // Repopulate with udpated data
                        passwords = Database.getUserPasswords();
                        observableList.addAll(passwords);
                        // Select the first item in the ListView
                        passwordListView.getSelectionModel().selectFirst();

                    } else {
                        // Show error alert
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update credentials.");
                        alert.showAndWait();
                    }
                    // Close the confirm master stage
                    popupStage.close();
                });

                btnClose.setOnAction(closePopUpEvent -> {
                    // Close the confirm master stage
                    popupStage.close();
                });
            }
        });

        btnDeletePassword.setOnAction(deleteEvent -> {

            //Create a new DeletePasswordPopup
            Stage popupStage = new Stage();

            //Create layout for popup
            VBox popupLayout = new VBox(SPACING);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setPadding(new Insets(PADDING));

            //Buttons
            Button btnConfirm = createButton("Confirm", ACTION_BUTTON_STYLE);
            Button btnClose = createButton("Back", BACK_BUTTON_STYLE);

            //Text Fields
            TextField txtConfirmMaster = createTextField("", TEXT_FIELD_STYLE);

            //Label
            Label lblConfirmMaster = createLabel("Confirm master password", LOGIN_TITLE_STYLE);

            // Set the title of the view passwords window
            popupStage.setTitle("CypherGuard - Confirm Master Password");

            // Add components to the popup layout
            popupLayout.getChildren().addAll(lblConfirmMaster, txtConfirmMaster, btnConfirm, btnClose);

            Scene popupScene = new Scene(popupLayout, 400, 400);
            popupScene.getStylesheets().add(getClass().getResource("/resources/styles.css").toExternalForm());
            popupStage.setScene(popupScene);

            popupStage.show();

            btnConfirm.setOnAction(confirmEvent -> {
                String currentUser = UserSession.getInstance().getUsername();
                String inputMaster = txtConfirmMaster.getText();
                Boolean isAuthenticated = Key.authenticate(inputMaster, Database.getMaster(currentUser), Database.getSalt(currentUser));

                if (isAuthenticated == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect master password.");
                    alert.showAndWait();
                    return;
                }

                boolean isDeleted = Database.deletePassword(txtUrl.getText());

                if (isDeleted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password deleted successfully!");
                    alert.showAndWait();
                    // Clear the obersvable list
                    observableList.clear();
                    // Repopulate with udpated data
                    passwords = Database.getUserPasswords();
                    observableList.addAll(passwords);
                    // Select the first item in the ListView
                    passwordListView.getSelectionModel().selectFirst();

                } else {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to delete password.");
                    alert.showAndWait();
                }

                // Close the confirm master stage
                popupStage.close();
            });

            btnClose.setOnAction(closePopUpEvent -> {
                // Close the confirm master stage
                popupStage.close();
            });
        });

        btnBack.setOnAction(backEvent -> {
            // Set the scene to the previous scene
            stage.setScene(previousScene);
        });

        btnAddPasswordPopup.setOnAction(openPopUpEvent -> {

            // Create a new AddPasswordPopup
            Stage popupStage = new Stage();

            // Create layout for popup
            VBox popupLayout = new VBox(SPACING);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setPadding(new Insets(PADDING));

            // Buttons
            Button btnSave = createButton("Save Password", ACTION_BUTTON_STYLE);
            Button btnClose = createButton("Back", BACK_BUTTON_STYLE);

            // Text Fields
            TextField txtWebsiteName = createTextField("Enter the Wesbite's name...", TEXT_FIELD_STYLE);
            TextField txtAddedUserName = createTextField("Enter your Username...", TEXT_FIELD_STYLE);
            TextField txtAddedPassword = createTextField("Enter your Password...", TEXT_FIELD_STYLE);

            // Create a label for the view passwords title
            Label lblViewPasswordsTitle = createLabel("Add a new password", LOGIN_TITLE_STYLE);

            // Set the title of the view passwords window
            popupStage.setTitle("CypherGuard - Add a new Password");

            // Add components to the popup layout
            popupLayout.getChildren().addAll(lblViewPasswordsTitle, txtWebsiteName, txtAddedUserName, txtAddedPassword,
                    btnSave, btnClose);

            Scene popupScene = new Scene(popupLayout, 400, 400);

            try {
            
                if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                    popupScene.getStylesheets().add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                    popupStage.setScene(popupScene);
                } else { // light mode is on
                    popupScene.getStylesheets().add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                    popupStage.setScene(popupScene);
                }
    
            } catch (Exception e) {
                System.err.println("Failed to load CSS file: " + e.getMessage());
            }

            popupStage.show();

            btnSave.setOnAction(saveInputevent -> {

                String website = txtWebsiteName.getText();
                String appUsername = txtAddedUserName.getText();
                String appPassword = txtAddedPassword.getText();
                String encrypted;

                // Try to parse the input as a URI
                try {
                    URI uri = new URI(website);
                    website = uri.getHost();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                if (website.isEmpty() || appUsername.isEmpty() || appPassword.isEmpty()) {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.");
                    alert.showAndWait();
                    return;
                }

                String currentUser = UserSession.getInstance().getUsername();
                try {
                    String key = Key.deriveKey(Database.getMaster(currentUser), Database.getSalt(currentUser));
                    encrypted = AES.encrypt(appPassword, key);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to encrypt password, please try again.");
                    alert.showAndWait();
                    return;
                }

                boolean isAdded = Database.addPassword(appUsername, website, encrypted);

                if (isAdded) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Credentials saved successfully!");
                    alert.showAndWait();
                    // Clear the obersvable list
                    observableList.clear();
                    // Repopulate with udpated data
                    passwords = Database.getUserPasswords();
                    observableList.addAll(passwords);
                    // Select the first item in the ListView
                    passwordListView.getSelectionModel().selectFirst();

                    //Clear the text fields in case user wants to add another password
                    txtWebsiteName.clear();
                    txtAddedUserName.clear();
                    txtAddedPassword.clear();

                } else {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save credentials.");
                    alert.showAndWait();
                }
            });

            btnClose.setOnAction(closePopUpEvent -> {
                // Close the view passwords stage
                popupStage.close();
            });
        });

        return Arrays.asList(btnAddPasswordPopup, btnBack, btnEditPassword, btnDeletePassword);
    }

    private void setupListView() {
        // Get the passwords from the database
        passwords = Database.getUserPasswords();

        observableList = FXCollections.observableArrayList(passwords);

        // Add the passwords to the ListView
        passwordListView.setItems(observableList);

        // Initialize the TextField values
        txtUsername = new TextField();
        txtPassword = new TextField();
        txtUrl = new TextField();
        favicon = new ImageView();
        lblWebsiteName = new Label();

        txtUsername.setEditable(false);
        txtPassword.setEditable(false);
        txtUrl.setEditable(false);

        // Cell factory to accomodate favicons and labels
        passwordListView.setCellFactory(lv -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            private final Label lblPlatform = new Label();
            private final Label lblUsername = new Label();
            private final VBox vboxLabels = new VBox(16, lblPlatform, lblUsername);
            private final HBox hbox = new HBox(16, imageView, vboxLabels);

            {
                hbox.setAlignment(Pos.CENTER_LEFT);
                imageView.setFitHeight(32);
                imageView.setFitWidth(32);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Map<String, String> password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setGraphic(null);
                } else {
                    String websiteName = password.get("platform");
                    if (websiteName.startsWith("www.")) {
                        websiteName = websiteName.substring(4);
                    }
                    if (websiteName.contains(".")) {
                        websiteName = websiteName.substring(0, websiteName.indexOf('.'));
                    }
                    if (websiteName != null && !websiteName.isEmpty()) {
                        websiteName = websiteName.substring(0, 1).toUpperCase()
                                + websiteName.substring(1).toLowerCase();
                    }

                    lblPlatform.setText(websiteName);
                    lblUsername.setText(password.get("username"));

                    String faviconUrl = "https://logo.clearbit.com/" + password.get("platform") + "?size=180";
                    Image image = new Image(faviconUrl, true);
                    imageView.setImage(image);

                    setGraphic(hbox);
                }
            }
        });

        passwordListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtUsername.setText(newSelection.get("username"));

                String user = UserSession.getInstance().getUsername();
                String decryptedPassword = "Error decrypting password";
                // Decrypt the password
                try{
                    String key = Key.deriveKey(Database.getMaster(user), Database.getSalt(user));
                    decryptedPassword = AES.decrypt(newSelection.get("password"), key);
                }
                catch (Exception e) {
                    System.out.println("Error decrypting password: " + e.getMessage());
                }
                txtPassword.setText(decryptedPassword);
                txtUrl.setText(newSelection.get("platform"));

                String websiteName = newSelection.get("platform");
                if (websiteName.startsWith("www.")) {
                    websiteName = websiteName.substring(4);
                }
                if (websiteName.contains(".")) {
                    websiteName = websiteName.substring(0, websiteName.indexOf('.'));
                }
                if (websiteName != null && !websiteName.isEmpty()) {
                    websiteName = websiteName.substring(0, 1).toUpperCase() + websiteName.substring(1).toLowerCase();
                }

                lblWebsiteName.setText(websiteName);

                String faviconUrl = "https://logo.clearbit.com/" + newSelection.get("platform") + "?size=180";
                Image image = new Image(faviconUrl, true);

                favicon.setImage(image);
                favicon.setFitHeight(32);
                favicon.setFitWidth(32);
                favicon.setPreserveRatio(true);
            }
        });
    }

    private void setupSearchBar(){
        txtSearch = new TextField();
        //Create a filtered list
        FilteredList<Map<String, String>> filteredData = new FilteredList<>(observableList, p -> true);

        //Set the filter Predicate whenever the filter changes
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(password -> {
                //If filter text is empty, display all passwords
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                //Compare the platform and username of every password with filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (password.get("platform").toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                } 
                else if (password.get("username").toLowerCase().contains(lowerCaseFilter)) {
                    return true; 
                }
                return false; //Does not match
            });
        });

        //Add sorted/filtered data to the table
        passwordListView.setItems(filteredData);
    }
}