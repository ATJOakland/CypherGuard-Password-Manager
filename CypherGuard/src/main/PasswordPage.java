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
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class PasswordPage extends GridPane {
    private static final int SPACING = 20;
    private static final int PADDING = 20;
    private static String CSS_FILE_PATH;
    private static final String ACTION_BUTTON_STYLE = "action-button";
    private static final String BACK_BUTTON_STYLE = "back-button";
    private static final String LOGIN_TITLE_STYLE = "login-title";
    private static final String TEXT_FIELD_STYLE = "text-field";
    private static final String LIST_CELL_STYLE = "list-cell";

    static {
        try {
            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                CSS_FILE_PATH = "/resources/dark-styles.css";
            } else { // light mode is on
                CSS_FILE_PATH = "/resources/light-styles.css";
            }
        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }
    }

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

    private String originalPassword;
    private String hiddenPassword;
    private String originalUsername;

    public PasswordPage(Stage stage, Scene previousScene) {

        this.stage = stage;
        List<Pane> layout = setupLayout();
        Pane vboxLeft = layout.get(0);
        Pane vboxRight = layout.get(1);
        Pane topLeftHBox = layout.get(2);
        Pane topRightHBox = layout.get(3);
        Pane selectionVBox = layout.get(4);
        Pane topSelectionHBox = layout.get(5);
        Pane passwordHBox = layout.get(6);

        // Styling for content
        vboxLeft.getStyleClass().add("password-info-container-left");
        vboxRight.getStyleClass().add("password-info-container-right");
        topLeftHBox.getStyleClass().add("interest-box-container");
        topRightHBox.getStyleClass().add("interest-box-container");
        selectionVBox.getStyleClass().add("interest-box-container");
        topSelectionHBox.getStyleClass().add("selected-box-container");

        List<Button> buttons = setupButtons(previousScene);
        Button btnAddPasswordPopup = buttons.get(0);
        Button btnBack = buttons.get(1);
        Button btnEditPassword = buttons.get(2);
        Button btnDeletePassword = buttons.get(3);
        Button btnUnHidePassword = buttons.get(4);
        Button btnGeneratePassword = buttons.get(5);
        Button btnCancel = buttons.get(6);

        btnBack.setMinWidth(75);
        btnDeletePassword.setMinWidth(75);
        btnEditPassword.setMinWidth(75);

        // Labels
        Label lblWebsite = createLabel("Website: ", "label");
        Label lblUsername = createLabel("Username: ", "label");
        Label lblPassword = createLabel("Password: ", "label");

        // Create a ListView for the passwords
        passwordListView = new ListView<>();
        setupListView();

        // Search bar
        setupSearchBar();

        // Lock text fields
        txtUsername.setEditable(false);
        txtPassword.setEditable(false);
        txtUrl.setEditable(false);
        btnCancel.setVisible(false);

        passwordListView.getSelectionModel().selectFirst();
        passwordHBox.getChildren().addAll(txtPassword, btnUnHidePassword);
        topSelectionHBox.getChildren().addAll(favicon, lblWebsiteName);
        selectionVBox.getChildren().addAll(topSelectionHBox, lblUsername, txtUsername, lblPassword, passwordHBox,
                lblWebsite, txtUrl);
        topRightHBox.getChildren().addAll(btnCancel, btnEditPassword, btnGeneratePassword, btnDeletePassword, btnBack);
        topLeftHBox.getChildren().addAll(txtSearch, btnAddPasswordPopup);
        vboxLeft.getChildren().addAll(topLeftHBox, passwordListView);
        vboxRight.getChildren().addAll(topRightHBox, selectionVBox);

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

        HBox passwordHBox = new HBox(SPACING);

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
        passwordHBox.setAlignment(Pos.CENTER_LEFT);

        // Set the title of the view passwords window
        stage.setTitle("CypherGuard - View Passwords");

        return Arrays.asList(vboxLeft, vboxRight, topLeftHBox, topRightHBox, selectionVBox, topSelectionHBox,
                passwordHBox);
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

        Button btnAddPasswordPopup = createButton("Add a Password", ACTION_BUTTON_STYLE);
        Button btnBack = createButton("Back", BACK_BUTTON_STYLE);
        Button btnEditPassword = createButton("Edit", ACTION_BUTTON_STYLE);
        Button btnDeletePassword = createButton("Delete", ACTION_BUTTON_STYLE);
        Button btnGeneratePassword = createButton("Generate", ACTION_BUTTON_STYLE);
        Button btnUnHidePassword = createButton("Unhide", ACTION_BUTTON_STYLE);
        Button btnCancel = createButton("Cancel", BACK_BUTTON_STYLE);

        btnAddPasswordPopup.setMinWidth(100);

        btnUnHidePassword.setOnAction(unhideEvent -> {
            if (btnUnHidePassword.getText().equals("Unhide")) {
                txtPassword.setText(originalPassword);
                btnUnHidePassword.setText("Hide");
            } else if (btnUnHidePassword.getText().equals("Hide")) {
                txtPassword.setText(hiddenPassword);
                btnUnHidePassword.setText("Unhide");
            }
        });

        btnCancel.setOnAction(cancelEvent -> {
            txtPassword.setText(hiddenPassword);
            txtUsername.setText(originalUsername);
            btnEditPassword.setText("Edit");
            btnCancel.setVisible(false);
            btnUnHidePassword.setText("Unhide");
            btnUnHidePassword.setVisible(true);
        });

        btnGeneratePassword.setOnAction(generateEvent -> {

            // Confirm master password before allowing to edit
            Map<String, Object> components = setupPopup("confirm");

            Button btnConfirm = (Button) components.get("btnConfirm");
            TextField txtConfirmMaster = (TextField) components.get("txtConfirmMaster");
            Stage popupStage = (Stage) components.get("popupStage");

            btnConfirm.setOnAction(confirmEvent -> {
                String currentUser = UserSession.getInstance().getUsername();
                String inputMaster = txtConfirmMaster.getText();
                Boolean isAuthenticated = Key.authenticate(inputMaster, Database.getMaster(currentUser),
                        Database.getSalt(currentUser));

                if (isAuthenticated == false) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect master password.");
                    alert.showAndWait();
                    return;
                }
                popupStage.close();

                // Open the genrate password popup
                Map<String, Object> generateComponents = setupPopup("generate");
                Button btnGenerate = (Button) generateComponents.get("btnGenerate");
                Button btnConfirmGenerated = (Button) generateComponents.get("btnConfirmGenerated");
                TextField txtLength = (TextField) generateComponents.get("txtLength");
                TextField txtGeneratedPassword = (TextField) generateComponents.get("txtGeneratedPassword");
                Stage generatePopupStage = (Stage) generateComponents.get("popupStage");

                // Prevent non-numeric input in txtLength
                txtLength.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        txtLength.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                btnGenerate.setOnAction(generateNewEvent -> {
                    int length = 0;
                    if (!txtLength.getText().isEmpty()) {
                        length = Integer.parseInt(txtLength.getText());
                    }

                    if (length < 14) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "A password is recommended to be at least 14 characters long!");
                        alert.showAndWait();
                        return;
                    }

                    String generatedPassword = Key.generatePassword(length);
                    txtGeneratedPassword.setText(generatedPassword);
                });

                btnConfirmGenerated.setOnAction(confirmGeneratedEvent -> {
                    if (txtGeneratedPassword.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Please generate a password first.");
                        alert.showAndWait();
                        return;
                    }

                    txtPassword.setText(txtGeneratedPassword.getText());
                    btnUnHidePassword.setText("Hide");

                    String encrypted = null;

                    // Add new password to database
                    try {
                        encrypted = AES.encrypt(txtGeneratedPassword.getText(),
                                Key.deriveKey(Database.getMaster(UserSession.getInstance().getUsername()),
                                        Database.getSalt(UserSession.getInstance().getUsername())));
                    } catch (Exception e) {
                        System.out.println("Error encrypting password: " + e.getMessage());
                    }
                    Database.updatePassword(txtUrl.getText(), encrypted);

                    generatePopupStage.close();
                });
            });
        });

        btnEditPassword.setOnAction(editEvent -> {
            if (btnEditPassword.getText().equals("Edit")) {

                // Confirm master password before allowing to edit
                Map<String, Object> components = setupPopup("confirm");

                Button btnConfirm = (Button) components.get("btnConfirm");
                TextField txtConfirmMaster = (TextField) components.get("txtConfirmMaster");
                Stage popupStage = (Stage) components.get("popupStage");

                btnConfirm.setOnAction(confirmEvent -> {
                    String currentUser = UserSession.getInstance().getUsername();
                    String inputMaster = txtConfirmMaster.getText();
                    Boolean isAuthenticated = Key.authenticate(inputMaster, Database.getMaster(currentUser),
                            Database.getSalt(currentUser));

                    if (isAuthenticated == false) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect master password.");
                        alert.showAndWait();
                        return;
                    }
                    // Close the confirm master stage
                    popupStage.close();

                    txtUsername.setEditable(true);
                    txtPassword.setEditable(true);
                    txtPassword.setText(originalPassword);
                    btnEditPassword.setText("Save");
                    btnCancel.setVisible(true);
                    btnUnHidePassword.setText("Hide");
                    btnUnHidePassword.setVisible(false);
                });
            } else if (btnEditPassword.getText().equals("Save")) {

                txtUsername.setEditable(false);
                txtPassword.setEditable(false);
                //Get input and hide it
                String input = txtPassword.getText();
                txtPassword.setText(input.replaceAll(".", "*"));

                btnEditPassword.setText("Edit");
                btnCancel.setVisible(false);
                btnUnHidePassword.setVisible(true);

                // Encrypt the password
                String currentUser = UserSession.getInstance().getUsername();
                String encryptedPassword = null;

                try {
                    String key = Key.deriveKey(Database.getMaster(currentUser), Database.getSalt(currentUser));
                    encryptedPassword = AES.encrypt(input, key);
                } catch (Exception e) {
                    System.out.println("Error decrypting password: " + e.getMessage());
                }

                boolean isUpdated = Database.updatePassword(txtUrl.getText(), encryptedPassword);
                isUpdated = Database.updateUsername(txtUrl.getText(), txtUsername.getText());

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
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update credentials.");
                    alert.showAndWait();
                }
            }
        });

        btnDeletePassword.setOnAction(deleteEvent -> {

            Map<String, Object> components = setupPopup("confirm");

            Button btnConfirm = (Button) components.get("btnConfirm");
            TextField txtConfirmMaster = (TextField) components.get("txtConfirmMaster");
            Stage popupStage = (Stage) components.get("popupStage");

            btnConfirm.setOnAction(confirmEvent -> {
                String currentUser = UserSession.getInstance().getUsername();
                String inputMaster = txtConfirmMaster.getText();
                Boolean isAuthenticated = Key.authenticate(inputMaster, Database.getMaster(currentUser),
                        Database.getSalt(currentUser));

                if (isAuthenticated == false) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect master password.");
                    alert.showAndWait();
                    return;
                }

                boolean isDeleted = Database.deletePassword(txtUrl.getText(), txtUsername.getText());

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

        });

        btnBack.setOnAction(backEvent -> {
            // Set the scene to the previous scene
            stage.setScene(previousScene);
        });

        btnAddPasswordPopup.setOnAction(openPopUpEvent -> {

            Map<String, Object> components = setupPopup("add");

            Button btnSave = (Button) components.get("btnSave");
            TextField txtWebsiteName = (TextField) components.get("txtWebsiteName");
            TextField txtAddedUserName = (TextField) components.get("txtAddedUserName");
            TextField txtAddedPassword = (TextField) components.get("txtAddedPassword");

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

                    // Clear the text fields in case user wants to add another password
                    txtWebsiteName.clear();
                    txtAddedUserName.clear();
                    txtAddedPassword.clear();

                } else {
                    // Show error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save credentials.");
                    alert.showAndWait();
                }
            });
        });

        return Arrays.asList(btnAddPasswordPopup, btnBack, btnEditPassword, btnDeletePassword, btnUnHidePassword,
                btnGeneratePassword, btnCancel);
    }

    private void setupListView() {
        // Get the passwords from the database
        passwords = Database.getUserPasswords();

        observableList = FXCollections.observableArrayList(passwords);

        // Add the passwords to the ListView
        passwordListView.setItems(observableList);

        // Apply CSS to the ListView
        passwordListView.getStyleClass().add("password-list-view");

        // Initialize the TextField values
        txtUsername = new TextField();
        txtPassword = new TextField();
        txtUrl = new TextField();
        favicon = new ImageView();
        lblWebsiteName = new Label();

        txtUsername.setEditable(false);
        txtPassword.setEditable(false);
        txtUrl.setEditable(false);
        txtPassword.setPrefWidth(380);

        Map<String, Image> faviconCache = new HashMap<>();

        // Pre-load the favicons
        for (Map<String, String> password : observableList) {
            String faviconUrl = "https://logo.clearbit.com/" + password.get("platform") + "?size=180";
            Image image = new Image(faviconUrl, true);
            faviconCache.put(password.get("platform"), image);
        }

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
                hbox.getStyleClass().add(LIST_CELL_STYLE);
            }

            @Override
            protected void updateItem(Map<String, String> password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setGraphic(null);
                } else {
                    String websiteName = password.get("platform");
                    if (websiteName.startsWith("www.")) {
                        websiteName = websiteName.replaceFirst("^www\\.", "");
                    }
                    if (websiteName.contains(".")) {
                        websiteName = websiteName.substring(0, websiteName.lastIndexOf('.'));
                    }
                    if (websiteName != null && !websiteName.isEmpty()) {
                        websiteName = websiteName.substring(0, 1).toUpperCase()
                                + websiteName.substring(1).toLowerCase();
                    }

                    lblPlatform.setText(websiteName);
                    lblUsername.setText(password.get("username"));

                    Image image = faviconCache.get(password.get("platform"));
                    if (image == null) {
                        String faviconUrl = "https://logo.clearbit.com/" + password.get("platform") + "?size=180";
                        image = new Image(faviconUrl, true);
                        faviconCache.put(password.get("platform"), image);
                    }

                    imageView.setImage(image);

                    setGraphic(hbox);
                }
            }
        });

        passwordListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtUsername.setText(newSelection.get("username"));
                originalUsername = newSelection.get("username");

                String user = UserSession.getInstance().getUsername();
                String decryptedPassword = "Error decrypting password";
                // Decrypt the password
                try {
                    String key = Key.deriveKey(Database.getMaster(user), Database.getSalt(user));
                    decryptedPassword = AES.decrypt(newSelection.get("password"), key);
                } catch (Exception e) {
                    System.out.println("Error decrypting password: " + e.getMessage());
                }

                originalPassword = decryptedPassword;
                //Hide the password
                hiddenPassword = decryptedPassword.replaceAll(".", "*");
                txtPassword.setText(hiddenPassword);

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

    private void setupSearchBar() {
        txtSearch = new TextField();
        // Create a filtered list
        FilteredList<Map<String, String>> filteredData = new FilteredList<>(observableList, p -> true);

        // Create a PauseTransition for debouncing
        PauseTransition pause = new PauseTransition(Duration.millis(200));

        double textWidth = 250;

        txtSearch.setPromptText("Search Saved Passwords...");
        txtSearch.setMinWidth(textWidth);

        // Set the filter Predicate whenever the filter changes
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            // On each text change, restart the PauseTransition
            pause.setOnFinished(event -> {
                filteredData.setPredicate(password -> {
                    // If filter text is empty, display all passwords
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare the platform and username of every password with filter text
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (password.get("platform").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (password.get("username").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false; // Does not match
                });
            });
            pause.playFromStart();
        });

        // Add sorted/filtered data to the table
        passwordListView.setItems(filteredData);
    }

    private Map<String, Object> setupPopup(String type) {
        Stage popupStage = new Stage();
        Map<String, Object> components = new HashMap<>();

        // Create layout for popup
        VBox popupLayout = new VBox(SPACING);
        popupLayout.setAlignment(Pos.CENTER);
        popupLayout.setPadding(new Insets(PADDING));

        // Always create back button
        Button btnClose = createButton("Back", BACK_BUTTON_STYLE);

        // Include popupStage incase of alternative exit
        components.put("popupStage", popupStage);

        switch (type) {
            case "add":
                // Buttons
                Button btnSave = createButton("Save Password", ACTION_BUTTON_STYLE);

                // Text Fields
                TextField txtWebsiteName = createTextField("Enter the Wesbite's name...", TEXT_FIELD_STYLE);
                TextField txtAddedUserName = createTextField("Enter your Username...", TEXT_FIELD_STYLE);
                TextField txtAddedPassword = createTextField("Enter your Password...", TEXT_FIELD_STYLE);

                // Create a label for the view passwords title
                Label lblViewPasswordsTitle = createLabel("Add a new password", LOGIN_TITLE_STYLE);

                // Set the title of the view passwords window
                popupStage.setTitle("CypherGuard - Add a new Password");

                // Add components to the popup layout
                popupLayout.getChildren().addAll(lblViewPasswordsTitle, txtWebsiteName, txtAddedUserName,
                        txtAddedPassword,
                        btnSave, btnClose);

                // Add componenets to the map
                components.put("btnSave", btnSave);
                components.put("txtWebsiteName", txtWebsiteName);
                components.put("txtAddedUserName", txtAddedUserName);
                components.put("txtAddedPassword", txtAddedPassword);

                break;

            case "confirm":

                // Buttons
                Button btnConfirm = createButton("Confirm", ACTION_BUTTON_STYLE);

                // Text Fields
                TextField txtConfirmMaster = createTextField("", TEXT_FIELD_STYLE);

                // Label
                Label lblConfirmMaster = createLabel("Confirm master password", LOGIN_TITLE_STYLE);

                // Set the title of the view passwords window
                popupStage.setTitle("CypherGuard - Confirm Master Password");

                // Add components to the popup layout
                popupLayout.getChildren().addAll(lblConfirmMaster, txtConfirmMaster, btnConfirm, btnClose);

                // Add componenets to the map
                components.put("btnConfirm", btnConfirm);
                components.put("txtConfirmMaster", txtConfirmMaster);

                break;

            case "generate":

                Button btnGenerate = createButton("Generate", ACTION_BUTTON_STYLE);
                Button btnConfirmGenerated = createButton("Confirm", ACTION_BUTTON_STYLE);
                TextField txtLength = createTextField("Minimum length of 8 characters...", TEXT_FIELD_STYLE);
                Label lblLength = createLabel("Desired password length", LOGIN_TITLE_STYLE);
                Label lblGeneratedPassword = createLabel("Generated Password", LOGIN_TITLE_STYLE);
                TextField txtGeneratedPassword = createTextField("", TEXT_FIELD_STYLE);

                // Set the title of the view passwords window
                popupStage.setTitle("CypherGuard - Generate New Password");
                popupLayout.getChildren().addAll(lblLength, txtLength, lblGeneratedPassword, txtGeneratedPassword,
                        btnGenerate, btnConfirmGenerated, btnClose);

                components.put("btnConfirmGenerated", btnConfirmGenerated);
                components.put("txtLength", txtLength);
                components.put("txtGeneratedPassword", txtGeneratedPassword);
                components.put("btnGenerate", btnGenerate);
        }

        Scene popupScene = new Scene(popupLayout, 500, 500);

        try {

            if (CypherGuard.isLightModeEnabled(CypherGuard.class) == false) {
                popupScene.getStylesheets()
                        .add(getClass().getResource("/resources/dark-styles.css").toExternalForm());
                popupStage.setScene(popupScene);
            } else { // light mode is on
                popupScene.getStylesheets()
                        .add(getClass().getResource("/resources/light-styles.css").toExternalForm());
                popupStage.setScene(popupScene);
            }

        } catch (Exception e) {
            System.err.println("Failed to load CSS file: " + e.getMessage());
        }

        btnClose.setOnAction(closePopUpEvent -> {
            // Close the view passwords stage
            popupStage.close();
        });

        popupStage.show();

        return components;
    }
}