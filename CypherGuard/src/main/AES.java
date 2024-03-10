package main;
import java.security.SecureRandom;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;


public class AES {
    public static String encrypt(String input, String key) 
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException{
        
        //Generate 12 byte IV as per NIST reccomendation
        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        
        //Create and initialize the cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Key.hexToBytes(key), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

        byte[] cipherText = cipher.doFinal(input.getBytes());

        //Prepend IV to the cipherText
        byte[] ivAndCipherText = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, ivAndCipherText, 0, iv.length);
        System.arraycopy(cipherText, 0, ivAndCipherText, iv.length, cipherText.length);

        //Convert to hex for database storage
        return Key.bytesToHex(ivAndCipherText);
    }

    public static String decrypt(String cipherText, String key)
        throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
        InvalidKeyException, BadPaddingException, IllegalBlockSizeException{

        //Convert hex to bytes
        byte[] ivAndCipherText = Key.hexToBytes(cipherText);

        //Separate IV and cipherText
        byte[] iv = Arrays.copyOfRange(ivAndCipherText, 0, 12);
        byte[] cipherTextBytes = Arrays.copyOfRange(ivAndCipherText, 12, ivAndCipherText.length);

        //Create and initialize the cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Key.hexToBytes(key), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
        
        byte[] decodedText = cipher.doFinal(cipherTextBytes);
        String decodedString = new String(decodedText);

        return decodedString;
    }
}