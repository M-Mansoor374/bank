package models;

/**
 * User Model
 * Represents a user in the banking system
 */
public class User {
    private String username;
    private String password;
    private String accountId;

    public User(String username, String password, String accountId) {
        this.username = username;
        this.password = password;
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}

