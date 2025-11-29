package services;

import models.Session;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session Service
 * Manages user sessions and authentication tokens
 */
public class SessionService {
    private static SessionService instance;
    private Map<String, Session> activeSessions; // token -> session
    private Map<String, String> userToToken; // username -> token

    private SessionService() {
        this.activeSessions = new ConcurrentHashMap<>();
        this.userToToken = new ConcurrentHashMap<>();
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    /**
     * Create a new session for a user
     */
    public Session createSession(String username) {
        // Remove existing session if any
        String existingToken = userToToken.get(username);
        if (existingToken != null) {
            activeSessions.remove(existingToken);
        }

        // Create new session
        Session session = new Session(username);
        activeSessions.put(session.getToken(), session);
        userToToken.put(username, session.getToken());
        return session;
    }

    /**
     * Get session by token
     */
    public Session getSession(String token) {
        if (token == null) {
            return null;
        }
        Session session = activeSessions.get(token);
        if (session != null && session.isValid()) {
            session.extend(); // Extend session on access
            return session;
        } else if (session != null) {
            // Session expired, remove it
            activeSessions.remove(token);
            userToToken.remove(session.getUsername());
        }
        return null;
    }

    /**
     * Get username from token
     */
    public String getUsername(String token) {
        Session session = getSession(token);
        return session != null ? session.getUsername() : null;
    }

    /**
     * Invalidate session
     */
    public void invalidateSession(String token) {
        Session session = activeSessions.remove(token);
        if (session != null) {
            userToToken.remove(session.getUsername());
        }
    }

    /**
     * Invalidate all sessions for a user
     */
    public void invalidateUserSessions(String username) {
        String token = userToToken.remove(username);
        if (token != null) {
            activeSessions.remove(token);
        }
    }

    /**
     * Check if token is valid
     */
    public boolean isValidToken(String token) {
        return getSession(token) != null;
    }
}

