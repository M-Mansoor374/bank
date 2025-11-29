package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.AuthService;
import services.SessionService;
import services.AccountService;
import utils.ResponseUtils;
import utils.JSONUtils;
import java.io.IOException;
import java.util.Map;

/**
 * Login Controller
 * Handles POST /login endpoint
 */
public class LoginController implements HttpHandler {
    private AuthService authService;
    private SessionService sessionService;
    private AccountService accountService;

    public LoginController() {
        this.authService = AuthService.getInstance();
        this.sessionService = SessionService.getInstance();
        this.accountService = AccountService.getInstance();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            ResponseUtils.handleCORS(exchange);
            return;
        }

        if (!"POST".equals(exchange.getRequestMethod())) {
            ResponseUtils.sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            String requestBody = ResponseUtils.readRequestBody(exchange);
            Map<String, String> requestData = JSONUtils.parseJSON(requestBody);

            String username = requestData.get("username");
            String password = requestData.get("password");

            // Validate input
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                ResponseUtils.sendError(exchange, 400, "Username and password are required");
                return;
            }

            // Authenticate user
            var user = authService.authenticate(username, password);
            if (user == null) {
                ResponseUtils.sendError(exchange, 401, "Invalid username or password");
                return;
            }

            // Create session
            var session = sessionService.createSession(username);
            
            // Get account info
            var account = accountService.getAccountByUsername(username);

            // Prepare response data
            Map<String, Object> responseData = new java.util.HashMap<>();
            responseData.put("token", session.getToken());
            responseData.put("username", username);
            responseData.put("accountId", account != null ? account.getAccountId() : "");
            responseData.put("balance", account != null ? account.getBalance() : 0.0);

            ResponseUtils.sendSuccess(exchange, "Login successful", responseData);

        } catch (Exception e) {
            ResponseUtils.sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }
}

