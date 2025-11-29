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
 * Transaction History Controller
 * Handles GET /transactions endpoint
 */
public class TransactionHistoryController implements HttpHandler {
    private SessionService sessionService;
    private AccountService accountService;
    private TransactionService transactionService;

    public TransactionHistoryController() {
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
            // Verify authentication
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

            // Get all transactions for the account
            List<Map<String, Object>> transactions = transactionService
                .getTransactionsByAccount(account.getAccountId())
                .stream()
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())) // Newest first
                .map(t -> {
                    Map<String, Object> tx = new HashMap<>();
                    tx.put("transactionId", t.getTransactionId());
                    tx.put("type", t.getType());
                    tx.put("amount", t.getAmount());
                    tx.put("timestamp", t.getTimestamp());
                    tx.put("description", t.getDescription());
                    if (t.getRelatedAccountId() != null) {
                        tx.put("relatedAccountId", t.getRelatedAccountId());
                    }
                    return tx;
                })
                .collect(Collectors.toList());

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("accountId", account.getAccountId());
            responseData.put("transactions", transactions);
            responseData.put("count", transactions.size());

            ResponseUtils.sendSuccess(exchange, "Transaction history retrieved successfully", responseData);

        } catch (Exception e) {
            ResponseUtils.sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private String getTokenFromRequest(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
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

