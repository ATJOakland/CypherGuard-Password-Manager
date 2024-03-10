package main;


import java.util.HashMap;

public class TestData {
    // Moodle HashMap
    public HashMap<String, String> moodle = new HashMap<>();
    
    // Twitter HashMap
    public HashMap<String, String> twitter = new HashMap<>();

    // Instagram HashMap
    public HashMap<String, String> instagram = new HashMap<>();
    
    // Discord HashMap
    public HashMap<String, String> discord = new HashMap<>();

    // HashMap Array
    /*
     * I'm adding all of these HashMaps to an array called accounts because in the future, when we aren't using pre-defined Hashmaps and they are being
     * created dynamically in the app, we can add that dynamically created HashMap to this array. Then, in another file (for now it's CypherGuard.java),
     * we use a for loop to loop through this array of HashMaps, and then encrypt or decrypt or look up an account or do whatever with it.
     * It's to make things easier in the future (hopefully).
    */
    @SuppressWarnings("rawtypes")
    HashMap[] accounts = {moodle, twitter, instagram, discord};

    // Constructor to initialize HashMaps
    public TestData() {
        // Moodle Key-Value pairs
        moodle.put("Name", "Moodle");
        moodle.put("Username", "moodle_username");
        moodle.put("Password", "password123");
        
        // Twitter Key-Value pairs
        twitter.put("Name", "Twitter");
        twitter.put("Username", "twitter_username");
        twitter.put("Password", "123password");

        // Instagram Key-Value pairs
        instagram.put("Name", "Instagram");
        instagram.put("Username", "insta_username");
        instagram.put("Password", "1234567890");

        // Discord Key-Value pairs
        discord.put("Name", "Discord");
        discord.put("Username", "this_is_my_discord_username");
        discord.put("Password", "I can't think of a password for this.");
    }
}
