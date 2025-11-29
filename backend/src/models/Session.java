package models;

import java.util.UUID;

/**
 * Session Model
 * Represents an active user session
 */
public class Session {
    private String token;
    private String username;
    private long createdAt;
    private long expiresAt;

    public Session(String username) {
        this.token = UUID.randomUUID().toString();
        this.username = username;
        this.createdAt = System.currentTimeMillis();
        this.expiresAt = createdAt + (24 * 60 * 60 * 1000); // 24 hours
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isValid() {
        return System.currentTimeMillis() < expiresAt;
    }

    public void extend() {
        this.expiresAt = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Extend by 24 hours
    }
}

