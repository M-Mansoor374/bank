package services;

import models.Transaction;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.UUID;

/**
 * Transaction Service
 * Manages transaction history
 */
public class TransactionService {
    private static TransactionService instance;
    private List<Transaction> transactions;

    private TransactionService() {
        this.transactions = new ArrayList<>();
        initializeDefaultTransaction();
    }

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    /**
     * Initialize default transaction matching frontend
     */
    private void initializeDefaultTransaction() {
        Transaction initialTransaction = new Transaction(
            UUID.randomUUID().toString(),
            "ACC001",
            "Initial",
            10000.0,
            "Account opened with initial balance"
        );
        transactions.add(initialTransaction);
    }

    /**
     * Create a new transaction
     */
    public Transaction createTransaction(String accountId, String type, double amount, String description) {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(transactionId, accountId, type, amount, description);
        transactions.add(transaction);
        return transaction;
    }

    /**
     * Create a transfer transaction
     */
    public Transaction createTransferTransaction(String fromAccountId, String toAccountId, String type, double amount, String description) {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(transactionId, fromAccountId, type, amount, description, toAccountId);
        transactions.add(transaction);
        return transaction;
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getTransactionsByAccount(String accountId) {
        return transactions.stream()
            .filter(t -> t.getAccountId().equals(accountId))
            .collect(Collectors.toList());
    }

    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Get recent transactions for an account (limit)
     */
    public List<Transaction> getRecentTransactions(String accountId, int limit) {
        return transactions.stream()
            .filter(t -> t.getAccountId().equals(accountId))
            .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
            .limit(limit)
            .collect(Collectors.toList());
    }
}

