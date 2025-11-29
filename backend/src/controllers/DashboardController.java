package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.SessionService;
import services.AccountService;
import services.TransactionService;
import utils.ResponseUtils;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dashboard Controller
 * Handles GET /dashboard endpoint
 */
public class DashboardController implements HttpHandler {
    private SessionService sessionService;
    private AccountService accountService;
    private TransactionService transactionService;

    public DashboardController() {
        this.sessionService = SessionService.getInstance();
        this.accountService = AccountService.getInstance();
        this.transactionService = TransactionService.getInstance();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            ResponseUtils.handleCORS(exchange);
            return;
        }

        if (!"GET".equals(exchange.getRequestMethod())) {
            ResponseUtils.sendError(exchange, 405, "Method not allowed");
            return;
        }

        try {
            // Get token from Authorization header or query parameter
            String token = getTokenFromRequest(exchange);
            
            if (token == null || !sessionService.isValidToken(token)) {
                ResponseUtils.sendError(exchange, 401, "Unauthorized. Please login first.");
                return;
            }

            String username = sessionService.getUsername(token);
            var account = accountService.getAccountByUsername(username);

            if (account == null) {
                ResponseUtils.sendError(exchange, 404, "Account not found");
                return;
            }

            // Get recent transactions (last 5)
            List<Map<String, Object>> recentTransactions = transactionService
                .getRecentTransactions(account.getAccountId(), 5)
                .stream()
                .map(t -> {
                    Map<String, Object> tx = new HashMap<>();
                    tx.put("transactionId", t.getTransactionId());
                    tx.put("type", t.getType());
                    tx.put("amount", t.getAmount());
                    tx.put("timestamp", t.getTimestamp());
                    tx.put("description", t.getDescription());
                    return tx;
                })
                .collect(Collectors.toList());

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("username", username);
            responseData.put("accountId", account.getAccountId());
            responseData.put("balance", account.getBalance());
            responseData.put("recentTransactions", recentTransactions);

            ResponseUtils.sendSuccess(exchange, "Dashboard data retrieved successfully", responseData);

        } catch (Exception e) {
            ResponseUtils.sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private String getTokenFromRequest(HttpExchange exchange) {
        // Try Authorization header first
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Try query parameter
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }

        return null;
    }
}

