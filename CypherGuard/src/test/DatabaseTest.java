package test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.*;

import org.junit.jupiter.api.Assertions;
import main.Database;

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
            y = statement.executeUpdate("INSERT OR IGNORE INTO passwords (user_id, platform, password) VALUES ((SELECT id FROM users WHERE username = 'testUser'), 'testPlatform', 'testPassword')");
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
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    @Order(6)
    void testAddPassword() throws Exception {
        
        try{
            Database.addPassword("testUser", "testPlatform2", "testPlatformPassword");
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
    @Order(5)
    void testCreateUser() {

        try {
            Database.createUser("testUser2", "testMaster");

            //Verify user was created
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                statement.setString(1, "testUser2");
                ResultSet resultSet = statement.executeQuery();

                Assertions.assertTrue(resultSet.next(), "User should exist in database");
                Assertions.assertEquals("testUser2", resultSet.getString("username"), "Username should be 'testUser2'");
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
    @Order(8)
    void testDeletePassword() {
        try{
            Database.deletePassword("testUser2", "testPlatform");
            PreparedStatement statement = connection.prepareStatement("SELECT password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?");
            statement.setString(1, "testUser2");
            statement.setString(2, "testPlatform");
            ResultSet resultSet = statement.executeQuery();

            Assertions.assertFalse(resultSet.next(), "Password should not exist in database");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(9)
    void testDeleteUser() {
        try{
            Database.deleteUser("testUser2");

            PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement1.setString(1, "testUser2");
            ResultSet resultSet1 = statement1.executeQuery();
            Assertions.assertFalse(resultSet1.next(), "User should not exist in database");

            PreparedStatement statement2 = connection.prepareStatement("SELECT password FROM passwords WHERE user_id = (SELECT id FROM users WHERE username = ?) AND platform = ?");
            statement2.setString(1, "testUser2");
            statement2.setString(2, "testPlatform");
            ResultSet resultSet2 = statement2.executeQuery();
            Assertions.assertFalse(resultSet2.next(), "Password should not exist in database");
        }
        catch (SQLException e) {
            Assertions.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    @Order(4)
    void testGetMaster() {
        String master = Database.getMaster("testUser");
        Assertions.assertEquals("testMaster", master, "Master should match expected value");
    }

    @Test
    @Order(3)
    void testGetPasswordByUsername() {
        String password = Database.getPasswordByUsername("testUser", "testPlatform");
        Assertions.assertEquals("testPassword", password, "Password should match expected value");
    }

    @Test
    @Order(2)
    void testGetSalt() {
        String salt = Database.getSalt("testUser");
        Assertions.assertEquals("testSalt", salt, "Salt should match expected value");
    }

    @Test
    @Order(1)
    void testInitializeDatabase() throws SQLException{
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, "users", null);
        Assertions.assertTrue(resultSet.next(), "Users table should exist");

        resultSet = metaData.getTables(null, null, "passwords", null);
        Assertions.assertTrue(resultSet.next(), "Passwords table should exist");
    }

    @Test
    @Order(7)
    void testUpdatePassword() {
        try{
            Database.updatePassword("testUser", "testPlatform", "updatedPassword");
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
}