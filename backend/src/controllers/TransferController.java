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
 * Transfer Controller
 * Handles POST /transfer endpoint
 */
public class TransferController implements HttpHandler {
    private SessionService sessionService;
    private AccountService accountService;
    private TransactionService transactionService;

    public TransferController() {
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
            var fromAccount = accountService.getAccountByUsername(username);

            if (fromAccount == null) {
                ResponseUtils.sendError(exchange, 404, "Account not found");
                return;
            }

            // Parse request body
            String requestBody = ResponseUtils.readRequestBody(exchange);
            Map<String, String> requestData = JSONUtils.parseJSON(requestBody);

            String toAccountId = requestData.get("toAccountId");
            String amountStr = requestData.get("amount");

            if (toAccountId == null || toAccountId.trim().isEmpty()) {
                ResponseUtils.sendError(exchange, 400, "Recipient account ID is required");
                return;
            }

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

            // Check if recipient account exists
            var toAccount = accountService.getAccount(toAccountId);
            if (toAccount == null) {
                ResponseUtils.sendError(exchange, 404, "Recipient account not found");
                return;
            }

            // Check if transferring to own account
            if (fromAccount.getAccountId().equals(toAccountId)) {
                ResponseUtils.sendError(exchange, 400, "Cannot transfer to your own account");
                return;
            }

            // Check sufficient balance
            if (!fromAccount.hasSufficientBalance(amount)) {
                ResponseUtils.sendError(exchange, 400, 
                    String.format("Insufficient balance. Available: Rs %.2f", fromAccount.getBalance()));
                return;
            }

            // Process transfer
            boolean success = accountService.transfer(fromAccount.getAccountId(), toAccountId, amount);
            if (!success) {
                ResponseUtils.sendError(exchange, 400, "Transfer failed");
                return;
            }

            // Create transaction records for both accounts
            String fromDescription = String.format("Transferred Rs %.2f to account %s", amount, toAccountId);
            String toDescription = String.format("Received Rs %.2f from account %s", amount, fromAccount.getAccountId());

            transactionService.createTransferTransaction(
                fromAccount.getAccountId(),
                toAccountId,
                "Transfer",
                amount,
                fromDescription
            );

            transactionService.createTransferTransaction(
                toAccountId,
                fromAccount.getAccountId(),
                "Transfer",
                amount,
                toDescription
            );

            // Get updated balance
            double newBalance = fromAccount.getBalance();

            // Prepare response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("amount", amount);
            responseData.put("toAccountId", toAccountId);
            responseData.put("newBalance", newBalance);
            responseData.put("message", String.format("Successfully transferred Rs %.2f to account %s. Remaining balance: Rs %.2f", 
                amount, toAccountId, newBalance));

            ResponseUtils.sendSuccess(exchange, "Transfer successful", responseData);

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

