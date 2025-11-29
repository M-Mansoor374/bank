package services;

import models.User;
import models.Session;
import java.util.Map;
import java.util.HashMap;

/**
 * Authentication Service
 * Handles user authentication
 */
public class AuthService {
    private static AuthService instance;
    private Map<String, User> users; // username -> user

    private AuthService() {
        this.users = new HashMap<>();
        initializeDefaultUser();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    /**
     * Initialize default user matching frontend credentials
     */
    private void initializeDefaultUser() {
        // Create default user matching frontend: user@gmail.com / 12345
        User defaultUser = new User("user@gmail.com", "12345", "ACC001");
        users.put("user@gmail.com", defaultUser);
    }

    /**
     * Authenticate user
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.validatePassword(password)) {
            return user;
        }
        return null;
    }

    /**
     * Get user by username
     */
    public User getUser(String username) {
        return users.get(username);
    }

    /**
     * Register a new user (for future expansion)
     */
    public boolean registerUser(String username, String password, String accountId) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        User user = new User(username, password, accountId);
        users.put(username, user);
        return true;
    }
}

