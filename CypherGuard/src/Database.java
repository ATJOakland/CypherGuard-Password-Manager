import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {
    private static final String DB_PATH = "jdbc:sqlite:CypherGuard\\lib\\database.db";
    private static final String USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, master_password TEXT NOT NULL, salt TEXT NOT NULL)";
    private static final String PASSWORDS_TABLE = "CREATE TABLE IF NOT EXISTS passwords (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, platform TEXT NOT NULL, password TEXT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id))";

    public static void initializeDatabase() {
        
        //Check for JDBC Driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        //Connect to database
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement()) {
            stmt.execute(USERS_TABLE);
            stmt.execute(PASSWORDS_TABLE);
            System.out.println("Database initialized successfully.");
            
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    //Method to retrieve password for given username and platform.
    public static String getPasswordByUsername(String username, String platform) {
        String password = null;
        String query = "SELECT password FROM users INNER JOIN passwords ON users.id = passwords.user_id WHERE username = '" + username + "' AND platform = '"+ platform +"'";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                password = rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
        return password;
    }

    //Method to create a new user
    public static void createUser(String username, String password, String salt){
        String query = "INSERT INTO users (username, master_password, salt) VALUES ('" + username + "', '" + password + "', '" + salt + "')";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    //Method to add a new password a user
    public static void addPassword(String username, String platform, String password){
        String query = "INSERT INTO passwords (user_id, platform, password) VALUES ((SELECT id FROM users WHERE username = '" + username + "'), '" + platform + "', '" + password + "')";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

        } catch (SQLException e) {
            System.out.println("Error adding password: " + e.getMessage());
        }
    }

    //Method to update an existing password for a user
    public static void updatePassword(String username, String platform, String password){
        String query = "UPDATE passwords SET password = '" + password + "' WHERE user_id = (SELECT id FROM users WHERE username = '" + username + "') AND platform = '" + platform + "'";
        try (Connection conn = DriverManager.getConnection(DB_PATH);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

        } catch (SQLException e) {
            System.out.println("Error updating project: " + e.getMessage());
        }
    }

}
