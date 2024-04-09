package test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

import org.junit.jupiter.api.Assertions;
import main.Database;
import main.UserSession;

public class DatabaseTest {

    private static Connection connection;

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

    @BeforeAll
    public static void setUp() throws SQLException{

        //Create database file if it does not exist
        Path dbPath = Paths.get("CypherGuard\\lib\\testdatabase.db");
        if (!Files.exists(dbPath)) {
            try {
                Files.createFile(dbPath);
            } catch (Exception e) {
                System.out.println("Error creating database file: " + e.getMessage());
            }
        }

        connection = DriverManager.getConnection("jdbc:sqlite:CypherGuard\\lib\\testdatabase.db");

        try (Statement statement = connection.createStatement()) {
            int x, y;
            statement.executeUpdate(USERS_TABLE);
            statement.executeUpdate(PASSWORDS_TABLE);
            x = statement.executeUpdate("INSERT OR IGNORE INTO users (username, master_password, salt) VALUES ('testUser', 'testMaster', 'testSalt')");
            y = statement.executeUpdate("INSERT OR IGNORE INTO passwords (user_id, platform, password, username) VALUES ((SELECT id FROM users WHERE username = 'testUser'), 'testPlatform', 'testPassword', 'testUsername')");
            //other tables/data as needed
            if (y > 0 && x > 0) {
                System.out.println("Tables created successfully");
            } else {
                System.out.println("Table creation failed");
            }
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }

        UserSession.getInstance("testUser");

    }

    @AfterAll
    public static void tearDown() throws SQLException {
        //Delete all data from the 'passwords' and 'users' table
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM passwords");
            statement.executeUpdate("DELETE FROM users");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }

        connection.close();
        UserSession.endSession();
    }

    @Test
    void testAddPassword() throws Exception {
        
        try{
            Database.addPassword("testUsername", "testPlatform2", "testPlatformPassword");
            PreparedStatement statement = connection.prepareStatement("SELECT password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?");
            statement.setString(1, "testUser");
            statement.setString(2, "testPlatform2");
            ResultSet resultSet = statement.executeQuery();

            Assertions.assertTrue(resultSet.next(), "Password should exist in database");
            Assertions.assertEquals("testPlatformPassword", resultSet.getString("password"), "Password should match expected value");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testCreateUser() {

        try {
            boolean result = Database.createUser("testUser2", "newTestMaster", "newTestSalt");
            Assertions.assertTrue(result, "User should be created");

            //Verify user was created
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                statement.setString(1, "testUser2");
                ResultSet resultSet = statement.executeQuery();

                if(resultSet.next()){
                    Assertions.assertEquals("testUser2", resultSet.getString("username"), "Username should be 'testUser2'");
                }
                else {
                    Assertions.fail("User not found in database");
                }
            }
            catch (SQLException e) {
                Assertions.fail("Exception thrown: " + e.getMessage());
            }
        }
        catch (Exception e) {
            Assertions.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testDeletePassword() {
        try{
            Database.deletePassword("testPlatform", "testUsername");
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM passwords");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Assertions.assertEquals(0, resultSet.getInt(1), "There should be no rows in the password table");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testDeleteUser() {
        try{
            Database.deleteUser("testUser");

            PreparedStatement statement1 = connection.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet resultSet = statement1.executeQuery();
            resultSet.next();

            Assertions.assertEquals(0, resultSet.getInt(1), "There should be no rows in the database");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetMaster() {
        String master = Database.getMaster("testUser");
        Assertions.assertEquals("testMaster", master, "Master should match expected value");
    }

    @Test
    void testGetPasswordByUsername() {
        String[] info = Database.getPlatformInfo("testPlatform");
        Assertions.assertEquals("testPassword", info[1], "Password should match expected value");
        Assertions.assertEquals("testUsername", info[0], "Username should match expected value");
    }

    @Test
    void testGetSalt() {
        String salt = Database.getSalt("testUser");
        Assertions.assertEquals("testSalt", salt, "Salt should match expected value");
    }

    @Test
    void testInitializeDatabase() throws SQLException{
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "users", null);
        Assertions.assertTrue(resultSet.next(), "Users table should exist");

        resultSet = metaData.getTables(null, null, "passwords", null);
        Assertions.assertTrue(resultSet.next(), "Passwords table should exist");
    }

    @Test
    void testUpdatePassword() {
        try{
            Database.updatePassword( "testPlatform", "updatedPassword");
            PreparedStatement statement = connection.prepareStatement("SELECT password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?");
            statement.setString(1, "testUser");
            statement.setString(2, "testPlatform");
            ResultSet resultSet = statement.executeQuery();

            Assertions.assertTrue(resultSet.next(), "Password should exist in database");
            Assertions.assertEquals("updatedPassword", resultSet.getString("password"), "Password should match expected value");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetAllData() {
        try{
            List<Map<String, String>> expected = Arrays.asList(
                Map.of("username", "testUsername", "platform", "testPlatform", "password", "testPassword")
            );
            List<Map<String, String>> passwords = Database.getUserPasswords();
            Assertions.assertEquals(expected, passwords);
        }
        catch (Exception e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }
}