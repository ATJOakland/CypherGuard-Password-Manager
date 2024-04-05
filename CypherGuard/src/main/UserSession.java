package main;

public class UserSession {
    private static UserSession instance;

    private String username;

    private UserSession(String username) {
        this.username = username;
    }

    //Create new session
    public static UserSession getInstance(String username) {
        if (instance == null) {
            instance = new UserSession(username);
        }
        return instance;
    }

    //Get current session
    public static UserSession getInstance() {
        return instance;
    }

    public static void endSession(){
        instance = null;
    }

    public String getUsername() {
        return username;
    }
}
