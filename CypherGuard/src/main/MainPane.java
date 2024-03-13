package main;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;

public class MainPane extends GridPane {

    public MainPane() {
        // Center vertically
        setAlignment(Pos.CENTER);

        // Logo Code
        File file = new File("img/CypherGuardLogo.png");
        Image logoImg = new Image(file.toURI().toString());
        ImageView logoImageView = new ImageView(logoImg);
        add(logoImageView, 0, 0);

        // Buttons
        Button btnSavePassword = new Button("Save a Password");
        Button btnViewPassword = new Button("Look at Passwords");
        Button btnSaveNote = new Button("Save a Note");
        Button btnClose = new Button("Close");

        // CSS Styling
        btnSavePassword.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnViewPassword.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        btnSaveNote.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnClose.setStyle("-fx-background-color: #555555; -fx-text-fill: white;");

        // Button actions
        btnClose.setOnAction(e -> Platform.exit());

        // Button Pane Design
        VBox buttonBox = new VBox(10); // Spacing between buttons
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.getChildren().addAll(btnSavePassword, btnViewPassword, btnSaveNote, btnClose);
        add(buttonBox, 0, 2);

        // Set column constraints to make the button pane wider
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        getColumnConstraints().add(columnConstraints);

        // Background styling
        setStyle("-fx-background-color: #091232");
    }
}
