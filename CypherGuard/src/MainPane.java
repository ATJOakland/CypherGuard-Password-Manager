//************************************************************************
// This Script creates the main options pane. It shows the main button options for
// the user to choose what they want to do.
//************************************************************************

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class MainPane extends GridPane {

    // Create GUI elements of the main menu
    public MainPane() {
        
        // Center vertically
        setAlignment(Pos.CENTER);

        // Title Label
        Label titleLabel = new Label("Cypher Guard"); // Label Text
        titleLabel.setFont(Font.font("Arial", 48)); // Font and Size
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE); // Font Color
        setHalignment(titleLabel, HPos.CENTER); // Center the text
        add(titleLabel, 0, 0, 2, 1); // Span it two columns

        // Buttons
        Button savePasswordButton = new Button("Save a Password");
        Button viewPasswordsButton = new Button("Look at Passwords");
        Button saveNoteButton = new Button("Save a Note");

        // Arrage the buttons in the center
        GridPane buttonPane = new GridPane();
        buttonPane.add(savePasswordButton, 0, 0);
        buttonPane.add(viewPasswordsButton, 1, 0);
        buttonPane.add(saveNoteButton, 2, 0);
        setHalignment(buttonPane, HPos.RIGHT);
        add(buttonPane, 0, 1, 2, 1); // Add buttonPane to the main grid, spanning two columns
        
        setStyle("-fx-background-color: #091232"); // Sets background to be dark blue
    }
}
