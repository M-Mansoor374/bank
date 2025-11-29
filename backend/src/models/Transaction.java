package models;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Transaction Model
 * Represents a banking transaction
 */
public class Transaction {
    private String transactionId;
    private String accountId;
    private String type; // Deposit, Withdraw, Transfer
    private double amount;
    private String timestamp;
    private String description;
    private String relatedAccountId; // For transfers

    public Transaction(String transactionId, String accountId, String type, double amount, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = Instant.now().toString();
        this.description = description;
    }

    public Transaction(String transactionId, String accountId, String type, double amount, String description, String relatedAccountId) {
        this(transactionId, accountId, type, amount, description);
        this.relatedAccountId = relatedAccountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelatedAccountId() {
        return relatedAccountId;
    }

    public void setRelatedAccountId(String relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }
}

