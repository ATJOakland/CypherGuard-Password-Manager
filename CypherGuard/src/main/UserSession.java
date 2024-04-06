package main;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class UserSession {
    private static ObjectProperty<UserSession> instance = new SimpleObjectProperty<>();
    private String username;

    private UserSession(String username) {
        this.username = username;
    }

    //Create new session
    public static UserSession getInstance(String username) {
        if (instance.get() == null) {
            instance.set(new UserSession(username));
        }
        return instance.get();
    }

    //Get current session
    public static UserSession getInstance() {
        return instance.get();
    }

    //End current session
    public static void endSession(){
        instance.set(null);
    }

    //Get username
    public String getUsername() {
        return username;
    }

    //Get instance property
    public static ObjectProperty<UserSession> instanceProperty() {
        return instance;
    }
}
