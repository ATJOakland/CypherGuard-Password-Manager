package main;

import java.util.ArrayList;
import java.util.List;

class PasswordEntry {
    private String name;
    private String username;
    private String password;

    public PasswordEntry(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

public class ViewPasswords {
    private List<PasswordEntry> passwordEntries;

    public ViewPasswords() {
        this.passwordEntries = new ArrayList<>();
    }

    public void addPasswordEntry(String name, String username, String password) {
        PasswordEntry entry = new PasswordEntry(name, username, password);
        passwordEntries.add(entry);
    }

    public void displayPasswords() {
        System.out.println("Saved Passwords:");
        for (PasswordEntry entry : passwordEntries) {
            System.out.println("Name: " + entry.getName());
            System.out.println("Username: " + entry.getUsername());
            System.out.println("Password: " + entry.getPassword());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ViewPasswords viewPasswords = new ViewPasswords();
        viewPasswords.addPasswordEntry("Example Name", "example_username", "example_password");
        viewPasswords.displayPasswords();
    }
}