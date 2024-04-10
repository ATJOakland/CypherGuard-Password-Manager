package test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import main.AES;

public class AESTest {
    @Test
    void testEncryptResultsInExpectedBytes() throws Exception {
        String small = "Hello!";
        String medium = "Hello, World! Hello, World! Hello, World!";
        String large = "a".repeat(1000);
        String numbers = "1234567890";
        String ascii = "!#$%&*";
        
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";

        String encryptedSmall = AES.encrypt(small, key);
        System.out.println(encryptedSmall);
        String encryptedMedium = AES.encrypt(medium, key);
        System.out.println(encryptedMedium);
        String encryptedLarge = AES.encrypt(large, key);
        System.out.println(encryptedLarge);
        String encryptedNumbers = AES.encrypt(numbers, key);
        System.out.println(encryptedNumbers);
        String encryptedAscii = AES.encrypt(ascii, key);
        System.out.println(encryptedAscii);

        //Size should be (inputlength + 12(iv) + 16(tag)) * 2 (convert to hexadecimal)
        assertEquals(68, encryptedSmall.length(),
                "Encrypted text should be 68 characters long (34 bytes in hexadecimal)");
        assertEquals(138, encryptedMedium.length(),
                "Encrypted text should be 138 characters long (69 bytes in hexadecimal)");
        assertEquals(2056, encryptedLarge.length(),
                "Encrypted text should be 2056 characters long (1028 bytes in hexadecimal)");
        assertEquals(76, encryptedNumbers.length(), 
                "Encrypted text should be 76 characters long (1028 bytes in hexadecimal)");
        assertEquals(68, encryptedAscii.length(), 
                "Encrypted text should be 68 characters long (1028 bytes in hexadecimal)");
    }

    @Test
    void testDecryptReversesEncrypt() throws Exception {
        String encryptedText = "c45bc9be4fc312837bf0c3e4f281618cffbaf9e8da709a01d6fb2db9306b195197477f4d29c24e7936";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";
        String expectedText = "Hello, World!";
        String decryptedText = AES.decrypt(encryptedText, key);

        assertEquals(expectedText, decryptedText, "Decrypted text should match the original text");
    }

    @Test
    void testEncryptionDecryptionEmptyString() throws Exception {
        String originalText = "";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";

        assertThrows(IllegalArgumentException.class, () -> AES.encrypt(originalText, key));
        assertThrows(IllegalArgumentException.class, () -> AES.decrypt(originalText, key));
    }

    @Test
    void testEncryptionDecryptionInvalidKey() {
        String originalText = "Hello, World!";
        String invalidKey = "invalid key";

        assertThrows(Exception.class, () -> AES.encrypt(originalText, invalidKey));
        assertThrows(Exception.class, () -> AES.decrypt(originalText, invalidKey));
    }

    @Test
    void testEncryptNonAscii() throws Exception {
        String originalText = "你好，世界!";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";

        String encryptedText = AES.encrypt(originalText, key);

        assertEquals(88, encryptedText.length(),
                "Encrypted text should be 88 characters long (44 bytes in hexadecimal)");

        String decryptedText = AES.decrypt(encryptedText, key);

        assertEquals(originalText, decryptedText, "Decrypted text should match the original text");
    }
}