package main;

import java.nio.file.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String DB_PATH = "jdbc:sqlite:CypherGuard\\lib\\database.db";

    private static final String USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                master_password TEXT NOT NULL,
                salt TEXT NOT NULL UNIQUE
            )""";

    private static final String PASSWORDS_TABLE = """
            CREATE TABLE IF NOT EXISTS passwords (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                username TEXT NOT NULL,
                platform TEXT NOT NULL,
                password TEXT NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )""";

    public static void initializeDatabase() {

        // Check for JDBC Driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        // Create database file if it does not exist
        Path dbPath = Paths.get("CypherGuard\\lib\\database.db");
        if (!Files.exists(dbPath)) {
            try {
                Files.createFile(dbPath);
            } catch (Exception e) {
                System.out.println("Error creating database file: " + e.getMessage());
            }
        }

        // Connect to database
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement()) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            stmt.execute(USERS_TABLE);
            stmt.execute(PASSWORDS_TABLE);
            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    // Method to retrieve username and password for given platform.
    public static String[] getPlatformInfo(String platform) {
        // Get current logged in user
        String loggedInUser = UserSession.getInstance().getUsername();
        String username = null;
        String password = null;

        String query = "SELECT username, password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, loggedInUser);
            pstmt.setString(2, platform);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
        return new String[] { username, password };
    }

    // Method to create a new user
    public static boolean createUser(String username, String hashedMasterPassword, String salt) {

        String query = "INSERT INTO users (username, master_password, salt) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);
            pstmt.setString(2, hashedMasterPassword);
            pstmt.setString(3, salt);

            pstmt.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    // Method to add a new password a user
    public static Boolean addPassword(String username, String platform, String password) {
        // Get current logged in user
        String loggedInUser = UserSession.getInstance().getUsername();

        String query = "INSERT INTO passwords (user_id, platform, password, username) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, loggedInUser);
            pstmt.setString(2, platform);
            pstmt.setString(3, password);
            pstmt.setString(4, username);

            pstmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error adding password: " + e.getMessage());
            return false;
        }
    }

    // Method to update an existing password for a user
    public static boolean updatePassword(String platform, String password, String username) {

        String query = "UPDATE passwords SET password = ? WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, password);
            pstmt.setString(2, username);
            pstmt.setString(3, platform);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    // Method to update an existing username for a password
    public static boolean updateUsername(String platform, String username) {
        // Get current logged in user
        String loggedInUser = UserSession.getInstance().getUsername();

        String query = "UPDATE passwords SET username = ? WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);
            pstmt.setString(2, loggedInUser);
            pstmt.setString(3, platform);

            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    // Method to delete a password for a user
    public static boolean deletePassword(String platform, String username) {
        // Get current logged in user
        String loggedInUser = UserSession.getInstance().getUsername();

        String query = "DELETE FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ? AND username = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, loggedInUser);
            pstmt.setString(2, platform);
            pstmt.setString(3, username);

            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting password: " + e.getMessage());
            return false;
        }
    }

    // Method to delete a user
    public static boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // Method to retrieve hashed salt for a user
    public static String getSalt(String username) {
        String salt = null;
        String query = "SELECT salt FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                salt = rs.getString("salt");
            } else {
                System.out.println("No user found with username: " + username);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving salt: " + e.getMessage());
        }

        if (salt == null) {
            System.out.println("Salt is null for user: " + username);
        }

        return salt;
    }

    // Method to retrieve hashed master password for a user
    public static String getMaster(String username) {
        String master = null;
        String query = "SELECT master_password FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_PATH);
                Statement stmt = conn.createStatement();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Enable SQLite foreign key contraints
            stmt.execute("PRAGMA foreign_keys = ON;");

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                master = rs.getString("master_password");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving master password: " + e.getMessage());
        }

        return master;
    }

    public static List<Map<String, String>> getUserPasswords() {
        List<Map<String, String>> passwords = new ArrayList<>();

        // Get current logged in user
        String loggedInUser = UserSession.getInstance().getUsername();

        String query = "SELECT platform, username, password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?)";

        try (Connection conn = DriverManager.getConnection(DB_PATH);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                
            pstmt.setString(1, loggedInUser);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, String> passwordEntry = new HashMap<>();
                passwordEntry.put("platform", rs.getString("platform"));
                passwordEntry.put("username", rs.getString("username"));
                passwordEntry.put("password", rs.getString("password"));
                passwords.add(passwordEntry);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving passwords: " + e.getMessage());
        }

        return passwords;
    }
}