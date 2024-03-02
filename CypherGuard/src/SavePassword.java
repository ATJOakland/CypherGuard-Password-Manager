/*
 * This script does the following:
 * 1. Create a new file to store the passwords in
 * 2. Save the text to the file
 * 3. Encrypts the txt file
*/

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
 
public class SavePassword {
    
    // This is the key used for the encryption and decryption. This key can be anything.
    private static final String CYPHER_KEY = "our_group_is_the_best"; // DON'T CHANGE. If you change this then every password must be changed which would be a lot of work.

    // File Variables
    static String fileName;
    static String filePath;

    // Temporary File Object for retrieving data.
    static File accountFileForDecryption; // Will be set equal to the passwordFile object, so that decryptPasswordFile can decrypt the whole thing to the console.

    // This function is called in CypherGuard.java (for testing). It should be called for each Account HashMap
    @SuppressWarnings("rawtypes")
    public void savePasswordToFile(HashMap accountHashMap) throws IOException {
        // Filename
        fileName = "passwords.txt";

        // Encrypted Username and Passwords that will be defined in the if/else below.
        String encryptedUsername;
        String encryptedPassword;
        
        // Check if the Operating System is Windows or Mac. File paths are different between the two systems.
        // Then set the relative file path
        filePathDecider();

        // Create the file object with the path and filename
        File passwordFile = new File(filePath, fileName);
        accountFileForDecryption = passwordFile; // This is for getting credientials from the text file in getAccountCredentials()

        // If the file doesn't exist then create it.
        if (!passwordFile.exists()) {
            passwordFile.createNewFile();
        }

        // Check if the HashMap is already saved, and if it is then do something (for now it does nothing but later it can update the username and password)\\
        try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Account Name: ") && line.substring(14).equals(accountHashMap.get("Name"))) {
                    return; // If an account was found/already exists, for now, just return and do nothing.
                }
            }
        }

        // Encrypt the password and username of the hashmap
        if (accountHashMap != null && !accountHashMap.isEmpty()){
            encryptedUsername = xorEncrypt((String) accountHashMap.get("Username")); // "(String)" converts the Username/Password to a string from an object because Java is beign weird.
            encryptedPassword = xorEncrypt((String) accountHashMap.get("Password"));
        } else {
            // If null or empty:
            System.out.println("Error: Account HashMap is null or empty.");
            throw new IllegalStateException("Account HashMap is null or empty.");
        }

        // Write the password to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile, true))) {
            writer.newLine(); // Makes a new line in the file
            writer.write("Account Name: " + accountHashMap.get("Name") + "\n");
            writer.write("Username: " + encryptedUsername + "\n");
            writer.write("Password: " + encryptedPassword + "\n");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving password to file: " + e.getMessage());
        }
    }

    /* ================ XOR Hexa-Deci Encryption operation ================
     *
     * Code explanation:
     * Video used: https://www.youtube.com/watch?v=1MmlDltKGmo
     * 
     * 
     * For Encryption:
     * 
     * xorEncrypt():
     * 1. xorEncrypt() takes a string as input (this would most likely be the username and password combination).
     * 2. There are two default variables, encryptedStringAsHexa which is an empty string, and keyIteration which is set at 0.
     * 3. In the for loop, as long as i is less than the length of the text (username and password), declare a temporary integer,
     *      this temporary int is the equal to the character position of the username/password, to the power of the character position of the CYPHER_KEY, at the keyIteration.
     * 4. Add the new integer to the encryptedStringAsHexa as a string and then add 1 to the keyIteration (so it moves on to the next character in the CYPHER_KEY)
     * 5. If the keyIteration is greater than the CYPHER_KEY's length, go back to the first letter of the CYPHER_KEY and start again.
     * 6. Lastly, it returns the encryptedStringAsHexa as a string. This should be a long series of numbers in the text file.
     * 
     * 
     * 
     * For Decryption:
     * 
     * xorDecrypt():
     * 1. It takes a hexcodeText as an argument. Right now at the end of savePasswordToFile(), it calls this for every line in the text file. For the actual project, we should
     *      find a way to search applications and only return what is being searched for, not everything.
     * 2. It takes the hexcodeText, splits it into a pair of two, then declares a decimal from the parsed split, and adds it to the hexToDecimal string (This is complicated I know it's confusing to me too.)
     * 3. When that's done, basically the exact same thing as xorEncrypt() is done, but for the hexToDecimal. It's converting the decimal series of numbers back into a string.
     * 4. It returns the original text as decryptedText. For right now, this is being put into the console.
     * 
    */

    private static String xorEncrypt(String text) {
        String encryptedStringAsHexa = "";
        int keyIteration = 0;
        
        for (int i = 0; i < text.length(); i++) {
            int temp = text.charAt(i) ^ CYPHER_KEY.charAt(keyIteration);

            encryptedStringAsHexa += String.format("%02x", (byte)temp);
            keyIteration++;

            // Once all of the characters in the KEY are used, start over.
            if (keyIteration >= CYPHER_KEY.length()) {
                keyIteration = 0;
            }
        }

        return encryptedStringAsHexa;
    }

    private static String xorDecrypt(String hexcodeText) {
        // Splitting the hex string into a decimal.
        String hexToDecimal = "";

        for (int i = 0; i < hexcodeText.length() - 1; i += 2) {
            // Split the hex into a pair of two
            String output = hexcodeText.substring(i, (i+2));

            int decimal = Integer.parseInt(output, 16);

            hexToDecimal += (char)decimal;
        }

        // Decrypting the decimal string
        String decryptedText = "";
        int keyIteration = 0;
        for (int i = 0; i < hexToDecimal.length(); i++) {
            int temp = hexToDecimal.charAt(i) ^ CYPHER_KEY.charAt(keyIteration);

            decryptedText += (char)temp;
            keyIteration++;

            // Once all of the characters in the KEY are used, start over.
            if (keyIteration >= CYPHER_KEY.length()) {
                keyIteration = 0;
            }
        }

        return decryptedText;
    }

    // Function retrieve specific accounts from the txt file using the accountName. 
    public HashMap<String, String> getAccountCredentials(String accountName) throws IOException {
        String currentLine;
        HashMap<String, String> credentials = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath + "/" + fileName))) {

            // Read file line by line
            while ((currentLine = reader.readLine()) != null) {
                // Check if the line contains the account name
                if (currentLine.startsWith("Account Name: ") && currentLine.substring(14).equals(accountName)) {
                    // Parse the next two lines to get the username and password
                    String usernameLine = reader.readLine();
                    String passwordLine = reader.readLine();

                    // Get the username and password
                    String username = usernameLine.substring(10); // This removes the "Username: "
                    String password = passwordLine.substring(10); // This removes the "Password: "

                    // Decrypt the username and password
                    username = xorDecrypt(username);
                    password = xorDecrypt(password);

                    // Add the username and password to the HashMap
                    credentials.put("Username", username);
                    credentials.put("Password", password);

                    break; // Stop the loop because the account was found
                }
            }
        }
        
        return credentials; // Return the credentials HashMap
    }

    // Functions for checking the operating system
    // Check if OS is windows
    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    // Check if OS is mac
    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac");
    }

    // Uses isWindows() and isMac() to check OS and decide the file path.
    private static void filePathDecider() {
        if (isWindows()) {
            filePath = "lib\\passwordFile";
        } else if (isMac()) {
            filePath = "lib/passwordFile";
        } else {
            System.out.println("Unsupported operating system!");
            return;
        }
    }
}
