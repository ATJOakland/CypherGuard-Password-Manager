package main;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserAuthentication {

    private static final String DB_PATH = "jdbc:sqlite:CypherGuard\\\\lib\\\\database.db";

    public static void main(String[] args) {

        // Create account
        try {
            createUser("example_user", "example_password");
            System.out.println("Account created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }

        // Login
        boolean loginSuccessful = login("example_user", "example_password");
        if (loginSuccessful) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Incorrect username or password.");
        }
    }

    public static void createUser(String username, String masterPassword)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, SQLException {

        String query = "INSERT INTO users (username, master_password, salt) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String salt = Key.generateSalt();
            String hashedPassword = Key.deriveKey(masterPassword, salt);

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, salt);

            pstmt.executeUpdate();

        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean login(String username, String password) {
        String query = "SELECT master_password, salt FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("master_password");
                String salt = rs.getString("salt");
                String hashedPassword = Key.deriveKey(password, salt);

                return storedPassword.equals(hashedPassword);
            }

        } catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            System.out.println("Error during login: " + e.getMessage());
        }

        return false;
    }
}







