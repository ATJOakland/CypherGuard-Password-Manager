package test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.Key;

public class KeyTest {
    @Test
    void testAuthenticate() throws Exception{
        String password = "password";
        String salt = Key.generateSalt();
        String derivedKey = Key.deriveKey(password, salt);
        boolean result = Key.authenticate(password, derivedKey, salt);

        assertTrue(result, "Authentication should succeed with correct password and derived key");
    }

    @Test
    void testBytesToHex() {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04};
        String hex = Key.bytesToHex(bytes);

        assertEquals("01020304", hex, "Hexadecimal string should match expected value");
    }

    @Test
    void testDeriveKey() throws Exception {
        String password = "password";
        String salt = "somesalt";
        String derivedKey = Key.deriveKey(password, salt);

        assertNotNull(derivedKey, "Derived key should not be null");
        assertEquals(64, derivedKey.length(), "Derived key should be 64 characters long (256 bits in hexadecimal)");
    }

    @Test
    void testGenerateSalt() throws Exception{
        String salt = Key.generateSalt();

        assertNotNull(salt, "Generated salt should not be null");
        assertEquals(64, salt.length(), "Generated salt should be 64 characters long (256 bits in hexadecimal");
    }

    @Test
    void testHexToBytes() {
        String hex = "01020304";
        byte[] bytes = Key.hexToBytes(hex);

        assertArrayEquals(new byte[] {0x01, 0x02, 0x03, 0x04}, bytes, "Byte array should match expected value");
    }
}