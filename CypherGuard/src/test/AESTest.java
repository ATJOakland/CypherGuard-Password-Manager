package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import main.AES;

public class AESTest {
    @Test
    void testEncryptResultsInExpectedBytes() throws Exception{
        String originalText = "Hello, World!";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";

        String encryptedText = AES.encrypt(originalText, key);
        System.out.println(encryptedText);

        assertEquals(82, encryptedText.length(), "Encrypted text should be 82 characters long (41 bytes in hexadecimal)");
    }

    @Test
    void testDecryptReversesEncrypt() throws Exception{
        String encryptedText = "c45bc9be4fc312837bf0c3e4f281618cffbaf9e8da709a01d6fb2db9306b195197477f4d29c24e7936";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";
        String expectedText = "Hello, World!";
        String decryptedText = AES.decrypt(encryptedText, key);

        assertEquals(expectedText, decryptedText, "Decrypted text should match the original text");
    }


}