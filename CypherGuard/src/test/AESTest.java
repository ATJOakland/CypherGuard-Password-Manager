package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import main.AES;

public class AESTest {
    @Test
    void testEncryptAndDecrypt() throws Exception{
        String originalText = "Hello, World!";
        String key = "000102030405060708090a0b0c0d0e0f000102030405060708090a0b0c0d0e0f";

        String encryptedText = AES.encrypt(originalText, key);
        System.out.println(encryptedText);

        String decryptedText = AES.decrypt(encryptedText, key);
        System.out.println(decryptedText);

        assertEquals(originalText, decryptedText, "Decrypted text should match original text");
    }
}