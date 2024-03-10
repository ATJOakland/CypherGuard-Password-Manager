package main;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Key {
    //Implementation of PBKDF2
    public static String deriveKey(String password, String salt) 
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{

        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] key = null;

        try {
            //Derive the 256 bit key, given password and salt
            PBEKeySpec spec = new PBEKeySpec(chars, hexToBytes(salt), iterations, 32 * 8);

            //Specifying the algorithm
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            //Generate the key
            key = skf.generateSecret(spec).getEncoded();
        } 
        catch (Exception e) {
            System.out.println("Error deriving key from password: " + e.getMessage());
        }

        if (key == null) {
            System.out.println("Error deriving key from password: key is null");
        }

        return bytesToHex(key);
    }

    //Authentication method
    public static Boolean authenticate(String inputPassword, String masterPassword, String salt) 
        throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{

        boolean authenticated = false;
        String hashedPassword = null;

        try{
            //Hash the input password
            hashedPassword = deriveKey(inputPassword, salt);
        }
        catch (Exception e) {
            System.out.println("Error authenticating password: " + e.getMessage());
        }

        if (hashedPassword == null) {
            System.out.println("Error authenticating password: Input password was not hashed");
            authenticated = false;
        }
        else {
            //Compare the hashed input password with the hashed master password
            authenticated = hashedPassword.equals(masterPassword);
        }

        return authenticated;
    }

    //Salt generation 32 bytes
    public static String generateSalt() throws NoSuchAlgorithmException, NoSuchProviderException{

        byte[] salt = new byte[32];
        try{
            //Generate a random salt
            SecureRandom.getInstanceStrong().nextBytes(salt);
        }
        catch (Exception e) {
            System.out.println("Error generating salt: " + e.getMessage());
        }

        //Check if the salt is empty (SecureRandom failed to generate a salt)
        boolean isEmpty = true;
        for (byte b : salt) {
            if (b != 0) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            throw new RuntimeException("Error generating salt: salt is empty");
        }

        return bytesToHex(salt);
    }

    //Convert bytes to hex
    public static String bytesToHex(byte[] array) {
        StringBuilder hexString = new StringBuilder(array.length * 2);
        for (byte b: array) {
            String hex = Integer.toHexString(0xff & b);

            //If byte is less than 16, add a 0 to the hex string
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    //Convert hex to bytes
    public static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}