package main;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javafx.scene.control.Alert;

public class UserAuthentication {

    public static boolean loginUser(String username, String password) {
        try {
            String salt = Database.getSalt(username);
            String hashedPassword = Key.deriveKey(password, salt);
            String storedPassword = Database.getMaster(username);
            if (hashedPassword.equals(storedPassword)) {
                return true; // Passwords match
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return false; // Invalid credentials
    }

    public static boolean createUser(String username, String password) {
        try {
            if (Database.getMaster(username) != null) {
                // User already exists
                return false;
            }
            // User does not exist, create new user
            Database.createUser(username, password);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }
}
