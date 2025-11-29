package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.SessionService;
import services.AccountService;
import services.TransactionService;
import utils.ResponseUtils;
import utils.JSONUtils;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Withdraw Controller
 * Handles POST /withdraw endpoint
 */
public class WithdrawController implements HttpHandler {
    private SessionService sessionService;
    private AccountService accountService;
    private TransactionService transactionService;

    public WithdrawController() {
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

        if (!"POST".equals(exchange.getRequestMethod())) {
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

            // Parse request body
            String requestBody = ResponseUtils.readRequestBody(exchange);
            Map<String, String> requestData = JSONUtils.parseJSON(requestBody);

            String amountStr = requestData.get("amount");
            if (amountStr == null || amountStr.trim().isEmpty()) {
                ResponseUtils.sendError(exchange, 400, "Amount is required");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                ResponseUtils.sendError(exchange, 400, "Invalid amount format");
                return;
            }

            // Validate amount
            if (amount <= 0) {
                ResponseUtils.sendError(exchange, 400, "Amount must be positive");
                return;
            }

            // Check sufficient balance
            if (!account.hasSufficientBalance(amount)) {
                ResponseUtils.sendError(exchange, 400, 
                    String.format("Insufficient balance. Available: Rs %.2f", account.getBalance()));
                return;
            }

            // Process withdrawal
            boolean success = accountService.withdraw(account.getAccountId(), amount);
            if (!success) {
                ResponseUtils.sendError(exchange, 400, "Withdrawal failed");
                return;
            }

            // Create transaction record
            String description = String.format("Withdrew Rs %.2f", amount);
            transactionService.createTransaction(
                account.getAccountId(),
                "Withdraw",
                amount,
                description
            );

            // Get updated balance
            double newBalance = account.getBalance();

            // Prepare response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("amount", amount);
            responseData.put("newBalance", newBalance);
            responseData.put("message", String.format("Successfully withdrew Rs %.2f. Remaining balance: Rs %.2f", amount, newBalance));

            ResponseUtils.sendSuccess(exchange, "Withdrawal successful", responseData);

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

